package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_pass_master", schema = "", catalog = "get_my_parking_v2")
public class ParkingPassMasterEntity {
    private Integer id;
    private String name;
    private String passType;
    private Integer numbers;
    private Integer price;
    private String vehicleType;
    private String isActive;
    @JsonIgnore
    private ParkingEntity parking;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 500)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "pass_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    @Basic
    @Column(name = "numbers", nullable = false, insertable = true, updatable = true)
    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    @Basic
    @Column(name = "price", nullable = false, insertable = true, updatable = true)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "vehicle_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Basic
    @Column(name = "isActive", nullable = false, insertable = true, updatable = true, length = 45)
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingPassMasterEntity)) return false;

        ParkingPassMasterEntity that = (ParkingPassMasterEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (numbers != null ? !numbers.equals(that.numbers) : that.numbers != null) return false;
        if (parking != null ? !parking.equals(that.parking) : that.parking != null) return false;
        if (passType != null ? !passType.equals(that.passType) : that.passType != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (vehicleType != null ? !vehicleType.equals(that.vehicleType) : that.vehicleType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (passType != null ? passType.hashCode() : 0);
        result = 31 * result + (numbers != null ? numbers.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (parking != null ? parking.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_id", referencedColumnName = "id", nullable = false)
    public ParkingEntity getParking() {
        return parking;
    }

    public void setParking(ParkingEntity parkingSubLotByParkingSubLotId) {
        this.parking = parkingSubLotByParkingSubLotId;
    }
}
