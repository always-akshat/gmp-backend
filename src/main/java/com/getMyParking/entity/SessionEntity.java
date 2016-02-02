package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "session", schema = "", catalog = "get_my_parking_v3")
public class SessionEntity {
    private Integer id;
    private String authToken;
    private DateTime validTime;
    private UserB2BEntity userB2BEntity;
    private DateTime lastTransactionTime;
    private DateTime lastAccessTime;
    private String deviceId;
    private Integer lastAccessedParkingLotId;
    private Integer transactionCount;

    public SessionEntity() {
    }

    public SessionEntity(String authToken, DateTime validTillTimestamp, UserB2BEntity userB2BEntity, String deviceId) {
        this.authToken = authToken;
        this.validTime = validTillTimestamp;
        this.userB2BEntity = userB2BEntity;
        this.lastAccessTime = DateTime.now();
        this.deviceId = deviceId;
        this.transactionCount = 0;
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

    @Basic
    @Column(name = "last_transaction_time", nullable = false, insertable = true, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getLastTransactionTime() {
        return lastTransactionTime;
    }

    public void setLastTransactionTime(DateTime lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }

    @Basic
    @Column(name = "last_access_time", nullable = false, insertable = true, updatable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Basic
    @Column(name = "device_id", nullable = false, insertable = true, updatable = true, length = 255)
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Basic
    @Column(name = "last_accessed_parking_lot_id", nullable = false, insertable = true, updatable = true)
    public Integer getLastAccessedParkingLotId() {
        return lastAccessedParkingLotId;
    }

    public void setLastAccessedParkingLotId(Integer lastAccessedParkingLotId) {
        this.lastAccessedParkingLotId = lastAccessedParkingLotId;
    }

    @Basic
    @Column(name = "transaction_count", nullable = false, insertable = true, updatable = true)
    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntity that = (SessionEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (authToken != null ? !authToken.equals(that.authToken) : that.authToken != null) return false;
        if (validTime != null ? !validTime.equals(that.validTime) : that.validTime != null) return false;
        if (lastTransactionTime != null ? !lastTransactionTime.equals(that.lastTransactionTime) : that.lastTransactionTime != null)
            return false;
        if (lastAccessTime != null ? !lastAccessTime.equals(that.lastAccessTime) : that.lastAccessTime != null)
            return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        if (lastAccessedParkingLotId != null ? !lastAccessedParkingLotId.equals(that.lastAccessedParkingLotId) : that.lastAccessedParkingLotId != null)
            return false;
        return !(transactionCount != null ? !transactionCount.equals(that.transactionCount) : that.transactionCount != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (authToken != null ? authToken.hashCode() : 0);
        result = 31 * result + (validTime != null ? validTime.hashCode() : 0);
        result = 31 * result + (lastTransactionTime != null ? lastTransactionTime.hashCode() : 0);
        result = 31 * result + (lastAccessTime != null ? lastAccessTime.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (lastAccessedParkingLotId != null ? lastAccessedParkingLotId.hashCode() : 0);
        result = 31 * result + (transactionCount != null ? transactionCount.hashCode() : 0);
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
