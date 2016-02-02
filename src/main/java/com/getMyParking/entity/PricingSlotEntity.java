package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "pricing_slot", schema = "", catalog = "get_my_parking_v3")
public class PricingSlotEntity {
    private Integer id;
    private Integer day;
    private Integer startMinutesOfDay;
    private Integer endMinutesOfDay;
    private Set<PriceGridEntity> priceGrids;
    @JsonIgnore
    private ParkingSubLotEntity parkingSubLot;
    private String type;

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

    @Basic
    @Column(name = "type", nullable = false, insertable = true, updatable = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PricingSlotEntity that = (PricingSlotEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (startMinutesOfDay != null ? !startMinutesOfDay.equals(that.startMinutesOfDay) : that.startMinutesOfDay != null)
            return false;
        return !(endMinutesOfDay != null ? !endMinutesOfDay.equals(that.endMinutesOfDay) : that.endMinutesOfDay != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (day != null ? day.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (startMinutesOfDay != null ? startMinutesOfDay.hashCode() : 0);
        result = 31 * result + (endMinutesOfDay != null ? endMinutesOfDay.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "pricingSlot", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
