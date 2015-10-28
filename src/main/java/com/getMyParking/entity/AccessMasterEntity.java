package com.getMyParking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by rahulgupta.s on 28/10/15.
 */
@Entity
@Table(name = "access_master", schema = "", catalog = "get_my_parking_v2")
public class AccessMasterEntity {

    private String accessTitle;
    @JsonIgnore
    private Set<UserB2BEntity> users;

    @Id
    @Column(name = "access_title")
    public String getAccessTitle() {
        return accessTitle;
    }

    public void setAccessTitle(String accessTitle) {
        this.accessTitle = accessTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessMasterEntity that = (AccessMasterEntity) o;

        if (accessTitle != null ? !accessTitle.equals(that.accessTitle) : that.accessTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return accessTitle != null ? accessTitle.hashCode() : 0;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "userAccesses")
    public Set<UserB2BEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserB2BEntity> users) {
        this.users = users;
    }
}