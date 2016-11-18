package com.sunzequn.srm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sloriac on 16-11-17.
 */
public class Vertice {

    private String uri;
    private boolean isClosed = false;
    private List<Edge> edges = new ArrayList<>();

    public Vertice(String uri) {
        this.uri = uri;
    }

    public Vertice(String uri, boolean isClosed, List<Edge> edges) {
        this.uri = uri;
        this.isClosed = isClosed;
        this.edges = edges;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
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
                ", isClosed=" + isClosed +
                ", edges=" + edges +
                '}';
    }
}
