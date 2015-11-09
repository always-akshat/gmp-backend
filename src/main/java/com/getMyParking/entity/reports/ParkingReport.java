package com.getMyParking.entity.reports;

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
    private Integer focCount;
    private Integer ttCount;
    private Integer passCheckInCount;
    private Integer passCheckOutCount;
    private BigDecimal checkInRevenue;
    private BigDecimal checkOutRevenue;
    private Integer acCount;

    public ParkingReport(Integer checkInCount, Integer checkOutCount, Integer focCount, Integer ttCount, Integer passCheckInCount,
                         Integer passCheckOutCount, BigDecimal checkInRevenue, BigDecimal checkOutRevenue, int acCount) {
        this.checkInCount = checkInCount;
        this.checkOutCount = checkOutCount;
        this.focCount = focCount;
        this.ttCount = ttCount;
        this.checkInRevenue = checkInRevenue;
        this.checkOutRevenue = checkOutRevenue;
        this.passCheckInCount = passCheckInCount;
        this.passCheckOutCount = passCheckOutCount;
        this.acCount = acCount;
    }

    public Integer getAcCount() {
        return acCount;
    }

    public void setAcCount(Integer acCount) {
        this.acCount = acCount;
    }

    public Integer getPassCheckInCount() {
        return passCheckInCount;
    }

    public void setPassCheckInCount(Integer passCheckInCount) {
        this.passCheckInCount = passCheckInCount;
    }

    public Integer getPassCheckOutCount() {
        return passCheckOutCount;
    }

    public void setPassCheckOutCount(Integer passCheckOutCount) {
        this.passCheckOutCount = passCheckOutCount;
    }

    public Integer getFocCount() {
        return focCount;
    }

    public void setFocCount(Integer focCount) {
        this.focCount = focCount;
    }

    public Integer getTtCount() {
        return ttCount;
    }

    public void setTtCount(Integer ttCount) {
        this.ttCount = ttCount;
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
