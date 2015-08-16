package com.getMyParking.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_pass", schema = "", catalog = "get_my_parking_v2")
public class ParkingPassEntity {
    private Integer id;
    private String registrationNumber;
    private DateTime validTime;
    private Set<ParkingEventEntity> parkingEvents;
    private ParkingPassMasterEntity parkingPassMaster;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    public DateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(DateTime validTime) {
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

    @OneToMany(mappedBy = "parkingPass")
    public Set<ParkingEventEntity> getParkingEvents() {
        return parkingEvents;
    }

    public void setParkingEvents(Set<ParkingEventEntity> parkingEventsById) {
        this.parkingEvents = parkingEventsById;
    }

    @ManyToOne
    @JoinColumn(name = "parking_pass_master_id", referencedColumnName = "id", nullable = false)
    public ParkingPassMasterEntity getParkingPassMaster() {
        return parkingPassMaster;
    }

    public void setParkingPassMaster(ParkingPassMasterEntity parkingPassMasterByParkingPassMasterId) {
        this.parkingPassMaster = parkingPassMasterByParkingPassMasterId;
    }
}
