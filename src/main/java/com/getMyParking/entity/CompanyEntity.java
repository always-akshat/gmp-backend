package com.getMyParking.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Created by rahulgupta.s on 13/08/15.
 */
@Entity
@Table(name = "company", schema = "", catalog = "get_my_parking_v3")
public class CompanyEntity {
    private Integer id;
    private String name;
    private String address;
    private String city;
    private String email;
    private String contactNumber;
    private String website;
    private Set<ParkingEntity> parkings;

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
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 500)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "address", nullable = false, insertable = true, updatable = true, length = 500)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "city", nullable = false, insertable = true, updatable = true, length = 500)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "email", nullable = true, insertable = true, updatable = true, length = 500)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "contact_number", nullable = true, insertable = true, updatable = true, length = 500)
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Basic
    @Column(name = "website", nullable = true, insertable = true, updatable = true, length = 500)
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyEntity that = (CompanyEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (contactNumber != null ? !contactNumber.equals(that.contactNumber) : that.contactNumber != null)
            return false;
        return !(website != null ? !website.equals(that.website) : that.website != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (contactNumber != null ? contactNumber.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
    public Set<ParkingEntity> getParkings() {
        return parkings;
    }

    public void setParkings(Set<ParkingEntity> parkingsById) {
        this.parkings = parkingsById;
    }
}
