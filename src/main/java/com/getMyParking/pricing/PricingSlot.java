package com.getMyParking.pricing;

import java.util.List;

/**
 * Created by rahulgupta.s on 05/06/15.
 */
public class PricingSlot {

    private int id;
    private String vehicleType;
    private int day;
    private Integer startMinutesOfDay;
    private Integer endMinutesOfDay;
    private List<PriceGrid> priceGrids;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getStartMinutesOfDay() {
        return startMinutesOfDay;
    }

    public void setStartMinutesOfDay(Integer startMinutesOfDay) {
        this.startMinutesOfDay = startMinutesOfDay;
    }

    public Integer getEndMinutesOfDay() {
        return endMinutesOfDay;
    }

    public void setEndMinutesOfDay(Integer endMinutesOfDay) {
        this.endMinutesOfDay = endMinutesOfDay;
    }

    public List<PriceGrid> getPriceGrids() {
        return priceGrids;
    }

    public void setPriceGrids(List<PriceGrid> priceGrids) {
        this.priceGrids = priceGrids;
    }
}
