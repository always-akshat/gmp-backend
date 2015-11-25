package com.getMyParking.dto;

import org.joda.time.DateTime;

/**
 * Created by rahulgupta.s on 25/11/15.
 */
public class ActiveSessions {

    private String username;
    private String name;
    private DateTime lastTransactionTime;
    private DateTime lastAccessTime;
    private String deviceId;
    private String parkingLotName;
    private Integer transactionCount;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getLastTransactionTime() {
        return lastTransactionTime;
    }

    public void setLastTransactionTime(DateTime lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }

    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }
}
