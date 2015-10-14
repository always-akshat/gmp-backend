package com.getMyParking.entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "session", schema = "", catalog = "get_my_parking_v2")
public class SessionEntity {
    private Integer id;
    private String authToken;
    private DateTime validTime;
    private UserB2BEntity userB2BEntity;

    public SessionEntity() {
    }

    public SessionEntity(String authToken, DateTime validTillTimestamp, UserB2BEntity userB2BEntity) {
        this.authToken = authToken;
        this.validTime = validTillTimestamp;
        this.userB2BEntity = userB2BEntity;
    }

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getValidTime() {
        return validTime;
    }

    public void setValidTime(DateTime validTime) {
        this.validTime = validTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntity that = (SessionEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (authToken != null ? !authToken.equals(that.authToken) : that.authToken != null) return false;
        return !(validTime != null ? !validTime.equals(that.validTime) : that.validTime != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (authToken != null ? authToken.hashCode() : 0);
        result = 31 * result + (validTime != null ? validTime.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UserB2BEntity getUserB2BEntity() {
        return userB2BEntity;
    }

    public void setUserB2BEntity(UserB2BEntity userB2BByUsername) {
        this.userB2BEntity = userB2BByUsername;
    }
}
