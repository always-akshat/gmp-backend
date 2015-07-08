package com.getMyParking.service.auth;

import java.util.List;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class GMPUser {

    private String authToken;

    private String userName;

    private String role;

    private List<Integer> parkingLotIds;

    public GMPUser(String authToken, String userName, String role, List<Integer> parkingLotIds) {
        this.authToken = authToken;
        this.userName = userName;
        this.role = role;
        this.parkingLotIds = parkingLotIds;
    }

    public GMPUser() {
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

    public List<Integer> getParkingLotIds() {
        return parkingLotIds;
    }

    public void setParkingLotIds(List<Integer> parkingLotIds) {
        this.parkingLotIds = parkingLotIds;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
