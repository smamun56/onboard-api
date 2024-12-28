package com.hr.onboard.entity.enums;

public enum Role {
    ADMIN("ADMIN"),
    STAFF("STAFF"),
    NORMAL("NORMAL");
    private final String value;

    Role(String value) {
        this.value = value;
    }
    public String toString() {
        return value;
    }
}
