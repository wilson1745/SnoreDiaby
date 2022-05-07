package com.bristol.snorediaby.common.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ButtonStateEnum {

    START("start"),

    STOP("stop"),

    ;

    private static final Map<String, ButtonStateEnum> LOOKUP =
        Arrays.stream(ButtonStateEnum.values()).collect(Collectors.toMap(ButtonStateEnum::name, Function.identity()));

    private static final Map<String, ButtonStateEnum> LOOKUP_VALUE =
        Arrays.stream(ButtonStateEnum.values()).collect(Collectors.toMap(ButtonStateEnum::getValue, Function.identity()));

    private String value;

    ButtonStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static ButtonStateEnum getEnum(String value) {
        return LOOKUP.get(value);
    }

    public static String getEnumValue(String value) {
        return LOOKUP.get(value).getValue();
    }

    public static List<String> getEnumValues() {
        return new ArrayList<>(LOOKUP_VALUE.keySet());
    }

}
