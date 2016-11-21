package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Pattern;
import com.sunzequn.srm.bean.PatternInstance;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by Sloriac on 2016/11/20.
 */
public class PatternGenerator {

    public void generateTypePatterns(Map<String, Pattern> patternMap, PatternInstance patternInstance) {
        String headType = patternInstance.getHead()[0];
        String bodyType = patternInstance.getBody()[0];
        add2TypePatternMap(patternMap, headType, bodyType, patternInstance);
    }

    public void generatePatterns(Map<String, Pattern> patternMap, PatternInstance patternInstance) {
        String head = patternInstance.getHead()[1];
        String[] body = patternInstance.getBody();
        add2PatternMap(patternMap, head, body, patternInstance);
    }

    private void add2TypePatternMap(Map<String, Pattern> patternMap, String head, String body, PatternInstance patternInstance) {
        String key = head + " <- " + body;
        if (patternMap.containsKey(key)) {
            patternMap.get(key).addPatternInstance(patternInstance);
        } else {
            Pattern pattern = new Pattern(head, new String[]{body});
            pattern.addPatternInstance(patternInstance);
            patternMap.put(key, pattern);
        }
    }

    private void add2PatternMap(Map<String, Pattern> patternMap, String head, String[] body, PatternInstance patternInstance) {
        String[] bodyRelations = getRelations(body);
        String key = head + " <- " + Arrays.toString(bodyRelations);
        if (patternMap.containsKey(key)) {
            patternMap.get(key).addPatternInstance(patternInstance);
        } else {
            Pattern pattern = new Pattern(head, bodyRelations);
            pattern.addPatternInstance(patternInstance);
            patternMap.put(key, pattern);
        }
    }

    private String[] getRelations(String[] body) {
        int n = (body.length - 1) / 2;
        String[] relations = new String[n];
        for (int i = 0; i < n; i++) {
            relations[i] = body[i * 2 + 1];
        }
        return relations;
    }
}
