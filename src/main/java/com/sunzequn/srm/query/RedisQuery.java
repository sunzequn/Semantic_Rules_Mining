package com.sunzequn.srm.query;

import com.sunzequn.srm.utils.ListUtil;
import com.sunzequn.srm.utils.Sys;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sloriac on 16-11-22.
 */
public class RedisQuery {

    private Logger logger = Logger.getLogger(RedisQuery.class);
    private Jedis jedis = new Jedis("localhost");

    public RedisQuery() {
        logger.debug(jedis.ping());
    }

    public void addSet(String key, List<String[]> pvs) {
        for (String[] pv : pvs) {
            jedis.sadd(key, pv[0] + Sys.SPLIT + pv[1]);
        }
    }

    public List<String> getSet(String key) {
        Set<String> res = jedis.smembers(key);
        return ListUtil.filter(new ArrayList<>(res));
    }

}
