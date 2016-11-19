package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.bean.Vertice;

import java.util.*;

/**
 * Created by Sloriac on 2016/11/19.
 * <p>
 * 改为多线程
 */
public class VerticePopulationHandler {

    private int n = 10;
    private LinkedList<String[]> linkedInstnacePairs;
    private Conf conf;
    private int K;
    private List<Vertice[]> linkedVertices = new ArrayList<>();

    public VerticePopulationHandler(List<String[]> linkedInstnacePairs, int K, int n, Conf conf) {
        this.linkedInstnacePairs = new LinkedList<>();
        this.linkedInstnacePairs.addAll(linkedInstnacePairs);
        this.n = n;
        this.K = K;
        this.conf = conf;
    }

    public List<Vertice[]> run() {
        Vector<Thread> threads = new Vector<Thread>();
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(() -> {
                ChainHandler chainHandler = new ChainHandler();
                while (true) {
                    try {
                        String[] pair = popPair();
                        if (pair == null) return;
                        Thread.sleep((long) (Math.random() * 100));
                        System.out.println(Arrays.toString(pair));
                        Vertice vertice1 = chainHandler.kConnectivityPopulation(pair[0], K, 1, conf);
                        // 对于kb2，也就是规则头，只查询1连接就行
                        Vertice vertice2 = chainHandler.kConnectivityPopulation(pair[1], 1, 2, conf);
                        if (vertice1.isHasNext() && vertice2.isHasNext()) {
                            addVertice(new Vertice[]{vertice1, vertice2});
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
        return linkedVertices;
    }

    private synchronized String[] popPair() {
        return linkedInstnacePairs.size() > 0 ? linkedInstnacePairs.pop() : null;
    }

    private synchronized void addVertice(Vertice[] vertices) {
        linkedVertices.add(vertices);
    }
}
