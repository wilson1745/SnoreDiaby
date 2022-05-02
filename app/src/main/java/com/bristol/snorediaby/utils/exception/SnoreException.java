package com.bristol.snorediaby.utils.exception;

import android.util.Log;

public class SnoreException extends Exception {

    public static void getErrorException(String tag, Exception e) {
        Log.e(tag, tag + " Exception: " + e, e);
    }

}
