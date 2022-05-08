package com.bristol.snorediaby.repo.domains;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Function: SnoreRecord
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
@Entity(tableName = "SNORE_RECORD")
public class SnoreRecord {

    // 設置是否使ID自動累加
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    private int id;

    // 起始時間
    @ColumnInfo(name = "SNORE_LIST", typeAffinity = ColumnInfo.BLOB)
    private byte[] snoreList;

    public SnoreRecord(byte[] snoreList) {
        this.snoreList = snoreList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getSnoreList() {
        return snoreList;
    }

    public void setSnoreList(byte[] snoreList) {
        this.snoreList = snoreList;
    }

}
