package com.getMyParking.entity.reports;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rahulgupta.s on 10/09/15.
 */
public class ParkingReportByQuery {

    private Long count;
    private BigDecimal cost;
    private String type;
    private String special;
    private String eventType;

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
}
