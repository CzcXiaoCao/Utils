package com.example.myutils.Views.MusicCutLikeDouYin;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by CaoZhiChao on 2018/5/8 10:32
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public boolean isScrollEnabled() {
        return isScrollEnabled;
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }
}