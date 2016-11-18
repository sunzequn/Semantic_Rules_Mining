package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.bean.Edge;
import com.sunzequn.srm.bean.Vertice;
import com.sunzequn.srm.query.KConnectivityQuery;
import com.sunzequn.srm.utils.ListUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * 求k连接链
 */
public class ChainHandler {

    private static final String TYPE_PROP = "";

    KConnectivityQuery query = new KConnectivityQuery();

    /**
     * 给定一个起始点和k，填充其所有的k连接链，形成一个树形结构，根节点是这个起始点。
     * 这是一个递归的过程
     *
     * @param uri  起始点的uri
     * @param k    k链接的k值
     * @param kb   　要查询知识库的标识
     * @param conf 　查询配置
     * @return
     */
    public Vertice kConnectivityPopulation(String uri, int k, int kb, Conf conf) {
        //　递归终止判断
        Vertice vertice = new Vertice(uri);
        if (k == 0) return vertice;
        List<String[]> chains = query.query1Connectivity(conf, kb, uri);
        if (ListUtil.isEmpty(chains)) return vertice;
        for (String[] chain : chains) {
            Edge edge = new Edge(chain[0]);
            Vertice subV = kConnectivityPopulation(chain[1], k - 1, kb, conf);
            edge.setVertice(subV);
            vertice.addEdge(edge);
        }
        return vertice;
    }

    public List<String[]> findKConnectivityChains(Vertice vertice, int k, String[] preChain) {
        if (preChain == null) preChain = new String[]{vertice.getUri()};
        List<String[]> kConnectivityChains = new ArrayList<>();
        List<Edge> edges = vertice.getEdges();
        if (k == 0 || edges.size() == 0) {
            kConnectivityChains.add(preChain);
            return kConnectivityChains;
        }

        for (Edge edge : edges) {
            String[] chain = new String[]{edge.getRel(), edge.getVertice().getUri()};
            chain = ArrayUtils.addAll(preChain, chain);
            List<String[]> postChains = findKConnectivityChains(edge.getVertice(), k - 1, chain);
            if (!ListUtil.isEmpty(postChains)) {
                kConnectivityChains.addAll(postChains);
            }
        }
        return kConnectivityChains;
    }

    public List<String[]> closeChains(List<String[]> chains, Set<String> kbLinkedInstances) {
        if (ListUtil.isEmpty(chains)) return null;
        List<String[]> closedChains = new ArrayList<>();
        for (String[] chain : chains) {
            if (kbLinkedInstances.contains(chain[chain.length - 1])) closedChains.add(chain);
        }
        return closedChains;
    }


}
