package com.example.meidalibrary.util;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Newnet on 2017/1/6.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener  {


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        //设置视图
        setContentView(initRootView());
        initViews();
        initTitle();
        initData();
        initListener();
    }


    /************************** 公共功能封装 《前》 ****************************************/

    //初始化视图
    protected abstract int initRootView();
    //初始化视图组件
    protected abstract void initViews();
    //初始化头布局
    protected abstract void initTitle();
    //数据处理
    protected abstract void initData();
    //视图监听事件处理
    protected abstract void initListener();


    /************************** 公共功能封装 《后》 ****************************************/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}