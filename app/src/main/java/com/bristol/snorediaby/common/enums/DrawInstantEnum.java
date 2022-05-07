package com.bristol.snorediaby.common.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DrawInstantEnum {

    LUX("lux"),

    SOUND("sound"),

    INIT("init"),
    ;

    private static final Map<String, DrawInstantEnum> LOOKUP =
        Arrays.stream(DrawInstantEnum.values()).collect(Collectors.toMap(DrawInstantEnum::name, Function.identity()));

    private static final Map<String, DrawInstantEnum> LOOKUP_VALUE =
        Arrays.stream(DrawInstantEnum.values()).collect(Collectors.toMap(DrawInstantEnum::getValue, Function.identity()));

    private String value;

    DrawInstantEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static DrawInstantEnum getEnum(String value) {
        return LOOKUP.get(value);
    }

    public static String getEnumValue(String value) {
        return LOOKUP.get(value).getValue();
    }

    public static List<String> getEnumValues() {
        return new ArrayList<>(LOOKUP_VALUE.keySet());
    }

}
