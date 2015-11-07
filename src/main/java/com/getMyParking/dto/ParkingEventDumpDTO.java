package com.getMyParking.dto;

import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 29/10/15.
 */
public class ParkingEventDumpDTO implements Comparable<ParkingEventDumpDTO>{

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
    private String operatorName;
    private DateTime passValidTime;
    private DateTime eventTime;

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

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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

    @Override
    public int compareTo(@Nullable ParkingEventDumpDTO that) {
        if (that == null) return -1;
        else return this.getEventTime().compareTo(that.getEventTime());
    }
}
