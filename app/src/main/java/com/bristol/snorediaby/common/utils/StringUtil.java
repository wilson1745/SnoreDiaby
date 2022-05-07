package com.bristol.snorediaby.common.utils;

import com.bristol.snorediaby.common.enums.ErrorMessageEnum;
import org.apache.commons.lang3.StringUtils;

public class StringUtil extends StringUtils {

    public static void getErrorMsg(String label) throws IllegalStateException {
        throw new IllegalStateException(String.format(ErrorMessageEnum.REQUIRED_NULL.getValue(), label));
    }

}
