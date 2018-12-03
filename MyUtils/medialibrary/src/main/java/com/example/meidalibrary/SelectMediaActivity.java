package com.example.meidalibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.meidalibrary.adapter.BaseFragmentPagerAdapter;
import com.example.meidalibrary.bean.MediaData;
import com.example.meidalibrary.fragment.MediaFragment;
import com.example.meidalibrary.interfaces.OnTotalNumChangeForActivity;
import com.example.meidalibrary.util.BaseActivity;
import com.example.meidalibrary.util.Logger;
import com.example.meidalibrary.util.MediaConstant;

import java.util.ArrayList;
import java.util.List;
public class SelectMediaActivity extends BaseActivity implements OnTotalNumChangeForActivity{
    private String TAG = getClass().getName();
    private TabLayout tlSelectMedia;
    private ViewPager vpSelectMedia;
    private List<Fragment> fragmentLists = new ArrayList<>();
    private List<String> fragmentTabTitles = new ArrayList<>();
    private BaseFragmentPagerAdapter fragmentPagerAdapter;
    private List<MediaData> mMediaDataList = new ArrayList<>();
    private static int total = 0;
    private TextView meidaTVOfStart;
    private float alphaOnPop = 0.6f;

    public static int getTotal() {
        return total;
    }

    public static void setTotal(int total) {
        SelectMediaActivity.total = total;
    }

    @Override
    protected int initRootView() {
        return R.layout.activity_select_media;
    }

    @Override
    protected void initViews() {
        tlSelectMedia = (TabLayout) findViewById(R.id.tl_select_media);
        vpSelectMedia = (ViewPager) findViewById(R.id.vp_select_media);
        meidaTVOfStart = (TextView) findViewById(R.id.media_tv_startEdit);
    }

    @Override
    protected void initTitle() {
    }


    @Override
    protected void initData() {
        String[] tabList = getResources().getStringArray(R.array.select_media);
        checkDataCountAndTypeCount(tabList, MediaConstant.MEDIATYPECOUNT);
        for (int i = 0; i < tabList.length; i++) {
            MediaFragment mediaFragment = new MediaFragment(this, this, MediaConstant.TYPE_ITEMCLICK_MULTIPLE);
            Bundle bundle = new Bundle();
            bundle.putInt(MediaConstant.MEDIA_TYPE, MediaConstant.MEDIATYPECOUNT[i]);
            mediaFragment.setArguments(bundle);
            fragmentLists.add(mediaFragment);
            fragmentTabTitles.add(tabList[i]);
        }

        //禁止预加载
        vpSelectMedia.setOffscreenPageLimit(3);
        //测试提交
        fragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragmentLists, fragmentTabTitles);
        vpSelectMedia.setAdapter(fragmentPagerAdapter);
        vpSelectMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                notifyFragmentDataSetChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tlSelectMedia.setupWithViewPager(vpSelectMedia);
    }

    /**
     * 校验一次数据，使得item标注的数据统一
     *
     * @param position 碎片对应位置0.1.2
     */
    private void notifyFragmentDataSetChanged(int position) {
        MediaFragment fragment = (MediaFragment) fragmentLists.get(position);
        List<MediaData> currentFragmentList = checkoutSelectList(fragment);
        fragment.getAdapter().setSelectList(currentFragmentList);
        Logger.e(TAG, "onPageSelected: " + fragment.getAdapter().getSelectList().size());
    }

    private List<MediaData> checkoutSelectList(MediaFragment fragment) {
        List<MediaData> currentFragmentList = fragment.getAdapter().getSelectList();
        List<MediaData> totalSelectList = getmMediaDataList();
        for (MediaData mediaData : currentFragmentList) {
            for (MediaData data : totalSelectList) {
                if (data.getPath().equals(mediaData.getPath()) && data.isState() == mediaData.isState()) {
                    mediaData.setPosition(data.getPosition());
                }
            }
        }
        return currentFragmentList;
    }

    private void checkDataCountAndTypeCount(String[] tabList, int[] mediaTypeCount) {
        if (tabList.length != mediaTypeCount.length) {
            return;
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断如果同意的情况下就去 吧权限请求设置给当前fragment的
        for (int i = 0; i < fragmentLists.size(); i++) {
            fragmentLists.get(i).onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onTotalNumChangeForActivity(List selectList, Object tag) {
        meidaTVOfStart.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
        int index = (int) tag;
        Logger.e("2222", "onTotalNumChangeForActivity对应的碎片：  " + index);
        for (int i = 0; i < fragmentLists.size(); i++) {
            if (i != index) {
                Logger.e("2222", "要刷新的碎片：  " + i);
                MediaFragment fragment = (MediaFragment) fragmentLists.get(i);
                fragment.refreshSelect(selectList, index);
            }
        }
        Logger.e(TAG, "onTotalNumChangeForActivity  " + selectList.size());
    }


    public List<MediaData> getmMediaDataList() {
        if (mMediaDataList == null) {
            return new ArrayList<>();
        }
        MediaFragment fragment = (MediaFragment) fragmentLists.get(0);
        return fragment.getAdapter().getSelectList();
    }

    @Override
    protected void onStop() {
        setTotal(0);
        super.onStop();
        Logger.e(TAG, "onStop");
    }
}
