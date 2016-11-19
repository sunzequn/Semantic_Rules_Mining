package com.sunzequn.srm.query;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.utils.RDFUtil;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * 查询k-connectivity模式
 */
public class KConnectivityQuery extends BaseQuery {

    /**
     * 查询1-connectivity模式
     *
     * @param conf
     * @param kb
     * @param uri  起始实体
     * @return 返回一个三元组s, r, o，s是起始实体，o是结束实体，r是它们的关系
     */
    public List<String[]> query1Connectivity(Conf conf, int kb, String uri) {
        try {
            String sparql;
            String graph = conf.getGraph(kb);
            if (graph.equals("None")) {
                sparql = "select * {<" + uri + "> ?p ?v}";
            } else {
                sparql = "select * from <" + graph + "> where {<" + uri + "> ?p ?v}";
            }
//            System.out.println(sparql);
            ResultSet rs = run(conf.getEndpoint(kb), conf.getPrefix(), conf.getSuffix(), sparql, conf.getTimeout());
            if (rs != null) {
                List<String[]> spvs = new ArrayList<>();
                while (rs.hasNext()) {
                    QuerySolution qs = rs.nextSolution();
                    RDFNode p = qs.get("p");
                    RDFNode v = qs.get("v");
                    String[] spv = new String[]{p.toString(), v.toString()};
                    spvs.add(spv);
                }
                //　过滤掉literals和非该kb的实体
                return RDFUtil.spvFilter(spvs, conf.getLocalname(kb));
            }
        } catch (Exception e) {
            e.getCause();
        }
        return null;
    }

}
