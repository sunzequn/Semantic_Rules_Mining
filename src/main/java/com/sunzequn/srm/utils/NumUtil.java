package com.sunzequn.srm.utils;

import java.text.DecimalFormat;

/**
 * Created by Sloriac on 2016/11/22.
 */
public class NumUtil {

    public static double m8(double d) {
        DecimalFormat df = new DecimalFormat("#.00000000");
        return Double.parseDouble(df.format(d));
    }

}
