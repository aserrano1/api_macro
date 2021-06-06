package com.dcatech.labviewer.macro.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "LVUSERVIEW")
public class LVUser implements Serializable, UserDetails {

    @Basic
    @Column(name = "STATUS")
    private String status;
    @Basic
    @Column(name = "PASSWORD")
    private String password;
    @Basic
    @Column(name = "ADDRESSID")
    private String addressid;
    @Basic
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Basic
    @Column(name = "LASTNAME")
    private String lastname;
    @Id
    @Column(name = "EMAIL")
    private String email;
    @Basic
    private String logonName;
    @Basic
    private String lastJobType;
    @Basic
    private String sysUserId;


    public String getLastJobType() {
        return lastJobType;
    }

    public void setLastJobType(String lastJobType) {
        this.lastJobType = lastJobType;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getLogonName() {
        return logonName;
    }

    public void setLogonName(String logonName) {
        this.logonName = logonName;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (this.status.equals("ACTIVO")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LVUser lvUser = (LVUser) o;

        if (!status.equals(lvUser.status)) return false;
        if (!password.equals(lvUser.password)) return false;
        if (!addressid.equals(lvUser.addressid)) return false;
        if (!firstname.equals(lvUser.firstname)) return false;
        if (!lastname.equals(lvUser.lastname)) return false;
        return email.equals(lvUser.email);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + addressid.hashCode();
        result = 31 * result + firstname.hashCode();
        // result = 31 * result + lastname.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
