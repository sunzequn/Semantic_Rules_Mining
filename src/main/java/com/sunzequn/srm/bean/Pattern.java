package com.sunzequn.srm.bean;

import java.util.Arrays;

/**
 * Created by Sloriac on 2016/11/19.
 */
public class Pattern {

    private String[] head;
    private String[] body;

    public Pattern(String[] head, String[] body) {
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
        return "Pattern{" +
                "head=" + Arrays.toString(head) +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
