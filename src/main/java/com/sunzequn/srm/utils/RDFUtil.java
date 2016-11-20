package com.sunzequn.srm.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sloriac on 16-11-17.
 */
public class RDFUtil {

    /**
     * @param spvs 链的数组，s r v，检查v是不是实体
     * @return
     */
    public static List<String[]> spvFilter(List<String[]> spvs) {
        if (ListUtil.isEmpty(spvs)) {
            return null;
        }
        List<String[]> res = new ArrayList<>();
        for (String[] spv : spvs) {
            if (isSRO(spv)) {
                res.add(spv);
            }
        }
        return ListUtil.filter(res);
    }

    private static boolean isSRO(String[] spv) {
        return spv[spv.length - 1].toLowerCase().startsWith("http://");
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
