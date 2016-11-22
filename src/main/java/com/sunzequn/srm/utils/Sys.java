package com.sunzequn.srm.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sloriac on 16-11-22.
 */
public class Sys {

    public static final String SPLIT = "{|\t|}";

    public static void main(String[] args) {
        String a = "http://www.w3.org/2002/07/owl#sameAs{|\t|}http://rdf.freebase.com/ns/m.076xsyg";
        String[] as = StringUtils.split(a, SPLIT);
        System.out.println(as.length);
        System.out.println(Arrays.toString(as));
    }
}
