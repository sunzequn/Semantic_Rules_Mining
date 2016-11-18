package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.bean.Vertice;
import com.sunzequn.srm.utils.RDFUtil;
import com.sunzequn.srm.utils.ReadUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by sloriac on 16-11-17.
 */
public class Mining {

    private Conf conf;
    private List<String[]> linkedInstancePairs = new ArrayList<>();
    private Set<String> kb1LinkedInstances = new HashSet<>();
    private Set<String> kb2LinkedInstances = new HashSet<>();
    private ChainHandler chainHandler = new ChainHandler();


    public Mining(String confFile) {
        conf = new Conf(confFile);
        ReadUtil readUtil = new ReadUtil(conf.getLinkedInstance());
        List<String> lines = readUtil.readByLine();
        for (String line : lines) {
            String[] li = RDFUtil.parseTTLLine(line);
            linkedInstancePairs.add(li);
            assert li != null;
            kb1LinkedInstances.add(li[0]);
            kb2LinkedInstances.add(li[1]);
        }
        System.out.println(linkedInstancePairs.size());

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
        m.kConnectivityChain();
    }

}
