package com.getMyParking.service.auth;

/**
 * Created by rahulgupta.s on 03/06/15.
 */
public class GMPCredentials {

    private String authToken;

    public GMPCredentials(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
