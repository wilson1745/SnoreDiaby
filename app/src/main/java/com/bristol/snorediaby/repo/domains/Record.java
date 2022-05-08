package com.bristol.snorediaby.repo.domains;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Function: Records
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
@Entity(tableName = "RECORD")
public class Record {

    // 設置是否使ID自動累加
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    private int id;

    // 起始時間
    @ColumnInfo(name = "START_TIME")
    private String startTime;

    //起始時間
    @ColumnInfo(name = "END_TIME")
    private String endTime;

    //總睡眠小時
    @ColumnInfo(name = "SLEEP_HOUR")
    private double sleepHour;

    //睡眠起始時間(小時幾點)
    @ColumnInfo(name = "TIME_OF_SLEEP")
    private double timeOfSleep;

    //打呼次數
    @ColumnInfo(name = "SNORE_TIMES")
    private int snoreTimes;

    //打呼百分比
    @ColumnInfo(name = "SNORE_PER")
    private String snorePer;

    //亮度警告
    @ColumnInfo(name = "LUX_ALERT")
    private int luxAlert;

    //照度百分比
    @ColumnInfo(name = "LUX_PER")
    private String luxPer;

    //呼吸中止次數
    @ColumnInfo(name = "OSA_TIMES")
    private String osaTimes;

    //日記
    @ColumnInfo(name = "DIARY")
    private String diary;

    public Record(String startTime, String endTime, double sleepHour, double timeOfSleep, int snoreTimes,
        String snorePer, int luxAlert, String luxPer, String osaTimes, String diary) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sleepHour = sleepHour;
        this.timeOfSleep = timeOfSleep;
        this.snoreTimes = snoreTimes;
        this.snorePer = snorePer;
        this.luxAlert = luxAlert;
        this.luxPer = luxPer;
        this.osaTimes = osaTimes;
        this.diary = diary;
    }

    // 如果要使用多形的建構子，必須加入@Ignore
    @Ignore
    public Record(int id, String startTime, String endTime, double sleepHour, double timeOfSleep, int snoreTimes,
        String snorePer, int luxAlert, String luxPer, String osaTimes, String diary) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sleepHour = sleepHour;
        this.timeOfSleep = timeOfSleep;
        this.snoreTimes = snoreTimes;
        this.snorePer = snorePer;
        this.luxAlert = luxAlert;
        this.luxPer = luxPer;
        this.osaTimes = osaTimes;
        this.diary = diary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getSleepHour() {
        return sleepHour;
    }

    public void setSleepHour(double sleepHour) {
        this.sleepHour = sleepHour;
    }

    public double getTimeOfSleep() {
        return timeOfSleep;
    }

    public void setTimeOfSleep(double timeOfSleep) {
        this.timeOfSleep = timeOfSleep;
    }

    public int getSnoreTimes() {
        return snoreTimes;
    }

    public void setSnoreTimes(int snoreTimes) {
        this.snoreTimes = snoreTimes;
    }

    public String getSnorePer() {
        return snorePer;
    }

    public void setSnorePer(String snorePer) {
        this.snorePer = snorePer;
    }

    public int getLuxAlert() {
        return luxAlert;
    }

    public void setLuxAlert(int luxAlert) {
        this.luxAlert = luxAlert;
    }

    public String getLuxPer() {
        return luxPer;
    }

    public void setLuxPer(String luxPer) {
        this.luxPer = luxPer;
    }

    public String getOsaTimes() {
        return osaTimes;
    }

    public void setOsaTimes(String osaTimes) {
        this.osaTimes = osaTimes;
    }

    public String getDiary() {
        return diary;
    }

    public void setDiary(String diary) {
        this.diary = diary;
    }

}
