package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Collection;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_lot", schema = "", catalog = "get_my_parking_v2")
public class ParkingLotEntity {
    private Integer id;
    private String name;
    private Time openTime;
    private Time closeTime;
    private BigDecimal longitude;
    private BigDecimal latitude;
    @JsonIgnore
    private ParkingEntity parking;
    private Set<ParkingSubLotEntity> parkingSubLots;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "open_time", nullable = false, insertable = true, updatable = true)
    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }

    @Basic
    @Column(name = "close_time", nullable = false, insertable = true, updatable = true)
    public Time getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Time closeTime) {
        this.closeTime = closeTime;
    }

    @Basic
    @Column(name = "longitude", nullable = false, insertable = true, updatable = true, precision = 4)
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "latitude", nullable = false, insertable = true, updatable = true, precision = 4)
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingLotEntity that = (ParkingLotEntity) o;

        if (id != that.id) return false;
        if (closeTime != null ? !closeTime.equals(that.closeTime) : that.closeTime != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (openTime != null ? !openTime.equals(that.openTime) : that.openTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (openTime != null ? openTime.hashCode() : 0);
        result = 31 * result + (closeTime != null ? closeTime.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_id", referencedColumnName = "id", nullable = false)
    public ParkingEntity getParking() {
        return parking;
    }

    public void setParking(ParkingEntity parkingByParkingId) {
        this.parking = parkingByParkingId;
    }

    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.EAGER)
    public Set<ParkingSubLotEntity> getParkingSubLots() {
        return parkingSubLots;
    }

    public void setParkingSubLots(Set<ParkingSubLotEntity> parkingSubLotsById) {
        this.parkingSubLots = parkingSubLotsById;
    }
}
