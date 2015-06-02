package com.getMyParking.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "user_b2b", schema = "", catalog = "get_my_parking")
public class UserB2BEntity {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    private String contactNumber;
    @NotNull
    private String role;
    private Collection<ParkingLotHasUserB2BEntity> parkingLotHasUserB2BsByUsername;

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
    @Column(name = "name", nullable = true, insertable = true, updatable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "contact_number", nullable = true, insertable = true, updatable = true, length = 255)
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Basic
    @Column(name = "role", nullable = true, insertable = true, updatable = true, length = 255)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (contactNumber != null ? contactNumber.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "userB2BByUserB2BUsername", fetch = FetchType.EAGER)
    public Collection<ParkingLotHasUserB2BEntity> getParkingLotHasUserB2BsByUsername() {
        return parkingLotHasUserB2BsByUsername;
    }

    public void setParkingLotHasUserB2BsByUsername(Collection<ParkingLotHasUserB2BEntity> parkingLotHasUserB2BsByUsername) {
        this.parkingLotHasUserB2BsByUsername = parkingLotHasUserB2BsByUsername;
    }
}
