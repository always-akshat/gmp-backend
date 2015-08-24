package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "user_access", schema = "", catalog = "get_my_parking_v2")
public class UserAccessEntity {
    @NotNull
    private String accessTitle;
    @JsonIgnore
    private UserB2BEntity userB2BEntity;

    @Id
    @Column(name = "access_title", nullable = false, insertable = true, updatable = true, length = 128)
    public String getAccessTitle() {
        return accessTitle;
    }

    public void setAccessTitle(String name) {
        this.accessTitle = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccessEntity that = (UserAccessEntity) o;

        return !(accessTitle != null ? !accessTitle.equals(that.accessTitle) : that.accessTitle != null);

    }

    @Override
    public int hashCode() {
        return accessTitle != null ? accessTitle.hashCode() : 0;
    }

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UserB2BEntity getUserB2BEntity() {
        return userB2BEntity;
    }

    public void setUserB2BEntity(UserB2BEntity userB2BByUserB2BUsername) {
        this.userB2BEntity = userB2BByUserB2BUsername;
    }
}
