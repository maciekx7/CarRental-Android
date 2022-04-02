package com.wpam.carrental.model;

public class MakeCreation {
    String name;

    public MakeCreation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MakeCreation{" +
                "name='" + name + '\'' +
                '}';
    }
}
