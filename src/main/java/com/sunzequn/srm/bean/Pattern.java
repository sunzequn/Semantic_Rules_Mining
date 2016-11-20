package com.sunzequn.srm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sloriac on 2016/11/20.
 *
 * 环模式，和环模式实例不同，环模式只包含谓词，也就是我们最后规则的候选集
 * 环模式是从环模式实例中抽取的
 */
public class Pattern {

    private String head;
    private String[] body;
    private List<PatternInstance> patternInstances = new ArrayList<>();

    public Pattern(String head, String[] body) {
        this.head = head;
        this.body = body;
    }

    public void addPatternInstance(PatternInstance patternInstance) {
        patternInstances.add(patternInstance);
    }

    public int getInstancesNum() {
        return patternInstances.size();
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String[] getBody() {
        return body;
    }

    public void setBody(String[] body) {
        this.body = body;
    }

    public List<PatternInstance> getPatternInstances() {
        return patternInstances;
    }

    public void setPatternInstances(List<PatternInstance> patternInstances) {
        this.patternInstances = patternInstances;
    }
}
