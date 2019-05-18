package com.srihari.ruasportal;

public class Notes_adapter {

    String Key;
    String value;

    public Notes_adapter() {
    }

    public Notes_adapter(String key, String value) {
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
