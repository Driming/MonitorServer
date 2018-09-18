package com.hc.entity.storage.dataField.indexDetail;

public class StorageDataIndexDetailInfoKey {
    private String id;

    private String collectname;

    private String field;

    private String elements;

    private String values;

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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field == null ? null : field.trim();
    }

    public String getElements() {
        return elements;
    }

    public void setElements(String elements) {
        this.elements = elements == null ? null : elements.trim();
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values == null ? null : values.trim();
    }

    public Short getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(Short fieldtype) {
        this.fieldtype = fieldtype;
    }
}