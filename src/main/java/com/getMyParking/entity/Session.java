package com.getMyParking.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by rahulgupta.s on 30/05/15.
 */
@Entity
@Table(name = "session", schema = "", catalog = "get_my_parking")
public class Session {
    private int id;
    private String username;
    private String authToken;
    private Timestamp validTime;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, insertable = true, updatable = true, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

        Session that = (Session) o;

        if (id != that.id) return false;
        if (authToken != null ? !authToken.equals(that.authToken) : that.authToken != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (validTime != null ? !validTime.equals(that.validTime) : that.validTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (authToken != null ? authToken.hashCode() : 0);
        result = 31 * result + (validTime != null ? validTime.hashCode() : 0);
        return result;
    }
}
