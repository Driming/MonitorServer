package com.hc.entity.collection.task.status;

public class CollectionTaskTimePointStatus extends CollectionTaskTimePointStatusKey {
    private Long timecurrent;

    private String status;

    public Long getTimecurrent() {
        return timecurrent;
    }

    public void setTimecurrent(Long timecurrent) {
        this.timecurrent = timecurrent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}