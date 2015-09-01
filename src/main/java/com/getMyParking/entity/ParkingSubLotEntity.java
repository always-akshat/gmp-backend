package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_sub_lot", schema = "", catalog = "get_my_parking_v2")
public class ParkingSubLotEntity {
    private Integer id;
    private String type;
    private Integer capacity;
    private String collectionModel;
    private Time taxiTime;
    private Time autoCheckoutTime;
    private String plateNumberType;
    private String mobileRequired;
    private String valetName;
    private Time lastCheckinUpdateTime;
    private Set<ParkingPassMasterEntity> parkingPasses;
    private Set<PricingSlotEntity> pricingSlots;
    private Set<ReceiptContentEntity> receiptContents;
    @JsonIgnore
    private ParkingLotEntity parkingLot;
    private BigDecimal autoCheckoutCost;

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
    @Column(name = "type", nullable = false, insertable = true, updatable = true, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "capacity", nullable = false, insertable = true, updatable = true)
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
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
    @Column(name = "plate_number_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getPlateNumberType() {
        return plateNumberType;
    }

    public void setPlateNumberType(String plateNumberType) {
        this.plateNumberType = plateNumberType;
    }

    @Basic
    @Column(name = "mobile_required", nullable = false, insertable = true, updatable = true)
    public String getMobileRequired() {
        return mobileRequired;
    }

    public void setMobileRequired(String mobileRequired) {
        this.mobileRequired = mobileRequired;
    }

    @Basic
    @Column(name = "valet_name", nullable = false, insertable = true, updatable = true)
    public String getValetName() {
        return valetName;
    }

    public void setValetName(String valetName) {
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
        result = 31 * result + (plateNumberType != null ? plateNumberType.hashCode() : 0);
        result = 31 * result + mobileRequired.hashCode();
        result = 31 * result + valetName.hashCode();
        result = 31 * result + (lastCheckinUpdateTime != null ? lastCheckinUpdateTime.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "parkingSubLot", fetch = FetchType.EAGER)
    public Set<ParkingPassMasterEntity> getParkingPasses() {
        return parkingPasses;
    }

    public void setParkingPasses(Set<ParkingPassMasterEntity> parkingPassMastersById) {
        this.parkingPasses = parkingPassMastersById;
    }

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLotEntity parkingLotByParkingLotId) {
        this.parkingLot = parkingLotByParkingLotId;
    }

    @OneToMany(mappedBy = "parkingSubLot", fetch = FetchType.EAGER)
    public Set<PricingSlotEntity> getPricingSlots() {
        return pricingSlots;
    }

    public void setPricingSlots(Set<PricingSlotEntity> pricingSlotsById) {
        this.pricingSlots = pricingSlotsById;
    }

    @OneToMany(mappedBy = "parkingSubLot", fetch = FetchType.EAGER)
    public Set<ReceiptContentEntity> getReceiptContents() {
        return receiptContents;
    }

    public void setReceiptContents(Set<ReceiptContentEntity> receiptContentsById) {
        this.receiptContents = receiptContentsById;
    }

    @Basic
    @Column(name = "auto_checkout_cost", nullable = false, insertable = true, updatable = true)
    public BigDecimal getAutoCheckoutCost() {
        return autoCheckoutCost;
    }

    public void setAutoCheckoutCost(BigDecimal autoCheckoutCost) {
        this.autoCheckoutCost = autoCheckoutCost;
    }
}
