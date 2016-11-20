package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.bean.Pattern;
import com.sunzequn.srm.bean.Vertice;
import com.sunzequn.srm.utils.ListUtil;
import com.sunzequn.srm.utils.RDFUtil;
import com.sunzequn.srm.utils.ReadUtil;
import com.sunzequn.srm.utils.TimeUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by sloriac on 16-11-17.
 */
public class Mining {

    private static final int THREAD_NUM = 50;
    private static final int K = 1;
    private Logger logger = Logger.getLogger(Mining.class);
    private TimeUtil timer = new TimeUtil(Mining.class);
    private Conf conf;

    private LinkedList<String[]> linkedInstancePairs = new LinkedList<>();
    private List<Pattern> patterns = new ArrayList<>();
    private List<Pattern> typePatterns = new ArrayList<>();

    private Set<String> kb1LinkedInstances = new HashSet<>();
    private Set<String> kb2LinkedInstances = new HashSet<>();
    private Map<String, Set<String>> kb1LinkToKb2 = new HashMap<>();
    private Map<String, Set<String>> kb2LinkToKb1 = new HashMap<>();


    public Mining(String confFile) {
        conf = new Conf(confFile);
    }

    private void run() {
        // 1）读取链接实例对
        readLinkedInstances();
        // 2）初始化每个链接实例的k连接链
        generateVertices();

    }

    private void readLinkedInstances() {
        timer.start();
        ReadUtil readUtil = new ReadUtil(conf.getLinkedInstance());
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
                while (true) {
                    try {
                        String[] pair = popPair();
                        if (pair == null) return;
                        Thread.sleep((long) (Math.random() * 100));
                        Vertice vertice1 = chainHandler.kConnectivityPopulation(pair[0], K, 1, conf);
                        // 对于kb2，也就是规则头，只查询1连接就行
                        Vertice vertice2 = chainHandler.kConnectivityPopulation(pair[1], 1, 2, conf);
                        if (vertice1.isHasNext() && vertice2.isHasNext()) {
                            generateTypePattern(vertice1, vertice2, chainHandler);
//                            generatePattern(vertice1, vertice2, chainHandler);
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
        timer.print("封闭模式数量： " + patterns.size());
    }


    private synchronized void generatePattern(Vertice v1, Vertice v2, ChainHandler chainHandler) {
        timer.start();
        for (int i = 1; i <= K; i++) {
            List<String[]> closedChains1 = chainHandler.findClosedKConnectivityChains(v1, i, kb1LinkedInstances);
            // 对于kb2，也就是规则头，只查询1连接就行
            List<String[]> closedChains2 = chainHandler.findClosedKConnectivityChains(v2, 1, kb2LinkedInstances);
            if (!ListUtil.isEmpty(closedChains1) && !ListUtil.isEmpty(closedChains2)) {
                for (String[] chain1 : closedChains1) {
                    for (String[] chain2 : closedChains2) {
                        String endV1 = chain1[chain1.length - 1];
                        String endV2 = chain2[chain2.length - 1];
                        Set<String> linkedInstance = kb1LinkToKb2.get(endV1);
                        if (linkedInstance.contains(endV2)) {
                            Pattern pattern = new Pattern(chain2, chain1);
                            logger.info(pattern.toString());
                            patterns.add(pattern);
                        }
                    }
                }
            }
        }
    }


    private synchronized void generateTypePattern(Vertice v1, Vertice v2, ChainHandler chainHandler) {
        List<String[]> typePatterns1 = chainHandler.findTypedChains(v1, conf.getLocalname(1));
        List<String[]> typePatterns2 = chainHandler.findTypedChains(v2, conf.getLocalname(2));
        for (String[] pattern1 : typePatterns1) {
            for (String[] pattern2 : typePatterns2) {
                Pattern pattern = new Pattern(pattern2, pattern1);
                logger.info(pattern.toString());
                typePatterns.add(pattern);
            }
        }
    }


    private synchronized String[] popPair() {
        System.out.println(linkedInstancePairs.size());
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
