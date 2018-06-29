package com.example.myutils.Activity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.example.myutils.R;
import com.example.myutils.Utils.LogUtil;
import com.example.myutils.Utils.ScreenUtil;
import com.example.myutils.Views.MusicCutLikeDouYin.CustomLinearLayoutManager;
import com.example.myutils.Views.MusicCutLikeDouYin.CutMusicRecycleView;
import com.example.myutils.Views.MusicCutLikeDouYin.CutMusicRecycleViewAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MusicCutActivity extends AppCompatActivity {
    @BindView(R.id.music_recyclerView)
    CutMusicRecycleView mRecyclerView;
    @BindView(R.id.button2)
    Button button;
    MediaPlayer player = null;
    //毫秒
    long totalLength;
    //假设一屏是长度
    long onePice = 20 * 1000;
    List mDatas;
    private AssetManager assetManager;
    CustomLinearLayoutManager linearLayoutManager;
    CutMusicRecycleViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_cut);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player = null;
        }
    }

    @OnClick(R.id.button2)
    public void showMusicView() {
        playRing();
        initMusicData((int) Math.ceil(totalLength/onePice));
        mRecyclerView.setTotalMusic(onePice);
        mAdapter = new CutMusicRecycleViewAdapter(getApplicationContext(), mDatas);
        //设置布局管理器
        linearLayoutManager = new CustomLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setmAdapter(mAdapter);
        mRecyclerView.setLinearLayoutManager(linearLayoutManager);
        mRecyclerView.setTotalSSS(0);
        long ss = totalLength % onePice;
        mRecyclerView.setYushu((int) (ss * ScreenUtil.getScreenWidth(getApplicationContext()) / onePice));
    }

    private MediaPlayer playRing() {
        try {
            player = new MediaPlayer();
            assetManager = getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("谢春花 - 一棵会开花的树.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            player.prepare();
            totalLength = player.getDuration();
            LogUtil.e("长度：  " + player.getDuration());
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    protected void initMusicData(int count) {
        mDatas = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            mDatas.add(i);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.start();
        }
    }

}
