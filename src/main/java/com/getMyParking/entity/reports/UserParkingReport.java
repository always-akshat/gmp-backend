package com.getMyParking.entity.reports;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 03/11/15.
 */
public class UserParkingReport {

    private Integer parkingSubLotId;
    private Integer checkInCount;
    private Integer checkOutCount;
    private BigDecimal checkInRevenue;
    private BigDecimal checkOutRevenue;

    public Integer getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(Integer parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
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
