package com.sunzequn.srm.bean;

import com.sunzequn.srm.utils.PropertiesUtil;

/**
 * Created by sloriac on 16-11-17.
 * <p>
 * 系统的输入参数
 */

public class Conf {

    private String ontology1;
    private String endpoint1;
    private String ontology2;
    private String endpoint2;
    private String graph1;
    private String graph2;
    private String localname1;
    private String localname2;
    private String prefix;
    private String suffix;
    private int timeout;

    private String linkedInstance;

    public Conf(String confProperties) {
        PropertiesUtil propertiesUtil = new PropertiesUtil(confProperties);
        ontology1 = propertiesUtil.getValue("ontology1");
        ontology2 = propertiesUtil.getValue("ontology2");
        endpoint1 = propertiesUtil.getValue("endpoint1");
        endpoint2 = propertiesUtil.getValue("endpoint2");
        prefix = propertiesUtil.getValue("prefix");
        suffix = propertiesUtil.getValue("suffix");
        graph1 = propertiesUtil.getValue("graph1");
        graph2 = propertiesUtil.getValue("graph2");
        localname1 = propertiesUtil.getValue("localname1");
        localname2 = propertiesUtil.getValue("localname2");
        timeout = Integer.parseInt(propertiesUtil.getValue("timeout"));
        linkedInstance = propertiesUtil.getValue("linkedInstance");
        if (ontology1 == null || ontology2 == null || endpoint1 == null || endpoint2 == null || linkedInstance == null) {
            System.out.println("配置文件错误");
        }
    }

    public String getOntology(int kb) {
        if (kb == 1)
            return ontology1;
        else
            return ontology2;
    }

    public String getEndpoint(int kb) {
        if (kb == 1)
            return endpoint1;
        else
            return endpoint2;
    }

    public String getGraph(int kb) {
        if (kb == 1)
            return graph1;
        else
            return graph2;
    }

    public String getLocalname(int kb) {
        if (kb == 1)
            return localname1;
        else
            return localname2;
    }

    public String getLinkedInstance() {
        return linkedInstance;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getTimeout() {
        return timeout;
    }


    @Override
    public String toString() {
        return "Conf{" +
                "ontology1='" + ontology1 + '\'' +
                ", endpoint1='" + endpoint1 + '\'' +
                ", ontology2='" + ontology2 + '\'' +
                ", endpoint2='" + endpoint2 + '\'' +
                ", graph1='" + graph1 + '\'' +
                ", graph2='" + graph2 + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", timeout=" + timeout +
                ", linkedInstance='" + linkedInstance + '\'' +
                '}';
    }
}
