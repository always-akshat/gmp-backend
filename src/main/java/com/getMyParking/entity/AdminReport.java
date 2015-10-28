package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by karan on 23/10/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminReport {

    private String name;
    private BigInteger checkInCount;
    private BigInteger checkOutCount;
    private BigDecimal checkInRevenue;
    private BigDecimal checkOutRevenue;
    private String lastCheckInTime;

    public AdminReport(String name, BigInteger checkInCount, BigInteger checkOutCount, BigDecimal checkInRevenue, BigDecimal checkOutRevenue) {
        this.name = name;
        this.checkInCount = checkInCount;
        this.checkOutCount = checkOutCount;
        this.checkInRevenue = checkInRevenue;
        this.checkOutRevenue = checkOutRevenue;
    }

    public String getName() {
        return name;
    }

    public BigInteger getCheckInCount() {
        return checkInCount;
    }

    public BigInteger getCheckOutCount() {
        return checkOutCount;
    }

    public BigDecimal getCheckInRevenue() {
        return checkInRevenue;
    }

    public BigDecimal getCheckOutRevenue() {
        return checkOutRevenue;
    }

    public String getLastCheckInTime() {
        return lastCheckInTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckInCount(BigInteger checkInCount) {
        this.checkInCount = checkInCount;
    }

    public void setCheckOutCount(BigInteger checkOutCount) {
        this.checkOutCount = checkOutCount;
    }

    public void setCheckInRevenue(BigDecimal checkInRevenue) {
        this.checkInRevenue = checkInRevenue;
    }

    public void setCheckOutRevenue(BigDecimal checkOutRevenue) {
        this.checkOutRevenue = checkOutRevenue;
    }

    public void setLastCheckInTime(String lastCheckInTime) {
        this.lastCheckInTime = lastCheckInTime;
    }
}
