package com.sunzequn.srm.bean;

/**
 * Created by sloriac on 16-11-17.
 */
public class Edge {

    private String rel;
    private Vertice vertice;

    public Edge(String rel) {
        this.rel = rel;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public Vertice getVertice() {
        return vertice;
    }

    public void setVertice(Vertice vertice) {
        this.vertice = vertice;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "rel='" + rel + '\'' +
                ", vertice=" + vertice +
                '}';
    }
}
