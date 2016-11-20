package com.sunzequn.srm.bean;

import java.util.Arrays;

/**
 * Created by Sloriac on 2016/11/19.
 * <p>
 * 环模式的实例，也就是包含实体和谓词的模式
 */
public class PatternInstance {

    private String[] head;
    private String[] body;

    public PatternInstance(String[] head, String[] body) {
        this.head = head;
        this.body = body;
    }

    public String[] getHead() {
        return head;
    }

    public void setHead(String[] head) {
        this.head = head;
    }

    public String[] getBody() {
        return body;
    }

    public void setBody(String[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "PatternInstance{" +
                "head=" + Arrays.toString(head) +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
