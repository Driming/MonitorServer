package com.hc.entity.collection.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.server.CollectionServer;

import java.util.concurrent.ConcurrentHashMap;

@JsonInclude(Include.NON_NULL)
public class CollectionTask {
    private String ctid;
    private String csid;
    private String name;
    private String config;
    private String driverName;
    private String driverVersion;
    private String type;
    private String dispatchConfig;
    private String status;
    private String statusReason;
    private long lastExecuteTime;
    private long totalDataSourceNum;
    private long totalDataResultNum;
    private double totalDataResultSize;
    private double totalDataSourceSize;
    private String label;
    private short isUsed;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public short getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(short isUsed) {
        this.isUsed = isUsed;
    }

    public String getCtid() {
        return ctid;
    }

    public void setCtid(String ctid) {
        this.ctid = ctid;
    }

    public String getCsid() {
        return csid;
    }

    public void setCsid(String csid) {
        this.csid = csid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDispatchConfig() {
        return dispatchConfig;
    }

    public void setDispatchConfig(String dispatchConfig) {
        this.dispatchConfig = dispatchConfig;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public long getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(long lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public long getTotalDataSourceNum() {
        return totalDataSourceNum;
    }

    public void setTotalDataSourceNum(long totalDataSourceNum) {
        this.totalDataSourceNum = totalDataSourceNum;
    }

    public double getTotalDataResultSize() {
        return totalDataResultSize;
    }

    public void setTotalDataResultSize(double totalDataResultSize) {
        this.totalDataResultSize = totalDataResultSize;
    }

    public long getTotalDataResultNum() {
        return totalDataResultNum;
    }

    public void setTotalDataResultNum(long totalDataResultNum) {
        this.totalDataResultNum = totalDataResultNum;
    }

    public double getTotalDataSourceSize() {
        return totalDataSourceSize;
    }

    public void setTotalDataSourceSize(double totalDataSourceSize) {
        this.totalDataSourceSize = totalDataSourceSize;
    }
}
