package com.getMyParking.service.auth;

import com.getMyParking.entity.ParkingLotHasUserB2BEntity;
import com.getMyParking.entity.UserAccessEntity;

import java.util.List;
import java.util.Set;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class GMPUser {

    private String userName;
    private String name;
    private List<Integer> parkingSubLotIds;
    private List<UserAccessEntity> userAccesses;

    private String authToken;

    public GMPUser(String userName, String name, List<Integer> parkingSubLotIds, List<UserAccessEntity> userAccesses,
                   String authToken) {
        this.userName = userName;
        this.name = name;
        this.parkingSubLotIds = parkingSubLotIds;
        this.userAccesses = userAccesses;
        this.authToken = authToken;
    }

    public GMPUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserAccessEntity> getUserAccesses() {
        return userAccesses;
    }

    public void setUserAccesses(List<UserAccessEntity> userAccesses) {
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
