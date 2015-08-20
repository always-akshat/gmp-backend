package com.getMyParking.entity;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 21/08/15.
 */
public class ParkingReport {

    private Integer checkInCount;
    private Integer checkOutCount;
    private BigDecimal checkInRevenue;
    private BigDecimal checkOutRevenue;

    public ParkingReport(Integer checkInCount, Integer checkOutCount, BigDecimal checkInRevenue, BigDecimal checkOutRevenue) {
        this.checkInCount = checkInCount;
        this.checkOutCount = checkOutCount;
        this.checkInRevenue = checkInRevenue;
        this.checkOutRevenue = checkOutRevenue;
    }

    public Integer getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(Integer checkInCount) {
        this.checkInCount = checkInCount;
    }

    public Integer getCheckOutCount() {
        return checkOutCount;
    }

    public void setCheckOutCount(Integer checkOutCount) {
        this.checkOutCount = checkOutCount;
    }

    public BigDecimal getCheckInRevenue() {
        return checkInRevenue;
    }

    public void setCheckInRevenue(BigDecimal checkInRevenue) {
        this.checkInRevenue = checkInRevenue;
    }

    public BigDecimal getCheckOutRevenue() {
        return checkOutRevenue;
    }

    public void setCheckOutRevenue(BigDecimal checkOutRevenue) {
        this.checkOutRevenue = checkOutRevenue;
    }
}
