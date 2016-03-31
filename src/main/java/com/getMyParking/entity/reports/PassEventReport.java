package com.getMyParking.entity.reports;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 21/11/15.
 */
public class PassEventReport {

    private Integer parkingPassMasterId;
    private BigDecimal collectedAmount;
    private DateTime eventDate;

    public Integer getParkingPassMasterId() {
        return parkingPassMasterId;
    }

    public void setParkingPassMasterId(Integer parkingPassMasterId) {
        this.parkingPassMasterId = parkingPassMasterId;
    }

    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(BigDecimal collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public DateTime getDateTime() {
        return eventDate;
    }

    public void setDateTime(DateTime eventDate) {
        this.eventDate = eventDate;
    }
}
