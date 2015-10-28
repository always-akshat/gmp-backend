package com.getMyParking.service.auth;

import com.getMyParking.entity.AccessMasterEntity;
import com.getMyParking.entity.ParkingSubLotUserAccessEntity;
import com.getMyParking.entity.UserB2BEntity;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class GMPUser {

    private String userName;
    private String name;
    private List<Integer> parkingSubLotIds;
    private List<Integer> parkingLotIds;
    private List<Integer> parkingIds;
    private List<Integer> companyIds;
    private List<AccessMasterEntity> userAccesses;
    private String authToken;
    private DateTime validTime;

    public GMPUser(UserB2BEntity userB2BEntity,String authToken, DateTime validTime) {

        this.parkingSubLotIds = Lists.newArrayList();
        for (ParkingSubLotUserAccessEntity entity : userB2BEntity.getParkingSubLots()) {
            parkingSubLotIds.add(entity.getParkingSubLotId());
        }

        this.parkingLotIds = Lists.newArrayList();
        for (ParkingSubLotUserAccessEntity entity : userB2BEntity.getParkingSubLots()) {
            if (!parkingLotIds.contains(entity.getParkingLotId()))
                parkingLotIds.add(entity.getParkingLotId());
        }

        parkingIds = Lists.newArrayList();
        for (ParkingSubLotUserAccessEntity entity : userB2BEntity.getParkingSubLots()) {
            if (!parkingIds.contains(entity.getParkingId()))
                parkingIds.add(entity.getParkingId());
        }

        companyIds = Lists.newArrayList();
        for (ParkingSubLotUserAccessEntity entity : userB2BEntity.getParkingSubLots()) {
            if (!companyIds.contains(entity.getCompanyId()))
                companyIds.add(entity.getCompanyId());
        }

        this.userName = userB2BEntity.getUsername();
        this.name = userB2BEntity.getName();
        this.userAccesses = Lists.newArrayList(userB2BEntity.getUserAccesses());
        this.authToken = authToken;
        this.validTime = validTime;
    }

    public GMPUser() {
    }

    public DateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(DateTime validTime) {
        this.validTime = validTime;
    }

    public List<Integer> getParkingLotIds() {
        return parkingLotIds;
    }

    public void setParkingLotIds(List<Integer> parkingLotIds) {
        this.parkingLotIds = parkingLotIds;
    }

    public List<Integer> getParkingIds() {
        return parkingIds;
    }

    public void setParkingIds(List<Integer> parkingIds) {
        this.parkingIds = parkingIds;
    }

    public List<Integer> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<Integer> companyIds) {
        this.companyIds = companyIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AccessMasterEntity> getUserAccesses() {
        return userAccesses;
    }

    public void setUserAccesses(List<AccessMasterEntity> userAccesses) {
        this.userAccesses = userAccesses;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Integer> getParkingSubLotIds() {
        return parkingSubLotIds;
    }

    public void setParkingSubLotIds(List<Integer> parkingSubLotIds) {
        this.parkingSubLotIds = parkingSubLotIds;
    }

}
