package com.getMyParking.entity.reports;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 21/11/15.
 */
public class PassReport {

    private Integer parkingPassMasterId;
    private Long activePassCount;
    private Long paidPassCount;
    private BigDecimal collectedAmount;
    private BigDecimal balanceAmount;

    public Integer getParkingPassMasterId() {
        return parkingPassMasterId;
    }

    public void setParkingPassMasterId(Integer parkingPassMasterId) {
        this.parkingPassMasterId = parkingPassMasterId;
    }

    public Long getActivePassCount() {
        return activePassCount;
    }

    public void setActivePassCount(Long activePassCount) {
        this.activePassCount = activePassCount;
    }

    public Long getPaidPassCount() {
        return paidPassCount;
    }

    public void setPaidPassCount(Long paidPassCount) {
        this.paidPassCount = paidPassCount;
    }

    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(BigDecimal collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }
}
