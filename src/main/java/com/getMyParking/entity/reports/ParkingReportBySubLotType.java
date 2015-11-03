package com.getMyParking.entity.reports;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rahulgupta.s on 10/09/15.
 */
public class ParkingReportBySubLotType {

    private LocalDate date;

    private List<SubLotReport> parkingReports;

    private List<SubLotReportDetails> reportBySubLotType;

    public ParkingReportBySubLotType() {
    }

    public ParkingReportBySubLotType(LocalDate date, List<SubLotReportDetails> reportDetails) {
        this.date = date;
        this.reportBySubLotType = reportDetails;

        Map<String,List<SubLotReportDetails>> reportBySubLot =
                reportDetails.stream().collect(Collectors.groupingBy(SubLotReportDetails::getSubLotType));

        reportBySubLot.forEach((subLotType, subLotReportDetailsList) -> {
            SubLotReport subLotReport = new SubLotReport();
            subLotReport.setType(subLotType);
            subLotReportDetailsList.forEach(userReportDetails -> {
                if (userReportDetails.getEventType().equalsIgnoreCase("checked_in")) {
                    subLotReport.setCheckInCount(subLotReport.getCheckInCount() + userReportDetails.getCount().intValue());
                    subLotReport.setCheckInRevenue(subLotReport.getCheckInRevenue().add(userReportDetails.getRevenue()));
                } else if (userReportDetails.getEventType().equalsIgnoreCase("checked_out")) {
                    subLotReport.setCheckOutCount(subLotReport.getCheckOutCount() + userReportDetails.getCount().intValue());
                    subLotReport.setCheckOutRevenue(subLotReport.getCheckOutRevenue().add(userReportDetails.getRevenue()));
                }
            });
            parkingReports.add(subLotReport);
        });

    }

    public List<SubLotReportDetails> getReportBySubLotType() {
        return reportBySubLotType;
    }

    public void setReportBySubLotType(List<SubLotReportDetails> reportBySubLotType) {
        this.reportBySubLotType = reportBySubLotType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<SubLotReport> getParkingReports() {
        return parkingReports;
    }

    public void setParkingReports(List<SubLotReport> parkingReports) {
        this.parkingReports = parkingReports;
    }
}
