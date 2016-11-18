package com.sunzequn.srm.utils;

import java.util.List;

/**
 * Created by sloriac on 16-11-17.
 */
public class ListUtil {

    public static <T> List<T> filter(List<T> ts) {
        return (ts != null && ts.size() > 0) ? ts : null;
    }

    public static <T> boolean isEmpty(List<T> l) {
        return l == null || l.size() == 0;
    }
}
