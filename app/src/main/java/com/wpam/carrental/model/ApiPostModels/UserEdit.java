package com.wpam.carrental.model.ApiPostModels;

public class UserEdit {
    String password;
    String currentPassword;
    String name;
    String lastName;
    int phone;

    public UserEdit(String password, String currentPassword, String name, String lastName, int phone) {
        this.password = password;
        this.currentPassword = currentPassword;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
