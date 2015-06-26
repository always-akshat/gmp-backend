package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rahulgupta.s on 14/05/15.
 */
public class ParkingReport {

    @JsonProperty
    private Integer numberOfCars;

    @JsonProperty
    private Integer numberOfBikes;

    @JsonProperty
    private Integer carRevenue;

    @JsonProperty
    private Integer bikeRevenue;

    public ParkingReport(Integer carNumbers, Integer bikeNumbers, Integer carsTotal, Integer bikeTotal) {
        numberOfBikes = bikeNumbers;
        bikeRevenue = bikeTotal;
        numberOfCars = carNumbers;
        carRevenue = carsTotal;
    }

    public Integer getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(Integer numberOfCars) {
        this.numberOfCars = numberOfCars;
    }

    public Integer getNumberOfBikes() {
        return numberOfBikes;
    }

    public void setNumberOfBikes(Integer numberOfBikes) {
        this.numberOfBikes = numberOfBikes;
    }

    public Integer getCarRevenue() {
        return carRevenue;
    }

    public void setCarRevenue(Integer carRevenue) {
        this.carRevenue = carRevenue;
    }

    public Integer getBikeRevenue() {
        return bikeRevenue;
    }

    public void setBikeRevenue(Integer bikeRevenue) {
        this.bikeRevenue = bikeRevenue;
    }
}
