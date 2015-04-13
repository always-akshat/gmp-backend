package com.getMyParking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 30/03/15.
 */
public class UpdateCostRequest {

    @JsonProperty
    @NotNull
    private Integer cost;

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
