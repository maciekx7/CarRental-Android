package com.wpam.carrental.model.enums;

public enum Fuel {
    ON("ON"),
    PB("PB"),
    GAZ("GAZ");

    private String code;

    Fuel(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
