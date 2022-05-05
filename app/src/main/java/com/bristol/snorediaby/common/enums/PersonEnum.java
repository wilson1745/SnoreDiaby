package com.bristol.snorediaby.common.enums;

public enum PersonEnum {

    AGE("age"),

    SEX("sex"),

    HEIGHT("height"),

    WEIGHT("weight"),

    BMI("bmi"),

    EVALUATION("evaluation");

    private final String value;

    PersonEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
