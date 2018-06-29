package com.example.myutils.Utils;

import android.util.Log;

import com.example.myutils.Data.Constant;

/**
 * Created by CaoZhiChao on 2018/6/29 11:18
 */
public class LogUtil {
    public static void e(Object msg) {
        if (Constant.DEBUG) {
            Log.e("1234", String.valueOf(msg));
        }
    }

    public static void e(String key, Object msg) {
        if (Constant.DEBUG) {
            Log.e(key, String.valueOf(msg));
        }
    }

    public static void d(String key, Object msg) {
        if (Constant.DEBUG) {
            Log.d(key, String.valueOf(msg));
        }
    }


}
