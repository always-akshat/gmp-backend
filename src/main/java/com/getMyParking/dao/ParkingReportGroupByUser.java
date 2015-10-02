package com.getMyParking.dao;

import com.getMyParking.entity.ParkingReport;

import java.util.List;

/**
 * Created by rahulgupta.s on 02/10/15.
 */
public class ParkingReportGroupByUser {

    private String username;

    private Integer parkingId;

    private Integer companyId;

    private Integer parkingLotId;

    private List<ParkingReport> parkingReports;

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

    public List<ParkingReport> getParkingReports() {
        return parkingReports;
    }

    public void setParkingReports(List<ParkingReport> parkingReports) {
        this.parkingReports = parkingReports;
    }
}
