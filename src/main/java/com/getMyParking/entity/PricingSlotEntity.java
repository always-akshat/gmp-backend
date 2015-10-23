package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "pricing_slot", schema = "", catalog = "get_my_parking_v2")
public class PricingSlotEntity {
    private Integer id;
    private Integer day;
    private Integer startMinutesOfDay;
    private Integer endMinutesOfDay;
    private Set<PriceGridEntity> priceGrids;
    @JsonIgnore
    private ParkingSubLotEntity parkingSubLot;

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
    @Column(name = "day", nullable = false, insertable = true, updatable = true)
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

    public void setStartMinutesOfDay(Integer startMinutesOfDay) {
        this.startMinutesOfDay = startMinutesOfDay;
    }

    @Basic
    @Column(name = "end_minutes_of_day", nullable = false, insertable = true, updatable = true)
    public Integer getEndMinutesOfDay() {
        return endMinutesOfDay;
    }

    public void setEndMinutesOfDay(Integer endMinutesOfDay) {
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
        int result=0;
        if(id!=null)
            result = id;
        if(day == null){
            System.out.println("day null" + result);
        }
        result = 31 * result + day;
        result = 31 * result + startMinutesOfDay;
        result = 31 * result + endMinutesOfDay;
        return result;
    }

    @OneToMany(mappedBy = "pricingSlot", fetch = FetchType.EAGER)
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
