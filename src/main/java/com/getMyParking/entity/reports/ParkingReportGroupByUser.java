package com.getMyParking.entity.reports;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rahulgupta.s on 02/10/15.
 */
public class ParkingReportGroupByUser {

    private String username;

    private Integer parkingId;

    private Integer companyId;

    private Integer parkingLotId;

    private List<UserParkingReport> parkingReports;

    private List<UserParkingReportDetails> reportDetails;

    public List<UserParkingReportDetails> getReportDetails() {
        return reportDetails;
    }

    public void setReportDetails(List<UserParkingReportDetails> reportDetails) {
        this.reportDetails = reportDetails;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public List<UserParkingReport> getParkingReports() {
        return parkingReports;
    }

    public void setParkingReports(List<UserParkingReport> parkingReports) {
        this.parkingReports = parkingReports;
    }

}
