package com.example.myutils.Views.MusicCutLikeDouYin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.myutils.R;
import com.example.myutils.Utils.ScreenUtil;
import com.example.myutils.Utils.UnitUtil;


/**
 * Created by CaoZhiChao on 2018/4/28 16:44
 */
public class CustomCutMusicItemView extends View {
    int height = 100;
    int totalHeight;
    int width;
    Paint paint_white;
    //两边默认边距
    float totalWidth;
    //单位是dp,要转化为px
    float margin;
    float viewWidth;
    double[] diffWidth = {0.4, 0.66, 1, 0.80, 0.64, 0.40, 0.80, 0.64, 0.50, 0.40,
            0.64, 0.40, 0.50, 0.64, 0.74, 1, 0.80, 0.64, 0.40, 0.80,
            0.64, 0.50, 0.40, 0.64, 0.40, 0.50, 0.64, 1, 0.80, 0.64,
            0.40, 0.64, 0.40, 0.30, 0.40};
    //上下两边间距
    int defaultY_T = 10;
    int defaultY_B = 20;
    int srcW = 0;
    int start_w = 0;
    //宽和间距占比
    float percentW = 7.0F;
    float percentM = 3.0F;
    int type = 0;
    private Paint mPaint;
    private Bitmap dstBmp = null;
    private Bitmap clipBitmap;
    private Bitmap newDstBmp;

    public CustomCutMusicItemView(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        width = ScreenUtil.getScreenWidth(getContext());
        height = UnitUtil.dip2px(getContext(), height);

        defaultY_T = UnitUtil.dip2px(getContext(), defaultY_T);
        defaultY_B = UnitUtil.dip2px(getContext(), defaultY_B);
        totalHeight = height + defaultY_T + defaultY_B;

        mPaint = new Paint();
        paint_white = new Paint();
        paint_white.setColor(getResources().getColor(R.color.cutmusic_paint_white));
        paint_white.setStyle(Paint.Style.FILL_AND_STROKE);
        paint_white.setStrokeCap(Paint.Cap.ROUND);
    }

    public CustomCutMusicItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        invalidate();
    }

    public void setStart_w(int start_w) {
        this.start_w = start_w;
        invalidate();
    }

    public void setSrcW(int srcW) {
        this.srcW = srcW;
        invalidate();
    }

    public void setSrcW_NOInvalidate(int srcW) {
        this.srcW = srcW;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (type == 0) {
            canvas.drawBitmap(dstBmp, 0, 0, mPaint);
            if (srcW != 0) {
                clipBitmap = Bitmap.createBitmap(newDstBmp, start_w, 0, srcW, totalHeight);
                canvas.drawBitmap(clipBitmap, start_w, 0, mPaint);
            }
        } else {
            canvas.drawBitmap(newDstBmp, 0, 0, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, totalHeight);
        int length = diffWidth.length;
        float a = (float) width / length / 10;
        margin = a * percentM;
        viewWidth = a * percentW;
        paint_white.setStrokeWidth(viewWidth);
        if (width > 0 && (dstBmp == null || newDstBmp == null)) {
            dstBmp = makeDst(width, totalHeight);
            paint_white.setColor(getResources().getColor(R.color.cutmusic_paint_blue));
            newDstBmp = makeDst(width, totalHeight);
        }
    }

    Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bm);
        int total = (int) (w / (margin + viewWidth));
        for (int i = 0; i < 35; i++) {
            totalWidth = viewWidth * (i + 1) + margin * i;
            int middle = height / 2;
            float heightB = (float) ((defaultY_T + middle) + diffWidth[i % diffWidth.length] * middle);
            float heightT = (float) ((defaultY_T + middle) - diffWidth[i % diffWidth.length] * middle);
            c.drawLine(totalWidth - viewWidth / 2, heightT, totalWidth - viewWidth / 2, heightB, paint_white);
        }
        return bm;
    }
}
