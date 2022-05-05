package com.bristol.snorediaby.domain;

import java.io.Serializable;

public class LuxStorage implements Serializable {

    private final String date;
    private float lux;

    public LuxStorage(String date, float lux) {
        this.date = date;
        this.lux = lux;
    }

    public String getDate() {
        return date;
    }

    public float getLux() {
        return lux;
    }

    public void updateLux(float x) {
        this.lux = x;
    }

}
