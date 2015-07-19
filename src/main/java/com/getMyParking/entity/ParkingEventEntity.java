package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "parking_event", schema = "", catalog = "get_my_parking")
public class ParkingEventEntity {
    private int id;
    @NotNull
    private String vehicleType;
    @NotNull
    private String type;
    @NotNull
    private String registrationNumber;
    @NotNull
    private DateTime eventTime;
    @NotNull
    private String eventType;
    @NotNull
    private BigDecimal cost;
    @NotNull
    private String serialNumber;
    @NotNull
    private String shiftNumber;
    private DateTime updatedTime;
    @JsonIgnore
    private ParkingLotEntity parkingLotByParkingLotId;
    @JsonIgnore
    private ParkingPassEntity parkingPassByParkingPassId;
    private int parkingLotId;
    private int parkingPassId;

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
    @Column(name = "vehicle_type", nullable = false, insertable = true, updatable = true, length = 45)
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLotByParkingLotId() {
        return parkingLotByParkingLotId;
    }

    public void setParkingLotByParkingLotId(ParkingLotEntity parkingLotByParkingLotId) {
        this.parkingLotByParkingLotId = parkingLotByParkingLotId;
    }

    @ManyToOne
    @JoinColumn(name = "parking_pass_id", referencedColumnName = "id")
    public ParkingPassEntity getParkingPassByParkingPassId() {
        return parkingPassByParkingPassId;
    }

    public void setParkingPassByParkingPassId(ParkingPassEntity parkingPassByParkingPassId) {
        this.parkingPassByParkingPassId = parkingPassByParkingPassId;
    }

    @Transient
    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Transient
    public int getParkingPassId() {
        return parkingPassId;
    }

    public void setParkingPassId(int parkingPassId) {
        this.parkingPassId = parkingPassId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingEventEntity)) return false;

        ParkingEventEntity that = (ParkingEventEntity) o;

        if (id != that.id) return false;
        if (parkingLotId != that.parkingLotId) return false;
        if (parkingPassId != that.parkingPassId) return false;
        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (eventTime != null ? !eventTime.equals(that.eventTime) : that.eventTime != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        if (parkingLotByParkingLotId != null ? !parkingLotByParkingLotId.equals(that.parkingLotByParkingLotId) : that.parkingLotByParkingLotId != null)
            return false;
        if (parkingPassByParkingPassId != null ? !parkingPassByParkingPassId.equals(that.parkingPassByParkingPassId) : that.parkingPassByParkingPassId != null)
            return false;
        if (registrationNumber != null ? !registrationNumber.equals(that.registrationNumber) : that.registrationNumber != null)
            return false;
        if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
        if (shiftNumber != null ? !shiftNumber.equals(that.shiftNumber) : that.shiftNumber != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (updatedTime != null ? !updatedTime.equals(that.updatedTime) : that.updatedTime != null) return false;
        if (vehicleType != null ? !vehicleType.equals(that.vehicleType) : that.vehicleType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (vehicleType != null ? vehicleType.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (eventTime != null ? eventTime.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
        result = 31 * result + (shiftNumber != null ? shiftNumber.hashCode() : 0);
        result = 31 * result + (updatedTime != null ? updatedTime.hashCode() : 0);
        result = 31 * result + (parkingLotByParkingLotId != null ? parkingLotByParkingLotId.hashCode() : 0);
        result = 31 * result + (parkingPassByParkingPassId != null ? parkingPassByParkingPassId.hashCode() : 0);
        result = 31 * result + parkingLotId;
        result = 31 * result + parkingPassId;
        return result;
    }
}
