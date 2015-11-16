package com.getMyParking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 29/10/15.
 */
public class ParkingEventDumpDTO {

    private String registrationNumber;
    private String mobileNumber;
    private String valetName;
    private DateTime checkInEventTime;
    private BigDecimal checkInCost;
    private String subLotType;
    private DateTime checkOutEventTime;
    private BigDecimal checkOutCost;
    private String serialNumber;
    private String special;
    private String checkinOperatorName;
    private String checkoutOperatorName;
    private DateTime passValidTime;
    private DateTime eventTime;
    @JsonIgnore
    private String eventType;
    @JsonIgnore
    private BigDecimal eventCost;
    @JsonIgnore
    private String eventOperatorName;
    @JsonIgnore
    private DateTime joinedEventTime;
    @JsonIgnore
    private String joinedEventType;
    @JsonIgnore
    private BigDecimal joinedEventCost;
    @JsonIgnore
    private String joinedEventOperatorName;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getValetName() {
        return valetName;
    }

    public void setValetName(String valetName) {
        this.valetName = valetName;
    }

    public DateTime getCheckInEventTime() {
        return checkInEventTime;
    }

    public void setCheckInEventTime(DateTime checkInEventTime) {
        this.checkInEventTime = checkInEventTime;
    }

    public BigDecimal getCheckInCost() {
        return checkInCost;
    }

    public void setCheckInCost(BigDecimal checkInCost) {
        this.checkInCost = checkInCost;
    }

    public String getSubLotType() {
        return subLotType;
    }

    public void setSubLotType(String subLotType) {
        this.subLotType = subLotType;
    }

    public DateTime getCheckOutEventTime() {
        return checkOutEventTime;
    }

    public void setCheckOutEventTime(DateTime checkOutEventTime) {
        this.checkOutEventTime = checkOutEventTime;
    }

    public BigDecimal getCheckOutCost() {
        return checkOutCost;
    }

    public void setCheckOutCost(BigDecimal checkOutCost) {
        this.checkOutCost = checkOutCost;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public DateTime getPassValidTime() {
        return passValidTime;
    }

    public void setPassValidTime(DateTime passValidTime) {
        this.passValidTime = passValidTime;
    }

    public DateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getCheckinOperatorName() {
        return checkinOperatorName;
    }

    public void setCheckinOperatorName(String checkinOperatorName) {
        this.checkinOperatorName = checkinOperatorName;
    }

    public String getCheckoutOperatorName() {
        return checkoutOperatorName;
    }

    public void setCheckoutOperatorName(String checkoutOperatorName) {
        this.checkoutOperatorName = checkoutOperatorName;
    }

    public String getEventOperatorName() {
        return eventOperatorName;
    }

    public void setEventOperatorName(String eventOperatorName) {
        this.eventOperatorName = eventOperatorName;
    }

    public String getJoinedEventOperatorName() {
        return joinedEventOperatorName;
    }

    public void setJoinedEventOperatorName(String joinedEventOperatorName) {
        this.joinedEventOperatorName = joinedEventOperatorName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public BigDecimal getEventCost() {
        return eventCost;
    }

    public void setEventCost(BigDecimal eventCost) {
        this.eventCost = eventCost;
    }

    public DateTime getJoinedEventTime() {
        return joinedEventTime;
    }

    public void setJoinedEventTime(DateTime joinedEventTime) {
        this.joinedEventTime = joinedEventTime;
    }

    public String getJoinedEventType() {
        return joinedEventType;
    }

    public void setJoinedEventType(String joinedEventType) {
        this.joinedEventType = joinedEventType;
    }

    public BigDecimal getJoinedEventCost() {
        return joinedEventCost;
    }

    public void setJoinedEventCost(BigDecimal joinedEventCost) {
        this.joinedEventCost = joinedEventCost;
    }
}
