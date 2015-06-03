package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "parking_lot_has_user_b2b", schema = "", catalog = "get_my_parking")
public class ParkingLotHasUserB2BEntity {
    private int id;
    private int parkingLotId;
    private UserB2BEntity userB2BByUserB2BUsername;

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
    @Column(name = "parking_lot_id", nullable = false, insertable = true, updatable = true)
    public int getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(int parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public ParkingLotHasUserB2BEntity(int parkingLotId, UserB2BEntity userB2BByUserB2BUsername) {
        this.parkingLotId = parkingLotId;
        this.userB2BByUserB2BUsername = userB2BByUserB2BUsername;
    }

    public ParkingLotHasUserB2BEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingLotHasUserB2BEntity)) return false;

        ParkingLotHasUserB2BEntity that = (ParkingLotHasUserB2BEntity) o;

        if (id != that.id) return false;
        if (parkingLotId != that.parkingLotId) return false;
        if (userB2BByUserB2BUsername != null ? !userB2BByUserB2BUsername.equals(that.userB2BByUserB2BUsername) : that.userB2BByUserB2BUsername != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + parkingLotId;
        result = 31 * result + (userB2BByUserB2BUsername != null ? userB2BByUserB2BUsername.hashCode() : 0);
        return result;
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
