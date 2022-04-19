package com.wpam.carrental.model;

public class APIResultMessageBasic {
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public APIResultMessageBasic(String message) {
        this.message = message;
    }
}
