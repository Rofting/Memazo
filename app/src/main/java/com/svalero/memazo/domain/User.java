package com.svalero.memazo.domain;

import java.io.Serializable;

public class User implements Serializable {

    private long id;
    private String name;
    private String email;
    private String password;
    private String birthDate;
    private String avatar;
    private String phone;
    private String sex;
    private Boolean active;

    public User() {
    }

    public User(String name, String email, String password, String birthDate, String avatar, String phone, String sex, Boolean active) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.avatar = avatar;
        this.phone = phone;
        this.sex = sex;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}