package com.example.myutils.Views.MusicCutLikeDouYin;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.myutils.Utils.LogUtil;
import com.example.myutils.Utils.ScreenUtil;


/**
 * Created by CaoZhiChao on 2018/5/8 11:21
 */
public class CutMusicRecycleView extends RecyclerView {
    Context mContext;
    int totalMusic;
    String TAG = "222";
    /**
     * 判断是往前还是往后以后的，0是后，1是前
     */
    int scrollState = 0;
    ObjectAnimator animator_first;
    ObjectAnimator animator_last;
    int animation_count = 0;
    int firstVisibleItemPosition;
    int lastVisibleItemPosition;
    float getX;
    int totalSSS = 0;
    CustomLinearLayoutManager linearLayoutManager;
    ScrollChangedListener mScrollChangedListener;
    //余数，用于计算滑动距离
    int yushu;
    int screenWidth;
    CustomCutMusicItemView viewFirst;
    CustomCutMusicItemView viewLast;
    //控制动画是start还是resume
    boolean isAnimaPause = false;
    long timeLineLength;
    //控制从草稿进入时，需要滑动的余数
    boolean fromDraftNeedScroll=false;
    //需要滑动的距离
    int fromDraftYuShu;
    public CutMusicRecycleView(Context context) {
        super(context);
        Log.e("1234","初始化");
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        screenWidth = ScreenUtil.getScreenWidth(context);
    }

    public CutMusicRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.e("1234","初始化");
        init(context);
    }

    public void setTotalSSS(int totalSSS) {
        this.totalSSS = totalSSS;
    }

    public boolean isAnimaPause() {
        return isAnimaPause;
    }

    public void setAnimaPause(boolean animaPause) {
        isAnimaPause = animaPause;
    }

    public ObjectAnimator getAnimator_first() {
        return animator_first;
    }

    public void setAnimator_first(ObjectAnimator animator_first) {
        this.animator_first = animator_first;
    }

    public ObjectAnimator getAnimator_last() {
        return animator_last;
    }

    public void setAnimator_last(ObjectAnimator animator_last) {
        this.animator_last = animator_last;
    }

    public CustomCutMusicItemView getViewFirst() {
        return viewFirst;
    }

    public void setViewFirst(CustomCutMusicItemView viewFirst) {
        this.viewFirst = viewFirst;
    }

    public CustomCutMusicItemView getViewLast() {
        return viewLast;
    }

    public void setViewLast(CustomCutMusicItemView viewLast) {
        this.viewLast = viewLast;
    }

    public void setYushu(int yushu) {
        this.yushu = yushu;
        if (yushu != 0) {
            Log.e("1234","setYushu  "+(getAdapter() == null));
            timeLineLength = ScreenUtil.getScreenWidth(getContext()) * (getAdapter().getItemCount() - 2) + yushu;
        } else {
            timeLineLength = ScreenUtil.getScreenWidth(getContext()) * (getAdapter().getItemCount() - 1);
        }
    }

    public int getTotalMusic() {
        return totalMusic;
    }

    public void setTotalMusic(Long totalMusic) {
        this.totalMusic =  Math.round(totalMusic);
    }

    public void setLinearLayoutManager(CustomLinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public void setmScrollChangedListener(ScrollChangedListener mScrollChangedListener) {
        this.mScrollChangedListener = mScrollChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getX < event.getX()) {
                    if (!linearLayoutManager.isScrollEnabled()) {
                        linearLayoutManager.setScrollEnabled(true);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        totalSSS += dx;
        if (fromDraftNeedScroll){
            fromDraftNeedScroll=false;
            scrollBy(fromDraftYuShu,0);
            onScrollStateChanged(RecyclerView.SCROLL_STATE_IDLE);
        }
        //整数长加上余数
        if (totalSSS > timeLineLength) {
            linearLayoutManager.setScrollEnabled(false);
        }
        if (dx > 0) {
            //向后滑动了
            scrollState = 0;
        } else {
            scrollState = 1;
            //向前滑动了
        }
    }

    @Override
    public void onScrollStateChanged(int newState) {
        super.onScrollStateChanged(newState);
        CutMusicRecycleViewAdapter adapter = (CutMusicRecycleViewAdapter) getAdapter();
        adapter.setFirst(false);
        isAnimaPause = false;
        LogUtil.e("onScrollStateChanged   "+newState);
        //自由滑动时，由于复用机制，会执行动画
        if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
            clearAnimation_all();
        }
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            //清空之前的动画
            clearAnimation_all();
            //滑动后，可见view的位置没有变，手动修改之前由于动画造成的图形变化
            //向后滑动，对应位置没有变化
            if (lastVisibleItemPosition == linearLayoutManager.findLastVisibleItemPosition() && lastVisibleItemPosition > 0) {
                CustomCutMusicItemView viewLast = getView(lastVisibleItemPosition);
                viewLast.setSrcW_NOInvalidate(0);
                viewLast.setType(0);
            } else if (linearLayoutManager.findLastVisibleItemPosition() == firstVisibleItemPosition && lastVisibleItemPosition > 0) {
                //向前滑动后，后一个位置是前一个的第一个位置
                CustomCutMusicItemView viewLast = getView(firstVisibleItemPosition);
                viewLast.setSrcW_NOInvalidate(0);
                viewLast.setType(0);
            }
            firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            mScrollChangedListener.refreshAdapter(firstVisibleItemPosition);
            viewFirst = getView(firstVisibleItemPosition);
            viewLast = getView(lastVisibleItemPosition);
            if (viewFirst != null && viewLast != null) {
                if (scrollState == 0) {
                    //由于缓存，手动刷新前一个item
                    getAdapter().notifyItemChanged(firstVisibleItemPosition - 1, TAG);
                    refreshLayout(viewFirst, viewLast);
                } else {
                    getAdapter().notifyItemChanged(firstVisibleItemPosition, TAG);
                    getAdapter().notifyItemChanged(lastVisibleItemPosition, TAG);
                    refreshLayout(viewFirst, viewLast);
                }
            }
        }
    }

    public void clearAnimation_all() {
        if (animator_first != null) {
            animator_first.cancel();
            animator_first = null;
        }
        if (animator_last != null) {
            animator_last.cancel();
            animator_last = null;
        }
        animation_count = 0;
    }

    private CustomCutMusicItemView getView(int position) {
        View child = getLayoutManager().findViewByPosition(position);
        CutMusicRecycleViewAdapter.MyViewHolder viewHolder = (CutMusicRecycleViewAdapter.MyViewHolder) getChildViewHolder(child);
        CustomCutMusicItemView myView_last = viewHolder.myView_last;
        if (myView_last == null) {
            return null;
        } else {
            return myView_last;
        }
    }

    private void refreshLayout(CustomCutMusicItemView viewFirst, final CustomCutMusicItemView viewLast) {
        LogUtil.e("refreshLayout");
        int x = Math.abs(getViewInScreenX(viewFirst));
        int x_last = Math.abs(getViewInScreenX(viewLast));
        mScrollChangedListener.scrollChange(x, firstVisibleItemPosition);
        viewFirst.setSrcW(x);
        int firstAnimaLength = totalMusic * (screenWidth - x) / screenWidth;
        int secondAnimaLength = totalMusic - firstAnimaLength;
        if (firstVisibleItemPosition == lastVisibleItemPosition) {
            secondAnimaLength = firstAnimaLength;
        }
        animator_first = moveView(viewFirst, "srcW", x, screenWidth, firstAnimaLength);
        animator_last = moveView(viewLast, "srcW", 1, (screenWidth - x_last), secondAnimaLength);
        animator_first.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (viewLast != null) {
                    viewLast.setSrcW_NOInvalidate(0);
                    viewLast.setType(0);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator_last.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_last.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(animator_first !=null){
                    animator_first.start();
                }
                if (viewLast != null) {
                    viewLast.setSrcW(0);
                    viewLast.setType(0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator_first.start();
    }

    /**
     * x是在屏幕外的部分大小
     *
     * @param view
     * @return
     */
    private int getViewInScreenX(CustomCutMusicItemView view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        return x;
    }

    public ObjectAnimator moveView(final CustomCutMusicItemView view, final String attr, int start, int end, long duration) {
        ObjectAnimator move = ObjectAnimator.ofInt(view, attr, start, end);
        move.setDuration(duration);
        LogUtil.e("setDuration      "+duration);
        move.setInterpolator(new LinearInterpolator());
        return move;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseAnimation() {
        if (animator_first != null) {
            animator_first.pause();
        }
        if (animator_last !=null){
            animator_last.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeAnimation() {
        if (animator_first != null) {
            animator_first.resume();
        }
        if (animator_last !=null){
            animator_last.resume();
        }
    }

    public void startAnimations() {
        if (animator_first != null && animator_first != null) {
            animator_first.start();
        }
    }

    public interface ScrollChangedListener {
        void refreshAdapter(int firstVisibleItemPosition);

        void scrollChange(int scrollWight, int position);
    }
}

