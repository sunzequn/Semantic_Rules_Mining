package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.bean.Vertice;
import com.sunzequn.srm.utils.RDFUtil;
import com.sunzequn.srm.utils.ReadUtil;
import com.sunzequn.srm.utils.TimeUtil;

import java.util.*;

/**
 * Created by sloriac on 16-11-17.
 */
public class Mining {

    private int k = 2;
    private Conf conf;
    private List<String[]> linkedInstancePairs = new ArrayList<>();
    private List<Vertice[]> linkedVertices = new ArrayList<>();
    private ChainHandler chainHandler = new ChainHandler();

    private Set<String> kb1LinkedInstances = new HashSet<>();
    private Set<String> kb2LinkedInstances = new HashSet<>();

    private Map<String, Set<String>> kb1LinkToKb2 = new HashMap<>();
    private Map<String, Set<String>> kb2LinkToKb1 = new HashMap<>();

    public Mining(String confFile) {
        conf = new Conf(confFile);
    }

    private void run() {
        readLinkedInstances();
        generateVertices();
        generatePattern(1);
    }

    private void readLinkedInstances() {
        TimeUtil.start();
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
        TimeUtil.print("链接实例数量： " + linkedInstancePairs.size());
    }

    private void generateVertices() {
        TimeUtil.start();
        for (String[] pair : linkedInstancePairs) {
            Vertice vertice1 = chainHandler.kConnectivityPopulation(pair[0], k, 1, conf);
            Vertice vertice2 = chainHandler.kConnectivityPopulation(pair[1], k, 2, conf);
            if (vertice1.isHasNext() && vertice2.isHasNext()) {
                linkedVertices.add(new Vertice[]{vertice1, vertice2});
            }
        }
        TimeUtil.print("链接结点数量： " + linkedVertices.size());
    }

    private void generatePattern(int k) {

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

    private void kConnectivityChain() {
        ChainHandler chainHandler = new ChainHandler();
        for (String[] pair : linkedInstancePairs) {
            Vertice vertice = chainHandler.kConnectivityPopulation(pair[0], 2, 1, conf);
            List<String[]> closedChains = chainHandler.findClosedKConnectivityChains(vertice, 2, kb1LinkedInstances);
            System.out.println(closedChains.size());
            List<String[]> typeChains = chainHandler.findTypedChains(vertice);
            System.out.println(typeChains.size());
            printChains(typeChains);
            return;
        }
    }

    private void printChains(List<String[]> chains) {
        for (String[] chain : chains) {
            System.out.println(chain.length);
            for (String achain : chain) {
                System.out.println(achain + " ");
            }
            System.out.println("----");
        }
    }


    public static void main(String[] args) {
        String confFile;
        if (args.length > 0) {
            confFile = args[0];
        } else {
            confFile = "target/classes/conf/input.properties";
        }
        Mining m = new Mining(confFile);
        m.run();
    }

}
