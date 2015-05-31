package com.getMyParking.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by rahulgupta.s on 31/05/15.
 */
@Entity
@Table(name = "session", schema = "", catalog = "get_my_parking")
public class SessionEntity {
    private int id;
    private String authToken;
    private Timestamp validTime;
    private UserB2BEntity userB2BByUsername;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "auth_token", nullable = false, insertable = true, updatable = true, length = 1000)
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Basic
    @Column(name = "valid_time", nullable = false, insertable = true, updatable = true)
    public Timestamp getValidTime() {
        return validTime;
    }

    public void setValidTime(Timestamp validTime) {
        this.validTime = validTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntity that = (SessionEntity) o;

        if (id != that.id) return false;
        if (authToken != null ? !authToken.equals(that.authToken) : that.authToken != null) return false;
        if (validTime != null ? !validTime.equals(that.validTime) : that.validTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (authToken != null ? authToken.hashCode() : 0);
        result = 31 * result + (validTime != null ? validTime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UserB2BEntity getUserB2BByUsername() {
        return userB2BByUsername;
    }

    public void setUserB2BByUsername(UserB2BEntity userB2BByUsername) {
        this.userB2BByUsername = userB2BByUsername;
    }
}
