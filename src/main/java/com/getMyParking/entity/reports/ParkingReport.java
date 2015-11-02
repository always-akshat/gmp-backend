package com.getMyParking.entity.reports;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rahulgupta.s on 21/08/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingReport {

    private String type;
    private Integer checkInCount;
    private Integer checkOutCount;
    private BigDecimal checkInRevenue;
    private BigDecimal checkOutRevenue;
    private List<ReportDetails> reportDetails;

    public ParkingReport(List<ReportDetails> reportDetails) {
        this.reportDetails =  reportDetails;
        checkInCount = 0;
        checkOutCount = 0;
        checkInRevenue = BigDecimal.ZERO;
        checkOutRevenue = BigDecimal.ZERO;
        for (ReportDetails details : reportDetails) {
            if (details.getEventType().equalsIgnoreCase("checked_in")) {
                checkInCount += details.getCount().intValue();
                checkInRevenue = checkInRevenue.add(details.getRevenue());
            } else if (details.getEventType().equalsIgnoreCase("checked_out")) {
                checkOutCount += details.getCount().intValue();
                checkOutRevenue = checkOutRevenue.add(details.getRevenue());
            }
        }
    }

    public ParkingReport(Integer checkInCount, Integer checkOutCount, BigDecimal checkInRevenue, BigDecimal checkOutRevenue) {
        this.checkInCount = checkInCount;
        this.checkOutCount = checkOutCount;
        this.checkInRevenue = checkInRevenue;
        this.checkOutRevenue = checkOutRevenue;
    }

    public List<ReportDetails> getReportDetails() {
        return reportDetails;
    }

    public void setReportDetails(List<ReportDetails> reportDetails) {
        this.reportDetails = reportDetails;
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
