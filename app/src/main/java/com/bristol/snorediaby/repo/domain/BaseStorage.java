package com.bristol.snorediaby.repo.domain;

/**
 * Function: BaseStorage
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
public class BaseStorage {

    public String date;

    public BaseStorage(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
