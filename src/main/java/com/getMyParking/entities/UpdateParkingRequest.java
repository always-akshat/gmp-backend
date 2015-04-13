package com.getMyParking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 30/03/15.
 */
public class UpdateParkingRequest {

    @JsonProperty("phone_number")
    @NotNull
    private String phoneNumber;


    @JsonProperty("registration_number")
    @NotNull
    private String registrationNumber;

    @NotNull
    private Integer id;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
