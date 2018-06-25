package com.example.myutils.Utils;

/**
 * Created by CaoZhiChao on 2018/6/25 16:56
 */
public interface MultiTypeSupport<T> {
    // 根据当前位置或者条目数据返回布局
    public int getLayoutId(T item, int position);
}