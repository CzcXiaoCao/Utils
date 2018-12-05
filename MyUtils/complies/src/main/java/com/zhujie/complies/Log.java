package com.zhujie.complies;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Created by CaoZhiChao on 2018/12/5 11:06
 */
public class Log {
    public static void e(Messager mMessager, String msg){
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
