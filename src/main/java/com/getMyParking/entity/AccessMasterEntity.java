package com.getMyParking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by karan on 13/10/15.
 */
@Entity
@Table(name = "access_master", schema = "", catalog = "get_my_parking_v2")
public class AccessMasterEntity {
    @NotNull
    private String accessTitle;

    @Id
    @Column(name = "access_title", nullable = false, insertable = true, updatable = true)
    public String getAccessTitle() {
        return accessTitle;
    }


    public void setAccessTitle(String accessTitle) {
        this.accessTitle = accessTitle;
    }
}
