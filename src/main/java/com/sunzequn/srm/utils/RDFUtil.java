package com.sunzequn.srm.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sloriac on 16-11-17.
 */
public class RDFUtil {

    /**
     * @param chains 链的数组，s r o，检查o是不是实体
     * @return
     */
    public static List<String[]> chainsFilter(List<String[]> chains) {
        if (ListUtil.isEmpty(chains)) {
            return null;
        }
        List<String[]> res = new ArrayList<>();
        for (String[] chain : chains) {
            if (isChain(chain)) {
                res.add(chain);
            }
        }
        return ListUtil.filter(res);
    }

    private static boolean isChain(String[] chain) {
        return chain[chain.length - 1].toLowerCase().startsWith("http");
    }

    public static String[] parseTTLLine(String line) {
        line = StringUtils.removeEnd(line, ".").trim();
        String[] params = line.split(" ");
        if (params.length == 3) {
            return new String[]{removeBrackets(params[0]), removeBrackets(params[2])};
        }
        return null;
    }

    private static String removeBrackets(String uri) {
        uri = StringUtils.removeEnd(uri.trim(), ">");
        return StringUtils.removeStart(uri, "<").trim();
    }
}
