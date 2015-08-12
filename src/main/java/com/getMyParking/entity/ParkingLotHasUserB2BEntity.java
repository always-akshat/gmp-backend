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
    private String userB2BUsername;
    private ParkingSubLotEntity parkingSubLotByParkingSubLotId;
    private UserB2BEntity userB2BByUserB2BUsername;

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

    @Basic
    @Column(name = "user_b2b_username", nullable = false, insertable = true, updatable = true, length = 255)
    public String getUserB2BUsername() {
        return userB2BUsername;
    }

    public void setUserB2BUsername(String userB2BUsername) {
        this.userB2BUsername = userB2BUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingLotHasUserB2BEntity entity = (ParkingLotHasUserB2BEntity) o;

        if (id != entity.id) return false;
        if (parkingSubLotId != entity.parkingSubLotId) return false;
        if (userB2BUsername != null ? !userB2BUsername.equals(entity.userB2BUsername) : entity.userB2BUsername != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + parkingSubLotId;
        result = 31 * result + (userB2BUsername != null ? userB2BUsername.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_sub_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingSubLotEntity getParkingSubLotByParkingSubLotId() {
        return parkingSubLotByParkingSubLotId;
    }

    public void setParkingSubLotByParkingSubLotId(ParkingSubLotEntity parkingSubLotByParkingSubLotId) {
        this.parkingSubLotByParkingSubLotId = parkingSubLotByParkingSubLotId;
    }

    @ManyToOne
    @JoinColumn(name = "user_b2b_username", referencedColumnName = "username", nullable = false)
    public UserB2BEntity getUserB2BByUserB2BUsername() {
        return userB2BByUserB2BUsername;
    }

    public void setUserB2BByUserB2BUsername(UserB2BEntity userB2BByUserB2BUsername) {
        this.userB2BByUserB2BUsername = userB2BByUserB2BUsername;
    }
}
