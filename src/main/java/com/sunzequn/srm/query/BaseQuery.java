package com.sunzequn.srm.query;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.log4j.Logger;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * Sparql查询
 */
public class BaseQuery {

    private Logger logger = Logger.getLogger(BaseQuery.class);

    /**
     * 通过HTTP去查询
     *
     * @param baseUrl 查询请求前缀
     * @param suffix  　查询请求后缀
     * @param sparql  　查询语句
     * @param timeout 　超时设置
     * @return 返回查询结果，如果没有则返回null
     */
    public ResultSet run(String baseUrl, String prefix, String suffix, String sparql, int timeout) {
        try {
            ParameterizedSparqlString pss = new ParameterizedSparqlString(sparql);
            URL url = new URL(baseUrl + prefix + URLEncoder.encode(pss.toString(), "UTF-8") + suffix);
            URLConnection connAPI = url.openConnection();
            connAPI.setConnectTimeout(timeout);
            connAPI.connect();
            ResultSet rs = ResultSetFactory.fromXML(connAPI.getInputStream());
            return rs.hasNext() ? rs : null;
        } catch (Exception e) {
//            logger.debug(e.getCause());
        }
        return null;
    }
}
