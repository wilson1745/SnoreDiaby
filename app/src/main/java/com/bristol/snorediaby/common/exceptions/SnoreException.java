package com.bristol.snorediaby.common.exceptions;

import android.util.Log;

public class SnoreException extends Exception {

    public static void getErrorException(String tag, Exception e) {
        Log.e(tag, tag + " Exception: " + e, e);
    }

}
