package com.hc.entity.collection.application.running;

public class CollectionApplicationRunningKey {
    private Integer pid;

    private String csid;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getCsid() {
        return csid;
    }

    public void setCsid(String csid) {
        this.csid = csid == null ? null : csid.trim();
    }
}