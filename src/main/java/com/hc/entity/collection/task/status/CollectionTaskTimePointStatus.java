package com.hc.entity.collection.task.status;

public class CollectionTaskTimePointStatus extends CollectionTaskTimePointStatusKey {
    private Long timeCurrent;

    private String status;

    public Long getTimeCurrent() {
        return timeCurrent;
    }

    public void setTimeCurrent(Long timeCurrent) {
        this.timeCurrent = timeCurrent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}