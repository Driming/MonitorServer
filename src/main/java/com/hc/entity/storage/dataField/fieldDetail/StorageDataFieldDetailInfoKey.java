package com.hc.entity.storage.dataField.fieldDetail;

public class StorageDataFieldDetailInfoKey {
    private String id;

    private String collectname;

    private String type;

    private String field;

    private Short fieldtype;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field == null ? null : field.trim();
    }

    public Short getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(Short fieldtype) {
        this.fieldtype = fieldtype;
    }
}