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
    private String operatorName;
    private String mobileNumber;
    private Integer cost;
    private Integer isPaid;
    private Set<ParkingEventEntity> parkingEvents;
    private ParkingPassMasterEntity parkingPassMaster;

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
    @Column(name = "registration_number", nullable = false, insertable = true, updatable = true, length = 500)
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Basic
    @Column(name = "operator_name", nullable = false, insertable = true, updatable = true)
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    @Basic
    @Column(name = "mobile_number", nullable = false, insertable = true, updatable = true)
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobile_number) {
        this.mobileNumber = mobile_number;
    }

    @Basic
    @Column(name = "cost", nullable = false, insertable = true, updatable = true)
    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Basic
    @Column(name = "is_paid", nullable = false, insertable = true, updatable = true)
    public Integer getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Integer isPaid) {
        this.isPaid = isPaid;
    }

    @Basic
    @Column(name = "valid_time", nullable = false, insertable = true, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(DateTime validTime) {
        this.validTime = validTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingPassEntity)) return false;

        ParkingPassEntity that = (ParkingPassEntity) o;

        if (cost != null ? !cost.equals(that.cost) : that.cost != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (isPaid != null ? !isPaid.equals(that.isPaid) : that.isPaid != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null)
            return false;
        if (operatorName != null ? !operatorName.equals(that.operatorName) : that.operatorName != null) return false;
        if (parkingEvents != null ? !parkingEvents.equals(that.parkingEvents) : that.parkingEvents != null)
            return false;
        if (parkingPassMaster != null ? !parkingPassMaster.equals(that.parkingPassMaster) : that.parkingPassMaster != null)
            return false;
        if (registrationNumber != null ? !registrationNumber.equals(that.registrationNumber) : that.registrationNumber != null)
            return false;
        if (validTime != null ? !validTime.equals(that.validTime) : that.validTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (validTime != null ? validTime.hashCode() : 0);
        result = 31 * result + (operatorName != null ? operatorName.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (isPaid != null ? isPaid.hashCode() : 0);
        result = 31 * result + (parkingEvents != null ? parkingEvents.hashCode() : 0);
        result = 31 * result + (parkingPassMaster != null ? parkingPassMaster.hashCode() : 0);
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
