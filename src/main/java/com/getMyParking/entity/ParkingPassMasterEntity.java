package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_pass_master", schema = "", catalog = "get_my_parking_v2")
public class ParkingPassMasterEntity {
    private Integer id;
    private String name;
    private String passType;
    private Integer numbers;
    private Integer price;
    private String vehicleType;
    private String isActive;
    private String isRegistrationNumber;
    private String isName;
    private String isMobileNumber;
    private String isRFID;
    private String isPaid;
    private Integer autoRenewal;
    private Set<ReceiptContentEntity> receiptContents;
    @JsonIgnore
    private ParkingEntity parking;

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
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 500)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "pass_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    @Basic
    @Column(name = "numbers", nullable = false, insertable = true, updatable = true)
    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    @Basic
    @Column(name = "price", nullable = false, insertable = true, updatable = true)
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "vehicle_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Basic
    @Column(name = "isActive", nullable = false, insertable = true, updatable = true, length = 45)
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Basic
    @Column(name = "is_registration_number", nullable = false, insertable = true, updatable = true, length = 255)
    public String getIsRegistrationNumber() {
        return isRegistrationNumber;
    }

    public void setIsRegistrationNumber(String isRegistrationNumber) {
        this.isRegistrationNumber = isRegistrationNumber;
    }

    @Basic
    @Column(name = "is_name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getIsName() {
        return isName;
    }

    public void setIsName(String isName) {
        this.isName = isName;
    }

    @Basic
    @Column(name = "is_mobile_number", nullable = false, insertable = true, updatable = true, length = 255)
    public String getIsMobileNumber() {
        return isMobileNumber;
    }

    public void setIsMobileNumber(String isMobileNumber) {
        this.isMobileNumber = isMobileNumber;
    }

    @Basic
    @Column(name = "is_rfid", nullable = false, insertable = true, updatable = true, length = 50)
    public String getIsRFID() {
        return isRFID;
    }

    public void setIsRFID(String isRFID) {
        this.isRFID = isRFID;
    }

    @Basic
    @Column(name = "is_paid", nullable = false, insertable = true, updatable = true, length = 50)
    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    @Basic
    @Column(name = "auto_renewal", nullable = false, insertable = true, updatable = true, length = 50)
    public Integer getAutoRenewal() {
        return autoRenewal;
    }

    public void setAutoRenewal(Integer autoRenewal) {
        this.autoRenewal = autoRenewal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingPassMasterEntity that = (ParkingPassMasterEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (passType != null ? !passType.equals(that.passType) : that.passType != null) return false;
        if (numbers != null ? !numbers.equals(that.numbers) : that.numbers != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (vehicleType != null ? !vehicleType.equals(that.vehicleType) : that.vehicleType != null) return false;
        if (isActive != null ? !isActive.equals(that.isActive) : that.isActive != null) return false;
        if (isRegistrationNumber != null ? !isRegistrationNumber.equals(that.isRegistrationNumber) : that.isRegistrationNumber != null)
            return false;
        if (isName != null ? !isName.equals(that.isName) : that.isName != null) return false;
        if (isMobileNumber != null ? !isMobileNumber.equals(that.isMobileNumber) : that.isMobileNumber != null)
            return false;
        if (isRFID != null ? !isRFID.equals(that.isRFID) : that.isRFID != null) return false;
        if (isPaid != null ? !isPaid.equals(that.isPaid) : that.isPaid != null) return false;
        if (autoRenewal != null ? !autoRenewal.equals(that.autoRenewal) : that.autoRenewal != null) return false;

        return !(parking != null ? !parking.equals(that.parking) : that.parking != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (passType != null ? passType.hashCode() : 0);
        result = 31 * result + (numbers != null ? numbers.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (isRegistrationNumber != null ? isRegistrationNumber.hashCode() : 0);
        result = 31 * result + (isName != null ? isName.hashCode() : 0);
        result = 31 * result + (isMobileNumber != null ? isMobileNumber.hashCode() : 0);
        result = 31 * result + (isRFID != null ? isRFID.hashCode() : 0);
        result = 31 * result + (isPaid != null ? isPaid.hashCode() : 0);
        result = 31 * result + (autoRenewal != null ? autoRenewal.hashCode() : 0);
        result = 31 * result + (parking != null ? parking.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_id", referencedColumnName = "id", nullable = false)
    public ParkingEntity getParking() {
        return parking;
    }

    public void setParking(ParkingEntity parkingSubLotByParkingSubLotId) {
        this.parking = parkingSubLotByParkingSubLotId;
    }

    @OneToMany(mappedBy = "parkingPassMaster", fetch = FetchType.EAGER)
    public Set<ReceiptContentEntity> getReceiptContents() {
        return receiptContents;
    }

    public void setReceiptContents(Set<ReceiptContentEntity> receiptContentsById) {
        this.receiptContents = receiptContentsById;
    }
}
