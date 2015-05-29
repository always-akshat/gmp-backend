package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 30/05/15.
 */
@Entity
@Table(name = "parking_lot_has_user_b2b", schema = "", catalog = "get_my_parking")
@IdClass(ParkingLotHasUserB2BEntityPK.class)
public class ParkingLotHasUserB2BEntity {
    private int parkingLotId;
    private String userB2BUsername;
    private ParkingLotEntity parkingLotByParkingLotId;

    @Id
    @Column(name = "parking_lot_id", nullable = false, insertable = true, updatable = true)
    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Id
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

        ParkingLotHasUserB2BEntity that = (ParkingLotHasUserB2BEntity) o;

        if (parkingLotId != that.parkingLotId) return false;
        if (userB2BUsername != null ? !userB2BUsername.equals(that.userB2BUsername) : that.userB2BUsername != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = parkingLotId;
        result = 31 * result + (userB2BUsername != null ? userB2BUsername.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLotByParkingLotId() {
        return parkingLotByParkingLotId;
    }

    public void setParkingLotByParkingLotId(ParkingLotEntity parkingLotByParkingLotId) {
        this.parkingLotByParkingLotId = parkingLotByParkingLotId;
    }
}
