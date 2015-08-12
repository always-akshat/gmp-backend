package com.getMyParking.entity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "pricing_slot", schema = "", catalog = "get_my_parking_v2")
public class PricingSlotEntity {
    private int id;
    private int day;
    private int startMinutesOfDay;
    private int endMinutesOfDay;
    private int parkingSubLotId;
    private Collection<PriceGridEntity> priceGridsById;
    private ParkingSubLotEntity parkingSubLotByParkingSubLotId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "day", nullable = false, insertable = true, updatable = true)
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Basic
    @Column(name = "start_minutes_of_day", nullable = false, insertable = true, updatable = true)
    public int getStartMinutesOfDay() {
        return startMinutesOfDay;
    }

    public void setStartMinutesOfDay(int startMinutesOfDay) {
        this.startMinutesOfDay = startMinutesOfDay;
    }

    @Basic
    @Column(name = "end_minutes_of_day", nullable = false, insertable = true, updatable = true)
    public int getEndMinutesOfDay() {
        return endMinutesOfDay;
    }

    public void setEndMinutesOfDay(int endMinutesOfDay) {
        this.endMinutesOfDay = endMinutesOfDay;
    }

    @Basic
    @Column(name = "parking_sub_lot_id", nullable = false, insertable = true, updatable = true)
    public int getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(int parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricingSlotEntity that = (PricingSlotEntity) o;

        if (day != that.day) return false;
        if (endMinutesOfDay != that.endMinutesOfDay) return false;
        if (id != that.id) return false;
        if (parkingSubLotId != that.parkingSubLotId) return false;
        if (startMinutesOfDay != that.startMinutesOfDay) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + day;
        result = 31 * result + startMinutesOfDay;
        result = 31 * result + endMinutesOfDay;
        result = 31 * result + parkingSubLotId;
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
    @JoinColumn(name = "parking_sub_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingSubLotEntity getParkingSubLotByParkingSubLotId() {
        return parkingSubLotByParkingSubLotId;
    }

    public void setParkingSubLotByParkingSubLotId(ParkingSubLotEntity parkingSubLotByParkingSubLotId) {
        this.parkingSubLotByParkingSubLotId = parkingSubLotByParkingSubLotId;
    }
}
