package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.bean.Edge;
import com.sunzequn.srm.bean.Vertice;
import com.sunzequn.srm.query.KConnectivityQuery;
import com.sunzequn.srm.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * 求k连接链
 */
public class KConnectivityHandler {

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
    public Vertice kConnectivityPopulation(String uri, int k, int kb, Conf conf, Set<String> kbLinkedInstances) {
        //　递归终止判断
        Vertice vertice = new Vertice(uri);
        if (kbLinkedInstances.contains(uri)) vertice.setClosed(true);
        if (k == 0) return vertice;
        List<String[]> chains = query.query1Connectivity(conf, kb, uri);
        if (ListUtil.isEmpty(chains)) return vertice;
        for (String[] chain : chains) {
            Edge edge = new Edge(chain[0]);
            Vertice subV = kConnectivityPopulation(chain[1], k - 1, kb, conf, kbLinkedInstances);
            edge.setVertice(subV);
            vertice.addEdge(edge);
        }
        return vertice;
    }

    public List<Edge> findKConnectivity(Vertice vertice, int k, String[] prechain) {
        if (prechain == null) {
            prechain = new String[]{vertice.getUri()};
        }
        List<String[]> kConnectivitychains = new ArrayList<>();
        return null;
    }


}
