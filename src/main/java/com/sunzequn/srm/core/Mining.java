package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.bean.Pattern;
import com.sunzequn.srm.bean.PatternInstance;
import com.sunzequn.srm.bean.Vertice;
import com.sunzequn.srm.utils.ListUtil;
import com.sunzequn.srm.utils.RDFUtil;
import com.sunzequn.srm.utils.ReadUtil;
import com.sunzequn.srm.utils.TimeUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;
import org.apache.log4j.xml.SAXErrorHandler;

import java.util.*;

/**
 * Created by sloriac on 16-11-17.
 *
 * 挖掘的主程序，控制计算步骤和流程
 */
public class Mining {

    private static final int THREAD_NUM = 10;
    private static final int K = 1;
    private Logger logger = Logger.getLogger(Mining.class);
    private TimeUtil timer = new TimeUtil(Mining.class);
    private Conf conf;

    private LinkedList<String[]> linkedInstancePairs = new LinkedList<>();
    private Map<String, Pattern> typePatternMap = new HashMap<>();
    private List<Map<String, Pattern>> patternMaps = new ArrayList<>();

    private Set<String> kb1LinkedInstances = new HashSet<>();
    private Set<String> kb2LinkedInstances = new HashSet<>();
    private Map<String, Set<String>> kb1LinkToKb2 = new HashMap<>();
    private Map<String, Set<String>> kb2LinkToKb1 = new HashMap<>();


    public Mining(String confFile) {
        conf = new Conf(confFile);
        for (int i = 0; i < K; i++) {
            Map<String, Pattern> patternMap = new HashMap<>();
            patternMaps.add(patternMap);
        }
    }

    private void run() {
        /*
        1）读取链接实例对
        linkedInstancePairs 填充完毕
         */
        readLinkedInstances(10000);
        /*
        2）初始化每个链接实例的k连接链
        typePatternMap填充完毕
        patternMap填充完毕
         */
        generateVertices();

//        for (String key : typePatternMap.keySet()) {
//            System.out.println(key);
//        }

        for (int i = 0; i < K; i++) {
            Map<String, Pattern> patternMap = patternMaps.get(i);
            for (String s : patternMap.keySet()) {
                logger.info(s + " " + patternMap.get(s).getInstancesNum());
            }
        }

    }

    private void readLinkedInstances(int n) {
        timer.start();
        ReadUtil readUtil = new ReadUtil(conf.getLinkedInstance());

        //开发环境，随机取n个
//        List<String> oriLines = readUtil.readByLine();
//        Collections.shuffle(oriLines);
//        List<String> lines = oriLines.subList(0, n);
        List<String> lines = readUtil.readByLine();

        for (String line : lines) {
            String[] li = RDFUtil.parseTTLLine(line);
            linkedInstancePairs.add(li);
            assert li != null;
            kb1LinkedInstances.add(li[0]);
            kb2LinkedInstances.add(li[1]);
            kb1LinkToKb2 = addKbLinkToKb(kb1LinkToKb2, li[0], li[1]);
            kb2LinkToKb1 = addKbLinkToKb(kb2LinkToKb1, li[1], li[0]);
        }
        timer.print("链接实例数量： " + linkedInstancePairs.size());
    }


    private void generateVertices() {
        timer.start();
        Vector<Thread> threads = new Vector<>();
        for (int i = 0; i < THREAD_NUM; i++) {
            Thread thread = new Thread(() -> {
                ChainHandler chainHandler = new ChainHandler();
                PatternGenerator patternGenerator = new PatternGenerator();
                while (true) {
                    try {
                        String[] pair = popPair();
                        if (pair == null) return;
                        Thread.sleep((long) (Math.random() * 100));
                        Vertice vertice1 = chainHandler.kConnectivityPopulation(pair[0], K, 1, conf);
                        // 对于kb2，也就是规则头，只查询1连接就行
                        Vertice vertice2 = chainHandler.kConnectivityPopulation(pair[1], 1, 2, conf);
                        if (vertice1.isHasNext() && vertice2.isHasNext()) {
//                            generateTypePattern(vertice1, vertice2, chainHandler, patternGenerator);
                            generatePattern(vertice1, vertice2, chainHandler, patternGenerator);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "thread" + i);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                // 等待所有线程执行完毕
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        timer.print("类型封闭模式数量： " + typePatternMap.size());
        for (int i = 1; i <= K; i++) {
            logger.info(i + "封闭链接数量： " + patternMaps.get(i - 1).size());
        }
    }


    private synchronized void generatePattern(Vertice v1, Vertice v2, ChainHandler chainHandler, PatternGenerator patternGenerator) {
        // 对于kb2，也就是规则头，只查询1连接就行
        List<String[]> closedChains2 = chainHandler.findClosedKConnectivityChains(v2, 1, kb2LinkedInstances);
        for (int i = 1; i <= K; i++) {
            Map<String, Pattern> patternMap = patternMaps.get(i - 1);
            List<String[]> closedChains1 = chainHandler.findClosedKConnectivityChains(v1, i, kb1LinkedInstances);
            if (!ListUtil.isEmpty(closedChains1) && !ListUtil.isEmpty(closedChains2)) {
                for (String[] chain1 : closedChains1) {
                    for (String[] chain2 : closedChains2) {
                        String endV1 = chain1[chain1.length - 1];
                        String endV2 = chain2[chain2.length - 1];
                        Set<String> linkedInstance = kb1LinkToKb2.get(endV1);
                        if (linkedInstance.contains(endV2)) {
                            PatternInstance patternInstance = new PatternInstance(chain2, chain1);
//                            logger.info(patternInstance.toString());
                            patternGenerator.generatePatterns(patternMap, patternInstance);
                        }
                    }
                }
            }
        }
    }

    private synchronized void generateTypePattern(Vertice v1, Vertice v2, ChainHandler chainHandler, PatternGenerator patternGenerator) {
        List<String[]> typePatterns1 = chainHandler.findTypedChains(v1, conf.getOntologyNS(1));
        List<String[]> typePatterns2 = chainHandler.findTypedChains(v2, conf.getOntologyNS(2));
        if (ListUtil.isEmpty(typePatterns1) || ListUtil.isEmpty(typePatterns2)) return;
        for (String[] pattern1 : typePatterns1) {
            for (String[] pattern2 : typePatterns2) {
                PatternInstance patternInstance = new PatternInstance(pattern2, pattern1);
//                logger.info(patternInstance.toString());
                patternGenerator.generateTypePatterns(typePatternMap, patternInstance);
            }
        }
    }


    private synchronized String[] popPair() {
        return linkedInstancePairs.size() > 0 ? linkedInstancePairs.pop() : null;
    }


    private Map<String, Set<String>> addKbLinkToKb(Map<String, Set<String>> kbLinkedToKb, String instance1, String instance2) {
        if (kbLinkedToKb.containsKey(instance1))
            kbLinkedToKb.get(instance1).add(instance2);
        else {
            Set<String> set = new HashSet<>();
            set.add(instance2);
            kbLinkedToKb.put(instance1, set);
        }
        return kbLinkedToKb;
    }


    public static void main(String[] args) {
        String confFile;
        if (args.length > 0) {
            confFile = args[0];
        } else {
            confFile = "target/classes/input.properties";
        }
        Mining m = new Mining(confFile);
        m.run();
    }


}
