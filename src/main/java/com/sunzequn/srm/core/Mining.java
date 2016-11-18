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
    private KConnectivityHandler kConnectivityHandler = new KConnectivityHandler();


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


    private void kConnectivitychain() {
        int num = 0;
        KConnectivityHandler kConnectivityHandler = new KConnectivityHandler();
        for (String[] pair : linkedInstancePairs) {

            Vertice vertice = kConnectivityHandler.kConnectivityPopulation(pair[0], 2, 1, conf, kb1LinkedInstances);
            System.out.println(vertice);
            return;
//            num++;
//            if (num % 100 == 0){
//                System.out.println(num);
//            }
        }
//        System.out.println(num);

    }

    private void printPartterns(List<String[]> chains) {
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
            confFile = "/home/sloriac/code/Semantic_Rules_Mining/src/main/resources/conf/input.properties";
        }
        Mining m = new Mining(confFile);
        m.kConnectivitychain();
    }

}
