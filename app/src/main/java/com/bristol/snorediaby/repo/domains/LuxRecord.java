package com.bristol.snorediaby.repo.domains;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Function: LuxRecord
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
@Entity(tableName = "LUX_RECORD")
public class LuxRecord {

    // 設置是否使ID自動累加
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    private int id;

    // 起始時間
    @ColumnInfo(name = "LUX_LIST", typeAffinity = ColumnInfo.BLOB)
    private byte[] luxList;

    public LuxRecord(byte[] luxList) {
        this.luxList = luxList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getLuxList() {
        return luxList;
    }

    public void setLuxList(byte[] luxList) {
        this.luxList = luxList;
    }

}
