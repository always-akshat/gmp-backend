package com.getMyParking.entity;

import javax.persistence.*;
import java.sql.Time;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_sub_lot", schema = "", catalog = "get_my_parking_v2")
public class ParkingSubLotEntity {
    private int id;
    private String type;
    private int capacity;
    private String collectionModel;
    private Time taxiTime;
    private Time autoCheckoutTime;
    private int parkingLotId;
    private String plateNumberType;
    private byte mobileRequired;
    private byte valetName;
    private Time lastCheckinUpdateTime;
    private Collection<ParkingEventEntity> parkingEventsById;
    private Collection<ParkingLotHasUserB2BEntity> parkingLotHasUserB2BsById;
    private Collection<ParkingPassMasterEntity> parkingPassMastersById;
    private ParkingLotEntity parkingLotByParkingLotId;
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
    @Column(name = "type", nullable = false, insertable = true, updatable = true, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "capacity", nullable = false, insertable = true, updatable = true)
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Basic
    @Column(name = "collection_model", nullable = false, insertable = true, updatable = true, length = 500)
    public String getCollectionModel() {
        return collectionModel;
    }

    public void setCollectionModel(String collectionModel) {
        this.collectionModel = collectionModel;
    }

    @Basic
    @Column(name = "taxi_time", nullable = false, insertable = true, updatable = true)
    public Time getTaxiTime() {
        return taxiTime;
    }

    public void setTaxiTime(Time taxiTime) {
        this.taxiTime = taxiTime;
    }

    @Basic
    @Column(name = "auto_checkout_time", nullable = false, insertable = true, updatable = true)
    public Time getAutoCheckoutTime() {
        return autoCheckoutTime;
    }

    public void setAutoCheckoutTime(Time autoCheckoutTime) {
        this.autoCheckoutTime = autoCheckoutTime;
    }

    @Basic
    @Column(name = "parking_lot_id", nullable = false, insertable = true, updatable = true)
    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Basic
    @Column(name = "plate_number_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getPlateNumberType() {
        return plateNumberType;
    }

    public void setPlateNumberType(String plateNumberType) {
        this.plateNumberType = plateNumberType;
    }

    @Basic
    @Column(name = "mobile_required", nullable = false, insertable = true, updatable = true)
    public byte getMobileRequired() {
        return mobileRequired;
    }

    public void setMobileRequired(byte mobileRequired) {
        this.mobileRequired = mobileRequired;
    }

    @Basic
    @Column(name = "valet_name", nullable = false, insertable = true, updatable = true)
    public byte getValetName() {
        return valetName;
    }

    public void setValetName(byte valetName) {
        this.valetName = valetName;
    }

    @Basic
    @Column(name = "last_checkin_update_time", nullable = false, insertable = true, updatable = true)
    public Time getLastCheckinUpdateTime() {
        return lastCheckinUpdateTime;
    }

    public void setLastCheckinUpdateTime(Time lastCheckinUpdateTime) {
        this.lastCheckinUpdateTime = lastCheckinUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingSubLotEntity that = (ParkingSubLotEntity) o;

        if (capacity != that.capacity) return false;
        if (id != that.id) return false;
        if (mobileRequired != that.mobileRequired) return false;
        if (parkingLotId != that.parkingLotId) return false;
        if (valetName != that.valetName) return false;
        if (autoCheckoutTime != null ? !autoCheckoutTime.equals(that.autoCheckoutTime) : that.autoCheckoutTime != null)
            return false;
        if (collectionModel != null ? !collectionModel.equals(that.collectionModel) : that.collectionModel != null)
            return false;
        if (lastCheckinUpdateTime != null ? !lastCheckinUpdateTime.equals(that.lastCheckinUpdateTime) : that.lastCheckinUpdateTime != null)
            return false;
        if (plateNumberType != null ? !plateNumberType.equals(that.plateNumberType) : that.plateNumberType != null)
            return false;
        if (taxiTime != null ? !taxiTime.equals(that.taxiTime) : that.taxiTime != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + capacity;
        result = 31 * result + (collectionModel != null ? collectionModel.hashCode() : 0);
        result = 31 * result + (taxiTime != null ? taxiTime.hashCode() : 0);
        result = 31 * result + (autoCheckoutTime != null ? autoCheckoutTime.hashCode() : 0);
        result = 31 * result + parkingLotId;
        result = 31 * result + (plateNumberType != null ? plateNumberType.hashCode() : 0);
        result = 31 * result + (int) mobileRequired;
        result = 31 * result + (int) valetName;
        result = 31 * result + (lastCheckinUpdateTime != null ? lastCheckinUpdateTime.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "parkingSubLotByParkingSubLotId")
    public Collection<ParkingEventEntity> getParkingEventsById() {
        return parkingEventsById;
    }

    public void setParkingEventsById(Collection<ParkingEventEntity> parkingEventsById) {
        this.parkingEventsById = parkingEventsById;
    }

    @OneToMany(mappedBy = "parkingSubLotByParkingSubLotId")
    public Collection<ParkingLotHasUserB2BEntity> getParkingLotHasUserB2BsById() {
        return parkingLotHasUserB2BsById;
    }

    public void setParkingLotHasUserB2BsById(Collection<ParkingLotHasUserB2BEntity> parkingLotHasUserB2BsById) {
        this.parkingLotHasUserB2BsById = parkingLotHasUserB2BsById;
    }

    @OneToMany(mappedBy = "parkingSubLotByParkingSubLotId")
    public Collection<ParkingPassMasterEntity> getParkingPassMastersById() {
        return parkingPassMastersById;
    }

    public void setParkingPassMastersById(Collection<ParkingPassMasterEntity> parkingPassMastersById) {
        this.parkingPassMastersById = parkingPassMastersById;
    }

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLotByParkingLotId() {
        return parkingLotByParkingLotId;
    }

    public void setParkingLotByParkingLotId(ParkingLotEntity parkingLotByParkingLotId) {
        this.parkingLotByParkingLotId = parkingLotByParkingLotId;
    }

    @OneToMany(mappedBy = "parkingSubLotByParkingSubLotId")
    public Collection<PricingSlotEntity> getPricingSlotsById() {
        return pricingSlotsById;
    }

    public void setPricingSlotsById(Collection<PricingSlotEntity> pricingSlotsById) {
        this.pricingSlotsById = pricingSlotsById;
    }

    @OneToMany(mappedBy = "parkingSubLotByParkingSubLotId")
    public Collection<ReceiptContentEntity> getReceiptContentsById() {
        return receiptContentsById;
    }

    public void setReceiptContentsById(Collection<ReceiptContentEntity> receiptContentsById) {
        this.receiptContentsById = receiptContentsById;
    }
}
