package com.sunzequn.srm.query;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.utils.RDFUtil;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            // 过滤掉非本知识库的查询，本来是在RDFUtil.spvFilter这个时候过滤的
            // 但是考虑到后面可能有用，就保留非本知识库的数据，只是不查询而已
            if (!uri.startsWith(conf.getInstanceNS(kb))) return null;
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
                Set<String> pvs = new HashSet<>();
                while (rs.hasNext()) {
                    QuerySolution qs = rs.nextSolution();
                    RDFNode p = qs.get("p");
                    RDFNode v = qs.get("v");
                    // pv判重，我们的GeoNames有问题，type defined有两个或三个，好像是因为之前导入我爬取的其他数据的原因
                    // virtuoso不会主动判重
                    if (!pvs.contains(p.toString() + v.toString())) {
                        String[] spv = new String[]{p.toString(), v.toString()};
                        spvs.add(spv);
                        pvs.add(p.toString() + v.toString());
                    }

                }
                //　过滤掉literals和非该kb的实体
                return RDFUtil.spvFilter(spvs);
            }
        } catch (Exception e) {
            e.getCause();
        }
        return null;
    }

}
