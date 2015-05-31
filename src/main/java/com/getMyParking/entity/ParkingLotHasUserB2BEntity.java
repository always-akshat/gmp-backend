package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "parking_lot_has_user_b2b", schema = "", catalog = "get_my_parking")
public class ParkingLotHasUserB2BEntity {
    private int id;
    private ParkingLotEntity parkingLotByParkingLotId;
    private UserB2BEntity userB2BByUserB2BUsername;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingLotHasUserB2BEntity)) return false;

        ParkingLotHasUserB2BEntity that = (ParkingLotHasUserB2BEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", referencedColumnName = "id", nullable = false)
    public ParkingLotEntity getParkingLotByParkingLotId() {
        return parkingLotByParkingLotId;
    }

    public void setParkingLotByParkingLotId(ParkingLotEntity parkingLotByParkingLotId) {
        this.parkingLotByParkingLotId = parkingLotByParkingLotId;
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
