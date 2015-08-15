package com.getMyParking.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "user_b2b", schema = "", catalog = "get_my_parking_v2")
public class UserB2BEntity {
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String name;
    private String contactNumber;
    private Set<ParkingLotHasUserB2BEntity> parkingSubLots;
    @NotNull
    private Set<UserAccessEntity> userAccesses;

    @Id
    @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 1000)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "contact_number", nullable = false, insertable = true, updatable = true, length = 255)
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserB2BEntity that = (UserB2BEntity) o;

        if (contactNumber != null ? !contactNumber.equals(that.contactNumber) : that.contactNumber != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (contactNumber != null ? contactNumber.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userB2B")
    public Set<ParkingLotHasUserB2BEntity> getParkingSubLots() {
        return parkingSubLots;
    }

    public void setParkingSubLots(Set<ParkingLotHasUserB2BEntity> parkingLotHasUserB2BsByUsername) {
        this.parkingSubLots = parkingLotHasUserB2BsByUsername;
    }

    @OneToMany(mappedBy = "userB2BEntity")
    public Set<UserAccessEntity> getUserAccesses() {
        return userAccesses;
    }

    public void setUserAccesses(Set<UserAccessEntity> userAccessesByUsername) {
        this.userAccesses = userAccessesByUsername;
    }
}
