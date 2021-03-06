package com.example.myutils.Views.MusicCutLikeDouYin;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myutils.R;

import java.util.List;

import static com.example.myutils.Activity.MusicCutActivity.onePice;


/**
 * Created by CaoZhiChao on 2018/5/7 10:15
 */
public class CutMusicRecycleViewAdapter extends RecyclerView.Adapter<CutMusicRecycleViewAdapter.MyViewHolder> {
    Context context;
    private List<Integer> mDatas;
    private boolean isFirst = true;
    private CustomCutMusicItemView view_position0;
    private CutMusicRecycleView mRecyclerView;

    public CutMusicRecycleViewAdapter(Context context, List<Integer> list, CutMusicRecycleView mRecyclerView) {
        this.context = context;
        this.mDatas = list;
        this.mRecyclerView = mRecyclerView;
    }

    public CustomCutMusicItemView getView_position0() {
        return view_position0;
    }

    public void setFirst(boolean first) {
        isFirst = first;
        if (view_position0 != null) {
            view_position0 = null;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.activity_cutmusic_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.myView_last.setSrcW_NOInvalidate(0);
        holder.myView_last.clearAnimation();
        holder.myView_last.setType(mDatas.get(position));
        if (position == 0 && isFirst) {
            view_position0 = holder.myView_last;
            view_position0.post(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator animator_first = mRecyclerView.moveView(view_position0, "srcW", 0, view_position0.getWidth(), onePice);
                    animator_first.setRepeatCount(-1);
                    mRecyclerView.setAnimator_first(animator_first);
                    mRecyclerView.startAnimations();
                }
            });

        }
    }

    /**
     * 带参的onBindViewHolder，解决局部刷新闪烁问题
     *
     * @param holder
     * @param position
     * @param payloads
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.myView_last.setSrcW_NOInvalidate(0);
            holder.myView_last.clearAnimation();
            holder.myView_last.setType(mDatas.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomCutMusicItemView myView_last;

        public MyViewHolder(View view) {
            super(view);
            myView_last = view.findViewById(R.id.view_musicItem);
        }
    }
}
