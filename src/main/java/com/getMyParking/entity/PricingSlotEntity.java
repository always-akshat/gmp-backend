package com.getMyParking.entity;

import javax.persistence.*;
import java.sql.Time;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "pricing_slot", schema = "", catalog = "get_my_parking")
public class PricingSlotEntity {
    private int id;
    private String vehicleType;
    private String day;
    private Time startTime;
    private Time endTime;
    private Collection<PriceGridEntity> priceGridsById;
    private ParkingLotEntity parkingLotByParkingLotId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
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
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Basic
    @Column(name = "start_time", nullable = false, insertable = true, updatable = true)
    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time", nullable = false, insertable = true, updatable = true)
    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricingSlotEntity that = (PricingSlotEntity) o;

        if (id != that.id) return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (vehicleType != null ? !vehicleType.equals(that.vehicleType) : that.vehicleType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "pricingSlotByPricingId")
    public Collection<PriceGridEntity> getPriceGridsById() {
        return priceGridsById;
    }

    public void setPriceGridsById(Collection<PriceGridEntity> priceGridsById) {
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
