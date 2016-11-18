package com.sunzequn.srm.utils;

import java.util.Date;

/**
 * Created by Sloriac on 2016/11/18.
 */
public class TimeUtil {

    private static long startTime;
    private static long endTime;

    private TimeUtil() {
    }

    public static void start() {
        startTime = new Date().getTime();
    }

    public static void end() {
        endTime = new Date().getTime();
    }

    private static long duration() {
        end();
        return endTime - startTime;
    }

    public static void print() {
        duration();
        System.out.println("cost: " + duration() + " milliseconds");
    }

    public static void print(String message) {
        System.out.println(message + "， 耗时: " + duration() + " milliseconds");
    }
}
