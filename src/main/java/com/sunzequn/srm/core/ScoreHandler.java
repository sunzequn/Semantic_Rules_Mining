package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Pattern;
import com.sunzequn.srm.utils.NumUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sloriac on 2016/11/22.
 * <p>
 * 给规则打分
 */
public class ScoreHandler {

    private static final double PROB_HEAD＿IN_BODY = 0.7;

    /**
     * 给规则打分，并且输出符合要求的规则
     *
     * @param rawPatterns
     * @return
     */
    public List<Pattern> score(List<Pattern> rawPatterns) {
        for (Pattern rawPattern : rawPatterns) {
            int[] nums = getNum(rawPattern, rawPatterns);
            // 都保留6位小数
            rawPattern.setProbOfHeadInBody(NumUtil.m8((double) rawPattern.getInstancesNum() / nums[2]));
            rawPattern.setProbOfHead(NumUtil.m8((double) nums[1] / nums[0]));
        }
        return rawPatterns;
    }

    private int[] getNum(Pattern pattern, List<Pattern> rawPatterns) {
        int headNum = 0;
        int bodyNum = 0;
        int totalNum = 0;
        for (Pattern rawPattern : rawPatterns) {
            if (rawPattern.getHead().equals(pattern.getHead())) {
                headNum += rawPattern.getInstancesNum();
            }
            if (Arrays.equals(rawPattern.getBody(), pattern.getBody())) {
                bodyNum += rawPattern.getInstancesNum();
            }
            totalNum += rawPattern.getInstancesNum();
        }
        return new int[]{totalNum, headNum, bodyNum};
    }

}
