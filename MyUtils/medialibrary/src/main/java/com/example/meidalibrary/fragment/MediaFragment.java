package com.example.meidalibrary.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.meidalibrary.R;
import com.example.meidalibrary.SelectMediaActivity;
import com.example.meidalibrary.adapter.AgendaSimpleSectionAdapter;
import com.example.meidalibrary.adapter.SectionedSpanSizeLookup;
import com.example.meidalibrary.bean.MediaData;
import com.example.meidalibrary.interfaces.OnTotalNumChange;
import com.example.meidalibrary.interfaces.OnTotalNumChangeForActivity;
import com.example.meidalibrary.util.Logger;
import com.example.meidalibrary.util.MediaConstant;
import com.example.meidalibrary.util.MediaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CaoZhiChao on 2018/6/5 10:59
 */
@SuppressLint("ValidFragment")
public class MediaFragment extends BaseFragment implements OnTotalNumChange {
    private final String TAG = getClass().getName();
    RecyclerView mediaRecycler;
    GridLayoutManager layoutManager;
    List<List<MediaData>> lists = new ArrayList<>();
    List<MediaData> listOfOut = new ArrayList<>();
    private AgendaSimpleSectionAdapter adapter;
    private int index;
    private OnTotalNumChangeForActivity mOnTotalNumChangeForActivity;
    private Activity mActivity;
    private int clickType;
    @SuppressLint("ValidFragment")
    public MediaFragment(Activity activity, OnTotalNumChangeForActivity totalNumChangeForActivity, int clickType) {
        mActivity = activity;
        mOnTotalNumChangeForActivity = totalNumChangeForActivity;
        this.clickType = clickType;
    }

    @Override
    protected int initRootView() {
        return R.layout.fragment_media;
    }

    @Override
    protected void initArguments(Bundle arguments) {
        if (arguments != null) {
            index = arguments.getInt(MediaConstant.MEDIA_TYPE);
        }
    }

    @Override
    protected void initView() {
        mediaRecycler = (RecyclerView) mRootView.findViewById(R.id.media_recycleView);
        showLocalMediaByMediaType();
    }

    @Override
    protected void onLazyLoad() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initListener() {
    }

    /**
     * 根据当前的media的类型显示不同的media
     */
    private void showLocalMediaByMediaType() {
        //要判断是否有权限，无权限的时候不能去读写SD卡
        MediaUtils.getMediasByType(mActivity, index, new MediaUtils.LocalMediaCallback() {
            @Override
            public void onLocalMediaCallback(final List<MediaData> allMediaTemp) {
                MediaUtils.ListOfAllMedia listOfAllMedia=MediaUtils.groupListByTime(allMediaTemp);
                lists=listOfAllMedia.getListOfAll();
                listOfOut=listOfAllMedia.getListOfParent();
                adapter = new AgendaSimpleSectionAdapter(lists, listOfOut, mediaRecycler, MediaFragment.this, index, mActivity,clickType);
                mediaRecycler.setAdapter(adapter);
                layoutManager = new GridLayoutManager(getContext(), 4);
                SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
                layoutManager.setSpanSizeLookup(lookup);
                mediaRecycler.setLayoutManager(layoutManager);
            }
        });
    }

    @Override
    public void onTotalNumChange(@NonNull List selectList, Object TAG) {
        int total = selectList.size();
        Logger.e("onTotalNumChange", "当前碎片的标签：   " + TAG + "     总数据：    " + total);
        if (mOnTotalNumChangeForActivity != null) {
            mOnTotalNumChangeForActivity.onTotalNumChangeForActivity(selectList, TAG);
        }
    }

    public AgendaSimpleSectionAdapter getAdapter() {
        return adapter;
    }

    public void refreshSelect(List<MediaData> listOfOther, int from) {
        if (index != 0) {
            if (mActivity instanceof SelectMediaActivity){
                SelectMediaActivity activity = (SelectMediaActivity) mActivity;
                listOfOther = activity.getmMediaDataList();
            }
        }
        if (isAdded() && adapter != null && adapter.getSelectList() != null) {
            List<MediaData> needRefreshList = getNeedRefreshList(getSameTypeData(adapter.getSelectList(), from), getSameTypeData(listOfOther, index), from);
            Logger.e("2222", "不同个数：    " + needRefreshList.size());
            for (int i = 0; i < needRefreshList.size(); i++) {
                if (needRefreshList.get(i).getType() == index || index == 0) {
                    Logger.e("2222", "更新数据：    " + needRefreshList.get(i).getPath());
                    Point point = adapter.getPointByData(lists, needRefreshList.get(i));
                    adapter.itemClick(mediaRecycler.getChildAt(adapter.getPositionByData(lists, needRefreshList.get(i))), point.x, point.y, false);
                }
            }
        }
    }

    private List<MediaData> getSameTypeData(List<MediaData> list, int index) {
        List<MediaData> newList = new ArrayList<>();
        for (MediaData mediaData : list) {
            if (mediaData.getType() == index || index == 0) {
                newList.add(mediaData);
            }
        }
        return newList;
    }

    /**
     * 获取两个List的不同元素
     *
     * @param adapterList
     * @param listFormList
     * @return
     */
    private List<MediaData> getNeedRefreshList(List<MediaData> adapterList, List<MediaData> listFormList, int from) {
        List<MediaData> diff = new ArrayList<>();
        //如果数据都和全部内容无关，暂时不做处理
        if (from != 0 && index != 0) {
            return diff;
        }
        Map<String, Integer> map = new HashMap<>(adapterList.size());
        for (MediaData mediaData : adapterList) {
            map.put(mediaData.getPath(), 1);
        }
        for (MediaData string : listFormList) {
            if (map.get(string.getPath()) != null) {
                map.put(string.getPath(), 2);
                Log.e("1234", "哈哈：   " + string.isState() + "          " + getStateByPathInList(adapterList, string.getPath()));
                if (string.isState() != getStateByPathInList(adapterList, string.getPath())) {
                    diff.add(string);
                }
                continue;
            }
            Log.e("1234", "这也添加一次");
            diff.add(string);
        }
        if (adapterList.size() > listFormList.size()) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 1 && !diff.contains(getDataByPath(entry.getKey()))) {
                    Log.e(TAG, "getNeedRefreshList: ++++++++++++++" + entry.getKey());
                    diff.add(getDataByPath(entry.getKey()));
                }
            }
        }
        return diff;
    }

    private boolean getStateByPathInList(List<MediaData> list, String path) {
        for (MediaData mediaData : list) {
            if (mediaData.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    private MediaData getDataByPath(String path) {
        return adapter.getDataByPath(lists, path);
    }

}
