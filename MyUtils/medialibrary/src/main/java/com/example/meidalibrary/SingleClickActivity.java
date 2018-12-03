package com.example.meidalibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.meidalibrary.bean.MediaData;
import com.example.meidalibrary.fragment.MediaFragment;
import com.example.meidalibrary.interfaces.OnTotalNumChangeForActivity;
import com.example.meidalibrary.util.BaseActivity;
import com.example.meidalibrary.util.MediaConstant;

import java.util.List;

import static com.example.meidalibrary.util.MediaConstant.SINGLE_PICTURE_PATH;


public class SingleClickActivity extends BaseActivity implements OnTotalNumChangeForActivity {
    private final String TAG = "SingleClickActivity";
    private TextView sigleTvStartEdit;
    private List<MediaData> mediaDataList;

    @Override
    protected int initRootView() {
        return R.layout.activity_single_click;
    }

    @Override
    protected void initViews() {
        sigleTvStartEdit = (TextView) findViewById(R.id.sigle_tv_startEdit);
        sigleTvStartEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(SINGLE_PICTURE_PATH, mediaDataList.get(0).getPath());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initData() {
        initVideoFragment(R.id.single_contain);
    }

    private void initVideoFragment(int layoutId) {
        MediaFragment mediaFragment = new MediaFragment(this, this, MediaConstant.TYPE_ITEMCLICK_SINGLE);
        Bundle bundle = new Bundle();
        bundle.putInt(MediaConstant.MEDIA_TYPE, MediaConstant.IMAGE);
        mediaFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(layoutId, mediaFragment)
                .commit();
        getSupportFragmentManager().beginTransaction().show(mediaFragment);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTotalNumChangeForActivity(List selectList, Object tag) {
        mediaDataList = selectList;
        sigleTvStartEdit.setVisibility(selectList.size() > 0 ? View.VISIBLE : View.GONE);
    }
}
