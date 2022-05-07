package com.bristol.snorediaby.repo.domain;

import android.content.Context;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Function: BaseExample
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
public abstract class BaseExample {

    protected Context context;

    protected List<LuxStorage> listLux;

    protected List<SnoreStorage> listSnore;

    protected AtomicInteger luxAlert;

    public BaseExample(Context context) {
        super();

        this.context = context;
        this.listLux = new ArrayList<>();
        this.listSnore = new ArrayList<>();
        this.luxAlert = new AtomicInteger(0);
    }

    protected abstract void run() throws Exception;

    protected abstract void makeDate() throws Exception;

    protected abstract void saveData() throws Exception;

    protected float randomLux(int x, int y) throws Exception {
        return (float) Math.random() * (x - y + 1) + y;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<LuxStorage> getListLux() {
        return listLux;
    }

    public void setListLux(List<LuxStorage> listLux) {
        this.listLux = listLux;
    }

    public List<SnoreStorage> getListSnore() {
        return listSnore;
    }

    public void setListSnore(List<SnoreStorage> listSnore) {
        this.listSnore = listSnore;
    }

    public AtomicInteger getLuxAlert() {
        return luxAlert;
    }

    public void setLuxAlert(AtomicInteger luxAlert) {
        this.luxAlert = luxAlert;
    }

}
