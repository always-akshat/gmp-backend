package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_event", schema = "", catalog = "get_my_parking_v2")
public class ParkingEventEntity implements Comparable<ParkingEventEntity>{
    private BigInteger id;
    @NotNull
    private String type;
    @NotNull
    private String registrationNumber;
    private String mobileNumber;
    private String valetName;
    @NotNull
    private DateTime eventTime;
    @NotNull
    private String eventType;
    @NotNull
    private BigDecimal cost;
    @NotNull
    private String subLotType;
    @NotNull
    private String serialNumber;
    private String shiftNumber;
    private DateTime updatedTime;
    private String operatorName;
    @NotNull
    private Integer parkingLotId;
    @NotNull
    private Integer parkingId;
    @NotNull
    private Integer companyId;
    private String special;
    private String focReason;
    private ParkingPassEntity parkingPass;
    @JsonProperty
    private Integer parkingSubLotId;
    @JsonProperty
    private Integer parkingPassId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Basic
    @Column(name = "type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "registration_number", nullable = false, insertable = true, updatable = true, length = 45)
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Basic
    @Column(name = "mobile_number", nullable = true, insertable = true, updatable = true, length = 45)
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Basic
    @Column(name = "valet_name", nullable = true, insertable = true, updatable = true, length = 255)
    public String getValetName() {
        return valetName;
    }

    public void setValetName(String valetName) {
        this.valetName = valetName;
    }

    @Basic
    @Column(name = "event_time", nullable = false, insertable = true, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    @Basic
    @Column(name = "event_type", nullable = false, insertable = true, updatable = true, length = 500)
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Basic
    @Column(name = "cost", nullable = false, insertable = true, updatable = true, precision = 2)
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "sub_lot_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getSubLotType() {
        return subLotType;
    }

    public void setSubLotType(String subLotType) {
        this.subLotType = subLotType;
    }

    @Basic
    @Column(name = "serial_number", nullable = false, insertable = true, updatable = true, length = 500)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Basic
    @Column(name = "shift_number", nullable = false, insertable = true, updatable = true, length = 500)
    public String getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(String shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    @Basic
    @Column(name = "updated_time", nullable = false, insertable = true, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(DateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Basic
    @Column(name = "parking_sub_lot_id", nullable = false, insertable = true, updatable = true)
    public Integer getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(Integer parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    @Basic
    @Column(name = "operator_name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Basic
    @Column(name = "parking_lot_id", nullable = false, insertable = true, updatable = true)
    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Basic
    @Column(name = "parking_id", nullable = false, insertable = true, updatable = true)
    public Integer getParkingId() {
        return parkingId;
    }

    public void setParkingId(Integer parkingId) {
        this.parkingId = parkingId;
    }

    @Basic
    @Column(name = "company_id", nullable = false, insertable = true, updatable = true)
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Basic
    @Column(name = "special", nullable = false, insertable = true, updatable = true)
    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    @Basic
    @Column(name = "foc_reason", nullable = false, insertable = true, updatable = true)
    public String getFocReason() {
        return focReason;
    }

    public void setFocReason(String focReason) {
        this.focReason = focReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingEventEntity that = (ParkingEventEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (registrationNumber != null ? !registrationNumber.equals(that.registrationNumber) : that.registrationNumber != null)
            return false;
        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null) return false;
        if (valetName != null ? !valetName.equals(that.valetName) : that.valetName != null) return false;
        if (eventTime != null ? !eventTime.equals(that.eventTime) : that.eventTime != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (subLotType != null ? !subLotType.equals(that.subLotType) : that.subLotType != null) return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (shiftNumber != null ? !shiftNumber.equals(that.shiftNumber) : that.shiftNumber != null) return false;
        if (updatedTime != null ? !updatedTime.equals(that.updatedTime) : that.updatedTime != null) return false;
        if (operatorName != null ? !operatorName.equals(that.operatorName) : that.operatorName != null) return false;
        if (parkingLotId != null ? !parkingLotId.equals(that.parkingLotId) : that.parkingLotId != null) return false;
        if (parkingId != null ? !parkingId.equals(that.parkingId) : that.parkingId != null) return false;
        if (companyId != null ? !companyId.equals(that.companyId) : that.companyId != null) return false;
        if (special != null ? !special.equals(that.special) : that.special != null) return false;
        if (focReason != null ? !focReason.equals(that.focReason) : that.focReason != null) return false;
        if (parkingSubLotId != null ? !parkingSubLotId.equals(that.parkingSubLotId) : that.parkingSubLotId != null)
            return false;
        return !(parkingPassId != null ? !parkingPassId.equals(that.parkingPassId) : that.parkingPassId != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (valetName != null ? valetName.hashCode() : 0);
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (subLotType != null ? subLotType.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (shiftNumber != null ? shiftNumber.hashCode() : 0);
        result = 31 * result + (updatedTime != null ? updatedTime.hashCode() : 0);
        result = 31 * result + (operatorName != null ? operatorName.hashCode() : 0);
        result = 31 * result + (parkingLotId != null ? parkingLotId.hashCode() : 0);
        result = 31 * result + (parkingId != null ? parkingId.hashCode() : 0);
        result = 31 * result + (companyId != null ? companyId.hashCode() : 0);
        result = 31 * result + (special != null ? special.hashCode() : 0);
        result = 31 * result + (focReason != null ? focReason.hashCode() : 0);
        result = 31 * result + (parkingSubLotId != null ? parkingSubLotId.hashCode() : 0);
        result = 31 * result + (parkingPassId != null ? parkingPassId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_pass_id", referencedColumnName = "id")
    public ParkingPassEntity getParkingPass() {
        return parkingPass;
    }

    public void setParkingPass(ParkingPassEntity parkingPassByParkingPassId) {
        this.parkingPass = parkingPassByParkingPassId;
    }

    @Transient
    public Integer getParkingPassId() {
        return parkingPassId;
    }

    public void setParkingPassId(Integer parkingPassId) {
        this.parkingPassId = parkingPassId;
    }

    public void copy(ParkingEventEntity parkingEvent) {
        if (parkingEvent.getType() != null) type = parkingEvent.getType();
        if (parkingEvent.getRegistrationNumber() != null)
            registrationNumber = parkingEvent.getRegistrationNumber();
        if (parkingEvent.getMobileNumber() != null) mobileNumber = parkingEvent.getMobileNumber();
        if (parkingEvent.getValetName() != null) valetName = parkingEvent.getValetName();
        if (parkingEvent.getEventTime() != null) eventTime = parkingEvent.getEventTime();
        if (parkingEvent.getEventType() != null) eventType = parkingEvent.getEventType();
        if (parkingEvent.getCost() != null) cost = parkingEvent.getCost();
        if (parkingEvent.getSubLotType() != null) subLotType = parkingEvent.getSubLotType();
        if (parkingEvent.getSerialNumber() != null) serialNumber = parkingEvent.getSerialNumber();
        if (parkingEvent.getShiftNumber() != null) shiftNumber = parkingEvent.getShiftNumber();
        if (parkingEvent.getOperatorName() != null) operatorName = parkingEvent.getOperatorName();
        if (parkingEvent.getCompanyId() != null) companyId = parkingEvent.getCompanyId();
        if (parkingEvent.getParkingLotId() != null) parkingLotId = parkingEvent.getParkingLotId();
        if (parkingEvent.getParkingId() != null) parkingId = parkingEvent.getParkingId();
        if (parkingEvent.getFocReason() != null) focReason = parkingEvent.getFocReason();
        if (parkingEvent.getSpecial() != null) special = parkingEvent.getSpecial();
    }

    @Override
    public int compareTo(ParkingEventEntity that) {
        return this.getEventType().compareTo(that.getEventType());
    }
}
