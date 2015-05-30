package com.getMyParking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by rahulgupta.s on 30/05/15.
 */
public class ParkingLotHasUserB2BPK implements Serializable {
    private int parkingLotId;
    private String userB2BUsername;

    @Column(name = "parking_lot_id", nullable = false, insertable = true, updatable = true)
    @Id
    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Column(name = "user_b2b_username", nullable = false, insertable = true, updatable = true, length = 255)
    @Id
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

        ParkingLotHasUserB2BPK that = (ParkingLotHasUserB2BPK) o;

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
}
