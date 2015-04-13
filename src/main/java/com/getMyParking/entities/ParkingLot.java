package com.getMyParking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by rahulgupta.s on 15/03/15.
 */

@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "name", nullable = false)
    @JsonProperty("name")
    @NotNull
    private String name;

    @Column(name = "car_capacity", nullable = false)
    @JsonProperty("car_capacity")
    @NotNull
    private Integer carCapacity;

    @Column(name = "bike_capacity", nullable = false)
    @JsonProperty("bike_capacity")
    @NotNull
    private Integer bikeCapacity;

    @Column(name = "address", nullable = false)
    @JsonProperty("address")
    @NotNull
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCarCapacity() {
        return carCapacity;
    }

    public void setCarCapacity(Integer carCapacity) {
        this.carCapacity = carCapacity;
    }

    public Integer getBikeCapacity() {
        return bikeCapacity;
    }

    public void setBikeCapacity(Integer bikeCapacity) {
        this.bikeCapacity = bikeCapacity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
