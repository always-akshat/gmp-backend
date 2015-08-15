package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "parking_lot_has_user_b2b", schema = "", catalog = "get_my_parking_v2")
public class ParkingLotHasUserB2BEntity {
    private int id;
    private int parkingSubLotId;
    private UserB2BEntity userB2B;

    public ParkingLotHasUserB2BEntity(int parkingSubLotId, UserB2BEntity userB2B) {
        this.parkingSubLotId = parkingSubLotId;
        this.userB2B = userB2B;
    }

    public ParkingLotHasUserB2BEntity() {
    }

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingLotHasUserB2BEntity entity = (ParkingLotHasUserB2BEntity) o;

        if (id != entity.id) return false;
        return parkingSubLotId == entity.parkingSubLotId;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + parkingSubLotId;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "user_b2b_username", referencedColumnName = "username", nullable = false)
    public UserB2BEntity getUserB2B() {
        return userB2B;
    }

    public void setUserB2B(UserB2BEntity userB2BByUserB2BUsername) {
        this.userB2B = userB2BByUserB2BUsername;
    }
}
