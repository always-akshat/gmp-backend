package com.getMyParking.entity.reports;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by rahulgupta.s on 10/09/15.
 */
public class ParkingReportBySubLotType {

    private LocalDate date;

    private List<ParkingReport> parkingReports;

    public ParkingReportBySubLotType() {
    }

    public ParkingReportBySubLotType(LocalDate date, List<ParkingReport> parkingReports) {
        this.date = date;
        this.parkingReports = parkingReports;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ParkingReport> getParkingReports() {
        return parkingReports;
    }

    public void setParkingReports(List<ParkingReport> parkingReports) {
        this.parkingReports = parkingReports;
    }
}
