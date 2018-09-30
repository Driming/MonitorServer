package com.hc.entity.collection.task.status;

public class CollectionTaskTimePointStatusKey {
    private String ctid;

    private Long timeSave;

    private Long timeDelay;

    public String getCtid() {
        return ctid;
    }

    public void setCtid(String ctid) {
        this.ctid = ctid == null ? null : ctid.trim();
    }

    public Long getTimeSave() { return timeSave; }

    public void setTimeSave(Long timeSave) { this.timeSave = timeSave; }

    public Long getTimeDelay() { return timeDelay; }

    public void setTimeDelay(Long timeDelay) { this.timeDelay = timeDelay; }
}