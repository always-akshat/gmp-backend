package com.getMyParking.dto;

import com.getMyParking.entity.ParkingPassEntity;

import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 29/10/15.
 */
public class ActiveParkingPassDTO {

    private ParkingPassEntity parking_pass;
    private Integer count;
    private Integer isPaidCount;

    public ParkingPassEntity getParkingPass() {
        return parking_pass;
    }

    public void setParkingPass(ParkingPassEntity parking_pass) {
        this.parking_pass = parking_pass;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIsPaidCount() {
        return isPaidCount;
    }

    public void setIsPaidCount(Integer isPaidCount) {
        this.isPaidCount = isPaidCount;
    }
}
