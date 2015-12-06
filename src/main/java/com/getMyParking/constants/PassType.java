package com.getMyParking.constants;

/**
 * Created by rahulgupta.s on 06/12/15.
 */
public enum PassType {
    MONTH("MONTH"),
    WEEK("WEEK"),
    DAY("DAY"),
    HOUR("HOUR");

    private String value;
    PassType(String val) {
        this.value = val;
    }
}
