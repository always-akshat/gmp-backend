package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 09/09/15.
 */
@Entity
@Table(name = "parking_lot_foc_reasons", schema = "", catalog = "get_my_parking_v2")
@IdClass(ParkingLotFocReasonsEntityPK.class)
public class ParkingLotFocReasonsEntity {
    private String focReasonsTitle;
    @JsonIgnore
    private ParkingLotEntity parkingLot;

    @Id
    @Column(name = "foc_reasons_title", nullable = false, insertable = true, updatable = true, length = 255)
    public String getFocReasonsTitle() {
        return focReasonsTitle;
    }

    public void setFocReasonsTitle(String focReasonsTitle) {
        this.focReasonsTitle = focReasonsTitle;
    }

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLotEntity parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingLotFocReasonsEntity)) return false;

        ParkingLotFocReasonsEntity that = (ParkingLotFocReasonsEntity) o;

        if (focReasonsTitle != null ? !focReasonsTitle.equals(that.focReasonsTitle) : that.focReasonsTitle != null)
            return false;
        if (parkingLot != null ? !parkingLot.equals(that.parkingLot) : that.parkingLot != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = focReasonsTitle != null ? focReasonsTitle.hashCode() : 0;
        result = 31 * result + (parkingLot != null ? parkingLot.hashCode() : 0);
        return result;
    }
}
