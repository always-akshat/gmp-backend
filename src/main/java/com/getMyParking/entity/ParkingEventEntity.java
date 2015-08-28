package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_event", schema = "", catalog = "get_my_parking_v2")
public class ParkingEventEntity {
    private Integer id;
    private String type;
    private String registrationNumber;
    private String mobileNumber;
    private String valetName;
    private DateTime eventTime;
    private String eventType;
    private BigDecimal cost;
    private String subLotType;
    private String serialNumber;
    private String shiftNumber;
    private DateTime updatedTime;
    private String operatorName;
    private int parkingLotId;
    private int parkingId;
    private int companyId;
    @JsonIgnore
    private ParkingSubLotEntity parkingSubLot;
    @JsonIgnore
    private ParkingPassEntity parkingPass;
    private Integer parkingSubLotId;
    private Integer parkingPassId;

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
    @Column(name = "operator_name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
    @Column(name = "parking_id", nullable = false, insertable = true, updatable = true)
    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    @Basic
    @Column(name = "company_id", nullable = false, insertable = true, updatable = true)
    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingEventEntity that = (ParkingEventEntity) o;

        if (id != that.id) return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (eventTime != null ? !eventTime.equals(that.eventTime) : that.eventTime != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null) return false;
        if (operatorName != null ? !operatorName.equals(that.operatorName) : that.operatorName != null) return false;
        if (registrationNumber != null ? !registrationNumber.equals(that.registrationNumber) : that.registrationNumber != null)
            return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (shiftNumber != null ? !shiftNumber.equals(that.shiftNumber) : that.shiftNumber != null) return false;
        if (subLotType != null ? !subLotType.equals(that.subLotType) : that.subLotType != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (updatedTime != null ? !updatedTime.equals(that.updatedTime) : that.updatedTime != null) return false;
        return !(valetName != null ? !valetName.equals(that.valetName) : that.valetName != null);

    }

    @Override
    public int hashCode() {
        int result = id;
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
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_sub_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingSubLotEntity getParkingSubLot() {
        return parkingSubLot;
    }

    public void setParkingSubLot(ParkingSubLotEntity parkingSubLotByParkingSubLotId) {
        this.parkingSubLot = parkingSubLotByParkingSubLotId;
    }

    @ManyToOne
    @JoinColumn(name = "parking_pass_id", referencedColumnName = "id")
    public ParkingPassEntity getParkingPass() {
        return parkingPass;
    }

    public void setParkingPass(ParkingPassEntity parkingPassByParkingPassId) {
        this.parkingPass = parkingPassByParkingPassId;
    }

    public void setParkingSubLotId(Integer parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
    }

    @Transient
    public Integer getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingPassId(Integer parkingPassId) {
        this.parkingPassId = parkingPassId;
    }

    @Transient
    public Integer getParkingPassId() {
        return parkingPassId;
    }
}
