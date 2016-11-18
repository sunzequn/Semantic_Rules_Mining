package com.sunzequn.srm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sloriac on 16-11-17.
 */
public class Vertice {

    private String uri;
    private List<Edge> edges = new ArrayList<>();

    public Vertice(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public Vertice(String uri, List<Edge> edges) {
        this.uri = uri;
        this.edges = edges;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public boolean isHasNext() {
        return edges.size() > 0;

    }

    @Override
    public String toString() {
        return "Vertice{" +
                "uri='" + uri + '\'' +
                ", edges=" + edges +
                '}';
    }
}
