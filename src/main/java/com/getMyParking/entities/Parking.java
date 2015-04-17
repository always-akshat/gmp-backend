package com.getMyParking.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 14/03/15.
 */
@Entity
@Table(name = "parkings")
public class Parking {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "parking_serial_number", nullable = false, length = 45)
    @JsonProperty("parking_serial_number")
    @NotNull
    private String parkingSerialNumber;

    @Column(name = "type", nullable = false, length = 45)
    @JsonProperty("type")
    @NotNull
    private String type;

    @Column(name = "phone_number", nullable = false, length = 45)
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Column(name = "registration_number", nullable = false, length = 45)
    @JsonProperty("registration_number")
    @NotNull
    private String registrationNumber;

    @Column(name = "checkin_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonProperty("check_in_time")
    @NotNull
    private DateTime checkInTime;

    @JsonProperty("checkout_time")
    @Column(name = "checkout_time")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime checkoutTime;

    @JsonProperty("parking_lot_id")
    @Column(name = "parking_lot_id")
    private Integer parkingLotId;

    @Column(name = "cost", nullable = true)
    @JsonProperty("cost")
    private Integer cost;

    @JsonProperty("valid_till_time")
    @Column(name = "valid_till_time")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime validTillTime;

    @JsonProperty("vehicle_status")
    @NotNull
    @Column(name = "status")
    private String status;

    public Parking() {
    }

    public void copy(Parking parking) {
        this.type = parking.getType();
        this.phoneNumber = parking.getPhoneNumber();
        this.registrationNumber = parking.getRegistrationNumber();
        this.checkInTime = parking.getCheckInTime();
        this.checkoutTime = parking.getCheckoutTime();
        this.cost = parking.getCost();
        this.validTillTime = parking.getValidTillTime();
        this.status = parking.getStatus();
        this.parkingSerialNumber = parking.getParkingSerialNumber();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public DateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(DateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public DateTime getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(DateTime checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public Integer getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Integer parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public DateTime getValidTillTime() {
        return validTillTime;
    }

    public void setValidTillTime(DateTime validTillTime) {
        this.validTillTime = validTillTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getParkingSerialNumber() {
        return parkingSerialNumber;
    }

    public void setParkingSerialNumber(String parkingSerialNumber) {
        this.parkingSerialNumber = parkingSerialNumber;
    }
}
