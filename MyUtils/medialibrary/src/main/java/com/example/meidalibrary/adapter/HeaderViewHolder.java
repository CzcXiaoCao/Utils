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

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.meidalibrary.R;
import com.example.meidalibrary.interfaces.OnItemClick;
import com.example.meidalibrary.util.MediaConstant;

/**
 * Created by tomas on 01/06/15.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView titleText = null;
    private TextView select_all;
    private int clickType;
    public HeaderViewHolder(View itemView, @IdRes int titleID, int type) {
        super(itemView);
        titleText = (TextView) itemView.findViewById(titleID);
        select_all = (TextView) itemView.findViewById(R.id.meida_head_selectAll);
        clickType = type;
    }

    public void render(String title, boolean state){
        titleText.setText(title);
        if (clickType == MediaConstant.TYPE_ITEMCLICK_MULTIPLE){
            select_all.setText(state? "取消全选": "全选");
        }else if (clickType == MediaConstant.TYPE_ITEMCLICK_SINGLE){
            select_all.setText("");
        }
    }
    public void onClick(final int  section, final OnItemClick onItemClick){
        select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.OnHeadClick(itemView,section);
            }
        });
    }


}
