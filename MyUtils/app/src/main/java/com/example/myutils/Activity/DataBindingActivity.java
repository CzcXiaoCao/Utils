package com.example.myutils.Activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.myutils.R;
import com.example.myutils.Data.User;
import com.example.myutils.databinding.ActivityDataBindingBinding;

public class DataBindingActivity extends AppCompatActivity {
    ActivityDataBindingBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_binding);
    }

    public void onClick(View view) {
        User user = new User("张君宝", "张三丰", true, 30);
        mBinding.setUser(user);
    }

    public void onClick2(View view) {
        User user = new User("张san", "张si", false, 20);
        mBinding.setUser(user);
    }
}