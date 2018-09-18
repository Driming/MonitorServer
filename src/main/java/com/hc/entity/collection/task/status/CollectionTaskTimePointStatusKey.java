package com.hc.entity.collection.task.status;

public class CollectionTaskTimePointStatusKey {
    private String ctid;

    private Long timesave;

    private Long timedelay;

    public String getCtid() {
        return ctid;
    }

    public void setCtid(String ctid) {
        this.ctid = ctid == null ? null : ctid.trim();
    }

    public Long getTimesave() {
        return timesave;
    }

    public void setTimesave(Long timesave) {
        this.timesave = timesave;
    }

    public Long getTimedelay() {
        return timedelay;
    }

    public void setTimedelay(Long timedelay) {
        this.timedelay = timedelay;
    }
}