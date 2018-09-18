package com.hc.entity.storage.dataField;

public class StorageDataFieldInfoKey {
    private String id;

    private String collectname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCollectname() {
        return collectname;
    }

    public void setCollectname(String collectname) {
        this.collectname = collectname == null ? null : collectname.trim();
    }
}