package com.getMyParking.pricing;

import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 05/06/15.
 */
public class PriceGrid {

    private int id;
    private String priceStructure;
    private int cost;
    private Integer duration;
    private int sequenceNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPriceStructure() {
        return priceStructure;
    }

    public void setPriceStructure(String priceStructure) {
        this.priceStructure = priceStructure;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
