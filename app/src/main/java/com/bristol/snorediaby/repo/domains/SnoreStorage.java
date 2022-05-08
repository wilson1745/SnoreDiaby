package com.bristol.snorediaby.repo.domains;

import java.io.Serializable;

public class SnoreStorage implements Serializable {

    private final String date;
    private final int snore;
    private final int OSA;

    public SnoreStorage(String date, int snore, int OSA) {
        this.date = date;
        this.snore = snore;
        this.OSA = OSA;
    }

    public String getDate() {
        return date;
    }

    public int getSnore() {
        return snore;
    }

    public int getOSA() {
        return OSA;
    }

}
