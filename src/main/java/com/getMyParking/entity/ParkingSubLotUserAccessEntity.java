package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 16/08/15.
 */
@Entity
@Table(name = "parking_sub_lot_user_access", schema = "", catalog = "get_my_parking_v2")
public class ParkingSubLotUserAccessEntity {
    private int id;
    private int parkingSubLotId;
    private UserB2BEntity userB2B;
    private int parkingLotId;
    private int parkingId;
    private int companyId;

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
    @Column(name = "parking_sub_lot_id", nullable = false, insertable = true, updatable = true)
    public int getParkingSubLotId() {
        return parkingSubLotId;
    }

    public void setParkingSubLotId(int parkingSubLotId) {
        this.parkingSubLotId = parkingSubLotId;
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

        ParkingSubLotUserAccessEntity that = (ParkingSubLotUserAccessEntity) o;

        if (id != that.id) return false;
        if (parkingSubLotId != that.parkingSubLotId) return false;
        if (parkingLotId != that.parkingLotId) return false;
        if (parkingId != that.parkingId) return false;
        if (companyId != that.companyId) return false;
        return !(userB2B != null ? !userB2B.equals(that.userB2B) : that.userB2B != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + parkingSubLotId;
        result = 31 * result + (userB2B != null ? userB2B.hashCode() : 0);
        result = 31 * result + parkingLotId;
        result = 31 * result + parkingId;
        result = 31 * result + companyId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "user_b2b", referencedColumnName = "username", nullable = false)
    public UserB2BEntity getUserB2B() {
        return userB2B;
    }

    public void setUserB2B(UserB2BEntity userB2B) {
        this.userB2B = userB2B;
    }
}
