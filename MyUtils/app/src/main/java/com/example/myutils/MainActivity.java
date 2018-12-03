package com.example.myutils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.meidalibrary.SelectMediaActivity;
import com.example.meidalibrary.SingleClickActivity;
import com.example.myutils.Activity.DataBindingActivity;
import com.example.myutils.Activity.HttpActivity;
import com.example.myutils.Activity.MusicCutActivity;
import com.example.myutils.Utils.CommonRecyclerAdapter;
import com.example.myutils.Utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CaoZhiChao on 2018/6/25 11:23
 */
public class MainActivity extends MPermissionsActivity implements CommonRecyclerAdapter.OnItemClickListener {
    private final String TAG = "MainActivity";
    List<String> listOfActivityName = new ArrayList<>();
    @BindView(R.id.activity_RecyclerView)
    RecyclerView recyclerView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        adapter = new MyAdapter(this, listOfActivityName, R.layout.item_main);
        adapter.setOnItemClickListener(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0002);
    }

    private void initData() {
        String[] activityNames = getResources().getStringArray(R.array.activityNames);
        for (int i = 0; i < activityNames.length; i++) {
            listOfActivityName.add(activityNames[i]);
        }
    }

    @Override
    public void onItemClick(int pos) {
        switch (pos) {
            case 0:
                startActivity(DataBindingActivity.class);
                break;
            case 1:
                startActivity(MusicCutActivity.class);
                break;
            case 2:
                startActivity(HttpActivity.class);
                break;
            case 3:
                startActivity(SelectMediaActivity.class);
                break;
            case 4:
                startActivity(SingleClickActivity.class);
                break;
            default:
                Log.e(TAG, "onItemClick: " + pos + "还没有处理点击事件！");
                break;
        }
    }

    class MyAdapter extends CommonRecyclerAdapter {

        public MyAdapter(Context context, List data, int layoutId) {
            super(context, data, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, Object item) {
            Log.e("1234", "2");
            // 从ViewHolder中去findViewById
            TextView nameTv = holder.itemView.findViewById(R.id.button);
            Log.e("1234", "" + (String) item);
            nameTv.setText((String) item);
        }
    }

    private void startActivity(Class activityClass) {
        startActivity(new Intent(MainActivity.this, activityClass));
    }
}

