package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Set;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "parking_lot", schema = "", catalog = "get_my_parking")
public class ParkingLotEntity {

    private int id;
    @NotNull
    private String name;
    @NotNull
    private Time openTime;
    @NotNull
    private Time closeTime;
    private Time autoCheckoutTime;
    @NotNull
    private BigDecimal latitude;
    @NotNull
    private BigDecimal longitude;
    @NotNull
    private int carCapacity;
    @NotNull
    private int bikeCapacity;
    private int taxiSupport;
    private int taxiTime;
    private int barcodeSupport;
    private String licenseNo;
    @NotNull
    private String collectionModel;
    @JsonProperty("parking")
    private ParkingEntity parkingByParkingId;

    private Set<ParkingPassMasterEntity> parkingPassMastersById;
    private Set<PricingSlotEntity> pricingSlotsById;
    private Set<ReceiptContentEntity> receiptContentsById;

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
    @Column(name = "auto_checkout_time", nullable = true, insertable = true, updatable = true)
    public Time getAutoCheckoutTime() {
        return autoCheckoutTime;
    }

    public void setAutoCheckoutTime(Time autoCheckoutTime) {
        this.autoCheckoutTime = autoCheckoutTime;
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
    @Column(name = "barcode_support", nullable = false, insertable = true, updatable = true)
    public int getBarcodeSupport() {
        return barcodeSupport;
    }

    public void setBarcodeSupport(int barcodeSupport) {
        this.barcodeSupport = barcodeSupport;
    }

    @Basic
    @Column(name = "collection_model", nullable = true, insertable = true, updatable = true, length = 500)
    public String getCollectionModel() {
        return collectionModel;
    }

    public void setCollectionModel(String collectionModel) {
        this.collectionModel = collectionModel;
    }

    @Basic
    @Column(name = "taxi_support", nullable = false, insertable = true, updatable = true)
    public int getTaxiSupport() {
        return taxiSupport;
    }

    public void setTaxiSupport(int taxiSupport) {
        this.taxiSupport = taxiSupport;
    }

    @Basic
    @Column(name = "taxi_time", nullable = true, insertable = true, updatable = true)
    public int getTaxiTime() {
        return taxiTime;
    }

    public void setTaxiTime(int taxiTime) {
        this.taxiTime = taxiTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingLotEntity that = (ParkingLotEntity) o;

        if (bikeCapacity != that.bikeCapacity) return false;
        if (carCapacity != that.carCapacity) return false;
        if (id != that.id) return false;
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
        result = 31 * result + (collectionModel != null ? collectionModel.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_id", referencedColumnName = "id", nullable = false)
    public ParkingEntity getParkingByParkingId() {
        return parkingByParkingId;
    }

    public void setParkingByParkingId(ParkingEntity parkingByParkingId) {
        this.parkingByParkingId = parkingByParkingId;
    }

    @OneToMany(mappedBy = "parkingLotByParkingLotId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<ParkingPassMasterEntity> getParkingPassMastersById() {
        return parkingPassMastersById;
    }

    public void setParkingPassMastersById(Set<ParkingPassMasterEntity> parkingPassMastersById) {
        this.parkingPassMastersById = parkingPassMastersById;
    }

    @OneToMany(mappedBy = "parkingLotByParkingLotId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<PricingSlotEntity> getPricingSlotsById() {
        return pricingSlotsById;
    }

    public void setPricingSlotsById(Set<PricingSlotEntity> pricingSlotsById) {
        this.pricingSlotsById = pricingSlotsById;
    }

    @OneToMany(mappedBy = "parkingLotByParkingLotId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<ReceiptContentEntity> getReceiptContentsById() {
        return receiptContentsById;
    }

    public void setReceiptContentsById(Set<ReceiptContentEntity> receiptContentsById) {
        this.receiptContentsById = receiptContentsById;
    }
}
