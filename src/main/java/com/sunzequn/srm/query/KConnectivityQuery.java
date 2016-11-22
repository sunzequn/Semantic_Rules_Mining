package com.sunzequn.srm.query;

import com.sunzequn.srm.bean.Conf;
import com.sunzequn.srm.utils.ListUtil;
import com.sunzequn.srm.utils.RDFUtil;
import com.sunzequn.srm.utils.Sys;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * 查询k-connectivity模式
 */
public class KConnectivityQuery extends BaseQuery {

    private RedisQuery redisQuery = new RedisQuery();
    private Logger logger = Logger.getLogger(KConnectivityQuery.class);

    /**
     * 查询1-connectivity模式
     *
     * @param conf
     * @param kb
     * @param uri  起始实体
     * @return 返回一个二元组(r, o)，o是结束实体，r是它们的关系
     */
    public List<String[]> query1Connectivity(Conf conf, int kb, String uri) {
        try {
            // 过滤掉非本知识库的查询，本来是在RDFUtil.spvFilter这个时候过滤的
            // 但是考虑到后面可能有用，就保留非本知识库的数据，只是不查询而已
            if (!uri.startsWith(conf.getInstanceNS(kb))) return null;
            List<String[]> spvs = new ArrayList<>();
            //先查询redis
            List<String> redisPvs = redisQuery.getSet(uri);
            // redis命中
            if (!ListUtil.isEmpty(redisPvs)) {
//                logger.info("redis 命中：" + uri);
                for (String redisPv : redisPvs) {
                    String[] pv = StringUtils.split(redisPv, Sys.SPLIT);
                    if (pv.length == 2) {
                        spvs.add(pv);
                    } else {
                        //有乱七八糟的字符
//                        logger.error("pv解析错误：" + redisPv);
                    }
                }
                //　过滤掉literals和非该kb的实体
                return RDFUtil.spvFilter(spvs);
            }
//            logger.info("redis未命中：" + uri);
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
                //保存到redis
                redisQuery.addSet(uri, spvs);
                //　过滤掉literals和非该kb的实体
                return RDFUtil.spvFilter(spvs);
            }
        } catch (Exception e) {
            e.getCause();
        }
        return null;
    }

}
