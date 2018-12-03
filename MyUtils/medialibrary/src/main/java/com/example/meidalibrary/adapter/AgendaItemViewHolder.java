/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.meidalibrary.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.meidalibrary.R;
import com.example.meidalibrary.bean.MediaData;
import com.example.meidalibrary.interfaces.OnItemClick;
import com.example.meidalibrary.util.MediaConstant;
import com.example.meidalibrary.util.ScreenUtils;
import com.example.meidalibrary.util.TimeUtil;

import java.io.File;

public class AgendaItemViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    ImageView iv_item_image;
    RelativeLayout item_media_hideLayout;
    TextView tv_selected_num;
    private int mClickType;
    public AgendaItemViewHolder(View itemView, int type) {
        super(itemView);
        this.mClickType = type;
        textView = (TextView) itemView.findViewById(R.id.tv_media_type);
        tv_selected_num = (TextView) itemView.findViewById(R.id.tv_selected_num);
        iv_item_image = (ImageView) itemView.findViewById(R.id.iv_item_image);
        item_media_hideLayout = (RelativeLayout) itemView.findViewById(R.id.item_media_hideLayout);
    }

    public void render(Context context, MediaData mediaData, final int se, final int position, final OnItemClick onItemClick) {
        //设置当前的图片为正方形
        int width = ScreenUtils.getWindowWidth(context);
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(width / 4, width / 4);
        itemView.setLayoutParams(param);
        if (mediaData.getDuration() / 1000 >= 1) {
            textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            textView.getPaint().setAntiAlias(true);//抗锯齿
            textView.setText(TimeUtil.secToTime((int) (mediaData.getDuration() / 1000)));
        } else {
            textView.setVisibility(View.GONE);
        }
        item_media_hideLayout.setVisibility(mediaData.isState() ? View.VISIBLE : View.GONE);
        if (mClickType == MediaConstant.TYPE_ITEMCLICK_SINGLE){
            tv_selected_num.setVisibility(View.GONE);
        }else {
            tv_selected_num.setText(mediaData.getPosition()+"");
        }
        File file = new File(mediaData.getPath());
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.bank_thumbnail_local);
        Glide.with(iv_item_image.getContext())
                .asBitmap()
                .load(file)
                .apply(options)
                .into(iv_item_image);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnItemClick(itemView, se, position);
            }
        });
    }
}
