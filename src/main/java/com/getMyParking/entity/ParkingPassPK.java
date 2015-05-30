package com.getMyParking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by rahulgupta.s on 30/05/15.
 */
public class ParkingPassPK implements Serializable {
    private int id;
    private int parkingPassMasterId;

    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "parking_pass_master_id", nullable = false, insertable = true, updatable = true)
    @Id
    public int getParkingPassMasterId() {
        return parkingPassMasterId;
    }

    public void setParkingPassMasterId(int parkingPassMasterId) {
        this.parkingPassMasterId = parkingPassMasterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingPassPK that = (ParkingPassPK) o;

        if (id != that.id) return false;
        if (parkingPassMasterId != that.parkingPassMasterId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + parkingPassMasterId;
        return result;
    }
}
