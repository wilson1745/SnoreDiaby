package com.bristol.snorediaby.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;

public enum LanguageTypeEnum {

    ENGLISH("english"),

    TRAD_CHINESE("traditional_chinese"),

    SIMP_CHINESE("simplified_chinese"),

    ;

    private static final Map<String, LanguageTypeEnum> LOOKUP = Maps
        .uniqueIndex(Arrays.asList(LanguageTypeEnum.values()), languageType -> languageType != null ? languageType.getValue() : null);

    private final String value;

    LanguageTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static LanguageTypeEnum getEnum(String value) {
        return LOOKUP.get(value);
    }

}
