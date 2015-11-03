package com.getMyParking.entity.reports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        Map<Integer,List<UserParkingReportDetails>> detailsBySubLotId =
        reportDetails.stream().collect(Collectors.groupingBy(UserParkingReportDetails::getParkingSubLotId));
        for (Map.Entry<Integer,List<UserParkingReportDetails>> entry : detailsBySubLotId.entrySet()) {
            UserParkingReport parkingReport = new UserParkingReport();
            parkingReport.setParkingSubLotId(entry.getKey());
            List<UserParkingReportDetails> reportDetail = entry.getValue();
            reportDetail.forEach(userReportDetails -> {
                if (userReportDetails.getEventType().equalsIgnoreCase("checked_in")) {
                    parkingReport.setCheckInCount(parkingReport.getCheckInCount() + userReportDetails.getCount().intValue());
                    parkingReport.setCheckInRevenue(parkingReport.getCheckInRevenue().add(userReportDetails.getRevenue()));
                } else if (userReportDetails.getEventType().equalsIgnoreCase("checked_out")) {
                    parkingReport.setCheckOutCount(parkingReport.getCheckOutCount() + userReportDetails.getCount().intValue());
                    parkingReport.setCheckOutRevenue(parkingReport.getCheckOutRevenue().add(userReportDetails.getRevenue()));
                }
            });
            parkingReports.add(parkingReport);
        }
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
