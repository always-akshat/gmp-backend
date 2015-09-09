package com.getMyParking.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by rahulgupta.s on 09/09/15.
 */
public class ParkingLotFocReasonsEntityPK implements Serializable {
    private String focReasonsTitle;
    private int parkingLotId;

    @Column(name = "foc_reasons_title", nullable = false, insertable = true, updatable = true, length = 255)
    @Id
    public String getFocReasonsTitle() {
        return focReasonsTitle;
    }

    public void setFocReasonsTitle(String focReasonsTitle) {
        this.focReasonsTitle = focReasonsTitle;
    }

    @Column(name = "parking_lot_id", nullable = false, insertable = true, updatable = true)
    @Id
    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParkingLotFocReasonsEntityPK that = (ParkingLotFocReasonsEntityPK) o;

        if (parkingLotId != that.parkingLotId) return false;
        if (focReasonsTitle != null ? !focReasonsTitle.equals(that.focReasonsTitle) : that.focReasonsTitle != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = focReasonsTitle != null ? focReasonsTitle.hashCode() : 0;
        result = 31 * result + parkingLotId;
        return result;
    }
}
