package com.wpam.carrental.model.enums;

public enum Role {
    admin("admin"),
    user("user");

    private String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
