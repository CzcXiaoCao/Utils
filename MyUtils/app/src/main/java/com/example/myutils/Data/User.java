package com.example.myutils.Data;

import android.view.View;
import android.widget.Toast;

/**
 * Created by CaoZhiChao on 2018/5/21 14:20
 */
public class User {
    String name;
    String nickname;
    boolean isMale;
    int age;

    public User(String name, String nickname, boolean isMale, int age) {
        this.name = name;
        this.nickname = nickname;
        this.isMale = isMale;
        this.age = age;
    }
    public void onNameClick(View view) {
        Toast.makeText(view.getContext(), name + " is Clicked", Toast.LENGTH_SHORT).show();
    }

    public boolean onNicknameLongClick(View view) {
        Toast.makeText(view.getContext(), nickname + " is long Clicked", Toast.LENGTH_SHORT).show();
        return true;
    }
    /**
     * Getter and Setter，省略
     */
    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? "" : nickname;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
