package com.wpam.carrental.model.enums;

public enum Role {
    ADMIN("admin"),
    USER("user");

    private String code;

    Role(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
