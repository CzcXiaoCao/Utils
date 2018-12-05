package com.example.myutils.annotationUtil;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by CaoZhiChao on 2018/12/5 14:08
 */
public class Bind {
    private static final String TAG = "Bind";

    public static void bindView(Activity activity) {
        try {
            Class<?> listenerClass = Class.forName(activity.getClass().getPackage().getName()+".Bind_" + activity.getClass().getSimpleName());
            Object o = listenerClass.newInstance();
            Method method = listenerClass.getDeclaredMethod("bindView", activity.getClass());
            method.invoke(o, activity);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ClassNotFoundException: " + e.toString());
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "NoSuchMethodException: " + e.toString());
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.e(TAG, "InvocationTargetException: " + e.toString());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
