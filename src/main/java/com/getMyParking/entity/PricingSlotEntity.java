package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "pricing_slot", schema = "", catalog = "get_my_parking")
public class PricingSlotEntity {

    private int id;
    @NotNull
    private String vehicleType;
    @NotNull
    private Integer day;
    @NotNull
    private Integer startMinutesOfDay;
    @NotNull
    private Integer endMinutesOfDay;
    @NotNull
    private String collectionModel;
    @NotNull
    private Set<PriceGridEntity> priceGridsById;
    @JsonIgnore
    private ParkingLotEntity parkingLotByParkingLotId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "vehicle_type", nullable = true, insertable = true, updatable = true, length = 255)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Basic
    @Column(name = "day", nullable = false, insertable = true, updatable = true, length = 45)
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Basic
    @Column(name = "start_minutes_of_day", nullable = false, insertable = true, updatable = true)
    public Integer getStartMinutesOfDay() {
        return startMinutesOfDay;
    }

    public void setStartMinutesOfDay(Integer startTime) {
        this.startMinutesOfDay = startTime;
    }

    @Basic
    @Column(name = "collection_model", nullable = false, insertable = true, updatable = true)
    public String getCollectionModel() {
        return collectionModel;
    }

    public void setCollectionModel(String collectionModel) {
        this.collectionModel = collectionModel;
    }

    @Basic
    @Column(name = "end_minutes_of_day", nullable = false, insertable = true, updatable = true)
    public Integer getEndMinutesOfDay() {
        return endMinutesOfDay;
    }

    public void setEndMinutesOfDay(Integer endTime) {
        this.endMinutesOfDay = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricingSlotEntity that = (PricingSlotEntity) o;

        if (id != that.id) return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;
        if (endMinutesOfDay != null ? !endMinutesOfDay.equals(that.endMinutesOfDay) : that.endMinutesOfDay != null) return false;
        if (startMinutesOfDay != null ? !startMinutesOfDay.equals(that.startMinutesOfDay) : that.startMinutesOfDay != null) return false;
        if (vehicleType != null ? !vehicleType.equals(that.vehicleType) : that.vehicleType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (startMinutesOfDay != null ? startMinutesOfDay.hashCode() : 0);
        result = 31 * result + (endMinutesOfDay != null ? endMinutesOfDay.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "pricingSlotByPricingId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<PriceGridEntity> getPriceGridsById() {
        return priceGridsById;
    }

    public void setPriceGridsById(Set<PriceGridEntity> priceGridsById) {
        this.priceGridsById = priceGridsById;
    }

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLotByParkingLotId() {
        return parkingLotByParkingLotId;
    }

    public void setParkingLotByParkingLotId(ParkingLotEntity parkingLotByParkingLotId) {
        this.parkingLotByParkingLotId = parkingLotByParkingLotId;
    }
}
