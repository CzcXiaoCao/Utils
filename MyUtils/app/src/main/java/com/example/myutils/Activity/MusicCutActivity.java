package com.example.myutils.Activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;

import com.example.myutils.R;
import com.example.myutils.Utils.LogUtil;
import com.example.myutils.Views.MusicCutLikeDouYin.CustomCutMusicItemView;
import com.example.myutils.Views.MusicCutLikeDouYin.CustomLinearLayoutManager;
import com.example.myutils.Views.MusicCutLikeDouYin.CutMusicRecycleView;
import com.example.myutils.Views.MusicCutLikeDouYin.CutMusicRecycleViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.myutils.Utils.ScreenUtil.getScreenWidth;

/**
 * @author 27982
 */
public class MusicCutActivity extends AppCompatActivity {
    //假设一屏是20毫秒
    public static final long onePice = 20 * 1000;
    @BindView(R.id.music_recyclerView)
    CutMusicRecycleView mRecyclerView;
    @BindView(R.id.button2)
    Button button;
    MediaPlayer player = null;
    //毫秒
    long totalLength;
    List mDatas;
    CustomLinearLayoutManager linearLayoutManager;
    CutMusicRecycleViewAdapter mAdapter;
    private AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_cut);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (mRecyclerView != null) {
            mRecyclerView.clearAnimation_all();
        }
        super.onDestroy();
        if (player != null) {
            player.stop();
            player = null;
        }
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.button2)
    public void showMusicView() {
        playRing();
        initMusicData((int) Math.ceil(totalLength / onePice));
        mRecyclerView.setTotalMusic(onePice);
        mAdapter = new CutMusicRecycleViewAdapter(getApplicationContext(), mDatas, mRecyclerView);
        //设置布局管理器
        linearLayoutManager = new CustomLinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLinearLayoutManager(linearLayoutManager);
        mRecyclerView.setTotalSSS(0);
        long ss = totalLength % onePice;
        mRecyclerView.setYushu((int) (ss * getScreenWidth(getApplicationContext()) / onePice));
        mRecyclerView.setmScrollChangedListener(new CutMusicRecycleView.ScrollChangedListener() {
            @Override
            public void refreshAdapter(int firstVisibleItemPosition) {
                int length = mDatas.size();
                for (int i = 0; i < length; i++) {
                    if (i < firstVisibleItemPosition) {
                        mDatas.set(i, 1);
                    } else {
                        mDatas.set(i, 0);
                    }
                }
                //不设置的话，如果刚显示就快速滑动，会因为缓存问题，部分不执行onBindViewHolder方法
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void scrollChange(int widght, int position) {
                //当前位置超出屏幕范围  当前位置
                //当前位置代表的秒数
//                long secondDruing = (long) (m_timeline.getDuration() / 1000D);
//                long min = position * secondDruing + widght * secondDruing / getScreenWidth(getApplicationContext());
//                //TODO 本地音乐需要，到时候要做区分，但是本地音乐逻辑之前是layout，不知道会不会改成activity，所以暂时不做兼容
////                long trimIn = (long) min + m_musicTrimIn;
//                long trimIn = min;
//                if (m_selectedMusic == null)
//                    return;
//                m_selectedMusic.setTrimIn(trimIn);
//                m_trimInTextView.setText(Util.formatTime((long) min));
//                TimelineData.instance().setM_music(m_selectedMusic);
//                Util.resetMusic(m_videoTrack, m_musicTrack);
//                playVideo(0);
            }
        });
        Log.e("1234", "第一个view：  " + (mAdapter.getView_position0() != null));
        if (mAdapter != null && mAdapter.getView_position0() != null) {
            CustomCutMusicItemView view = mAdapter.getView_position0();
            ObjectAnimator animator_first = mRecyclerView.moveView(view, "srcW", 0, view.getWidth(), onePice);
            animator_first.start();
        }
        mRecyclerView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                Log.e("1234", "第一个view：  " + (mAdapter.getView_position0() != null));
                return null;
            }
        });
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
            mDatas.add(0);
        }
    }

    @SuppressLint("NewApi")
    private void pauseRecyclerViewAnimation() {
        if (mRecyclerView != null) {
            mRecyclerView.pauseAnimation();
            mRecyclerView.setAnimaPause(true);
        }
    }

    private void startRecyclerViewAnimations() {
        if (mRecyclerView != null) {
            mRecyclerView.startAnimations();
        }
    }

    @SuppressLint("NewApi")
    private void resumeRecyclerViewAnimation() {
        if (mRecyclerView != null) {
            mRecyclerView.resumeAnimation();
        }
    }

    @Override
    protected void onPause() {
        pauseRecyclerViewAnimation();
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onResume() {
        resumeRecyclerViewAnimation();
        super.onResume();
        if (player != null) {
            player.start();
        }
    }

}
