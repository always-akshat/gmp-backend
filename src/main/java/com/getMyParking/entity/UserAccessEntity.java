package com.getMyParking.entity;

import javax.persistence.*;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "user_access", schema = "", catalog = "get_my_parking_v2")
public class UserAccessEntity {
    private String name;
    private String userB2BUsername;
    private UserB2BEntity userB2BByUserB2BUsername;

    @Id
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "user_b2b_username", nullable = false, insertable = true, updatable = true, length = 255)
    public String getUserB2BUsername() {
        return userB2BUsername;
    }

    public void setUserB2BUsername(String userB2BUsername) {
        this.userB2BUsername = userB2BUsername;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccessEntity that = (UserAccessEntity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (userB2BUsername != null ? !userB2BUsername.equals(that.userB2BUsername) : that.userB2BUsername != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (userB2BUsername != null ? userB2BUsername.hashCode() : 0);
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
