package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 21/08/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingReport {

    private String type;
    private Integer parkingSubLotId;
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

    public Integer getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(Integer parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
