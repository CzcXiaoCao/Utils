package com.example.meidalibrary.util;

import android.util.Log;

/**
 * Created by CaoZhiChao on 2018/8/30 17:52
 */
public class Logger {
    private static final boolean DEBUG = true;
    public static void e(String TAG, Object msg) {
        if (DEBUG) {
            Log.e(TAG, String.valueOf(msg));
        }
    }
}
