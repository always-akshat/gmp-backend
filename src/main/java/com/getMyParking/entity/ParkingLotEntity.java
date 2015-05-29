package com.getMyParking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 30/05/15.
 */
@Entity
@Table(name = "parking_lot", schema = "", catalog = "get_my_parking")
public class ParkingLotEntity {
    private int id;
    private String name;
    private Time openTime;
    private Time closeTime;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int carCapacity;
    private int bikeCapacity;
    private String licenseNo;
    private int parkingId;
    private String collectionModel;
    private Collection<PricingSlotEntity> pricingSlotsById;
    private Collection<ReceiptContentEntity> receiptContentsById;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 45)
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
    @Column(name = "latitude", nullable = false, insertable = true, updatable = true, precision = 10)
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude", nullable = false, insertable = true, updatable = true, precision = 10)
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "car_capacity", nullable = false, insertable = true, updatable = true)
    public int getCarCapacity() {
        return carCapacity;
    }

    public void setCarCapacity(int carCapacity) {
        this.carCapacity = carCapacity;
    }

    @Basic
    @Column(name = "bike_capacity", nullable = false, insertable = true, updatable = true)
    public int getBikeCapacity() {
        return bikeCapacity;
    }

    public void setBikeCapacity(int bikeCapacity) {
        this.bikeCapacity = bikeCapacity;
    }

    @Basic
    @Column(name = "license_no", nullable = true, insertable = true, updatable = true, length = 500)
    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    @Basic
    @Column(name = "parking_id", nullable = false, insertable = true, updatable = true)
    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    @Basic
    @Column(name = "collection_model", nullable = true, insertable = true, updatable = true, length = 500)
    public String getCollectionModel() {
        return collectionModel;
    }

    public void setCollectionModel(String collectionModel) {
        this.collectionModel = collectionModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingLotEntity that = (ParkingLotEntity) o;

        if (bikeCapacity != that.bikeCapacity) return false;
        if (carCapacity != that.carCapacity) return false;
        if (id != that.id) return false;
        if (parkingId != that.parkingId) return false;
        if (closeTime != null ? !closeTime.equals(that.closeTime) : that.closeTime != null) return false;
        if (collectionModel != null ? !collectionModel.equals(that.collectionModel) : that.collectionModel != null)
            return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (licenseNo != null ? !licenseNo.equals(that.licenseNo) : that.licenseNo != null) return false;
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
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + carCapacity;
        result = 31 * result + bikeCapacity;
        result = 31 * result + (licenseNo != null ? licenseNo.hashCode() : 0);
        result = 31 * result + parkingId;
        result = 31 * result + (collectionModel != null ? collectionModel.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "parkingLotByParkingLotId")
    public Collection<PricingSlotEntity> getPricingSlotsById() {
        return pricingSlotsById;
    }

    public void setPricingSlotsById(Collection<PricingSlotEntity> pricingSlotsById) {
        this.pricingSlotsById = pricingSlotsById;
    }

    @OneToMany(mappedBy = "parkingLotByParkingLotId")
    public Collection<ReceiptContentEntity> getReceiptContentsById() {
        return receiptContentsById;
    }

    public void setReceiptContentsById(Collection<ReceiptContentEntity> receiptContentsById) {
        this.receiptContentsById = receiptContentsById;
    }
}
