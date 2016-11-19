package com.sunzequn.srm.utils;

import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by Sloriac on 2016/11/18.
 */
public class TimeUtil {

    private long startTime;
    private long endTime;
    private Logger logger;

    public TimeUtil(Class clazz) {
        logger = Logger.getLogger(clazz);
    }

    public void start() {
        startTime = new Date().getTime();
    }

    private void end() {
        endTime = new Date().getTime();
    }

    private long duration() {
        end();
        return endTime - startTime;
    }

    public void print() {
        duration();
        logger.info("耗时: " + duration() + " milliseconds");
    }

    public void print(String message) {
        logger.info(message + "， 耗时: " + duration() + " milliseconds");
    }

}
