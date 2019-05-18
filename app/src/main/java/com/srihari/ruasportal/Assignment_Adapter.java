package com.srihari.ruasportal;

public class Assignment_Adapter {

    String Key;
    String value;

    public Assignment_Adapter() {
    }

    public Assignment_Adapter(String key, String value) {
        Key = key;
        this.value = value;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
