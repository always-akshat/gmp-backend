package com.getMyParking.entity.reports;

import org.hibernate.criterion.Projections;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 03/11/15.
 */
public class SubLotReportDetails {

    private Long count;
    private BigDecimal revenue;
    private String eventType;
    private String subLotType;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSubLotType() {
        return subLotType;
    }

    public void setSubLotType(String subLotType) {
        this.subLotType = subLotType;
    }
}
