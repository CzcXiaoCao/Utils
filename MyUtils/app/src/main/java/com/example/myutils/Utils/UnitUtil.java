package com.example.myutils.Utils;

import android.content.Context;

/**
 * Created by CaoZhiChao on 2018/6/25 16:00
 */
public class UnitUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
