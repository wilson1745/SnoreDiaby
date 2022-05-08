package com.bristol.snorediaby.repo.domains;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Function: Records
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
@Entity(tableName = "PERSON")
public class Person {

    // 設置是否使ID自動累加
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_ID")
    private int id;

    @ColumnInfo(name = "AGE")
    private int age;

    @ColumnInfo(name = "SEX")
    private String sex;

    @ColumnInfo(name = "HEIGHT")
    private double height;

    @ColumnInfo(name = "WEIGHT")
    private double weight;

    @ColumnInfo(name = "BMI")
    private double bmi;

    @ColumnInfo(name = "EVALUATION")
    private String evaluation;

    public Person(int age, String sex, double height, double weight, double bmi, String evaluation) {
        this.age = age;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.evaluation = evaluation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

}