package com.getMyParking.entity;

import javax.persistence.*;
import java.util.Set;

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
    private Set<PriceGridEntity> priceGrids;
    private ParkingSubLotEntity parkingSubLot;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricingSlotEntity that = (PricingSlotEntity) o;

        if (day != that.day) return false;
        if (endMinutesOfDay != that.endMinutesOfDay) return false;
        if (id != that.id) return false;
        if (startMinutesOfDay != that.startMinutesOfDay) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + day;
        result = 31 * result + startMinutesOfDay;
        result = 31 * result + endMinutesOfDay;
        return result;
    }

    @OneToMany(mappedBy = "pricingSlot")
    public Set<PriceGridEntity> getPriceGrids() {
        return priceGrids;
    }

    public void setPriceGrids(Set<PriceGridEntity> priceGridsById) {
        this.priceGrids = priceGridsById;
    }

    @ManyToOne
    @JoinColumn(name = "parking_sub_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingSubLotEntity getParkingSubLot() {
        return parkingSubLot;
    }

    public void setParkingSubLot(ParkingSubLotEntity parkingSubLotByParkingSubLotId) {
        this.parkingSubLot = parkingSubLotByParkingSubLotId;
    }
}
