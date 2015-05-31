package com.getMyParking.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "parking_pass", schema = "", catalog = "get_my_parking")
public class ParkingPassEntity {
    private int id;
    private String registrationNumber;
    private Timestamp validTime;
    private Collection<ParkingEventEntity> parkingEventsById;
    private ParkingPassMasterEntity parkingPassMasterByParkingPassMasterId;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "registration_number", nullable = false, insertable = true, updatable = true, length = 500)
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Basic
    @Column(name = "valid_time", nullable = false, insertable = true, updatable = true)
    public Timestamp getValidTime() {
        return validTime;
    }

    public void setValidTime(Timestamp validTime) {
        this.validTime = validTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingPassEntity that = (ParkingPassEntity) o;

        if (id != that.id) return false;
        if (registrationNumber != null ? !registrationNumber.equals(that.registrationNumber) : that.registrationNumber != null)
            return false;
        if (validTime != null ? !validTime.equals(that.validTime) : that.validTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (validTime != null ? validTime.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "parkingPassByParkingPassId")
    public Collection<ParkingEventEntity> getParkingEventsById() {
        return parkingEventsById;
    }

    public void setParkingEventsById(Collection<ParkingEventEntity> parkingEventsById) {
        this.parkingEventsById = parkingEventsById;
    }

    @ManyToOne
    @JoinColumn(name = "parking_pass_master_id", referencedColumnName = "id", nullable = false)
    public ParkingPassMasterEntity getParkingPassMasterByParkingPassMasterId() {
        return parkingPassMasterByParkingPassMasterId;
    }

    public void setParkingPassMasterByParkingPassMasterId(ParkingPassMasterEntity parkingPassMasterByParkingPassMasterId) {
        this.parkingPassMasterByParkingPassMasterId = parkingPassMasterByParkingPassMasterId;
    }
}
