package com.getMyParking.entity.reports;

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 20/02/16.
 */
public class ParkingReportByDates {

    private Long count;
    private BigDecimal cost;
    private String type;
    private String special;
    private String eventType;
    private DateTime eventDate;
    private String operatorName;
    private Integer parkingSubLotId;
    private Integer parkingId;
    private Integer parkingLotId;
    private Integer companyId;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public DateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(DateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(Integer parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
