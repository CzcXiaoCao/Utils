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
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meidalibrary.R;
import com.example.meidalibrary.bean.MediaData;
import com.example.meidalibrary.interfaces.OnItemClick;

import java.util.List;

/**
 * An extension of SectionedRecyclerViewAdapter for simple sectioned RecyclerViews. In most cases,
 * you will not need a footer for your sections and your header will consist only of a TextView.
 * SimpleSectionedAdapter simplifies the creation of such sectioned collections where you only
 * need to provide header titles and implement the rendering of your items.
 */
public abstract class SimpleSectionedAdapter<VH extends RecyclerView.ViewHolder> extends SectionedRecyclerViewAdapter<HeaderViewHolder,
        VH, RecyclerView.ViewHolder>{

    public void setClickType(int clickType) {
        this.clickType = clickType;
    }

    private int clickType;
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(getLayoutResource(), parent, false);
        HeaderViewHolder holder = new HeaderViewHolder(view, getTitleTextID(),clickType);

        return holder;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HeaderViewHolder holder, final int section) {
        holder.itemView.setTag("第几行的：       "+section);
        String title = getSectionHeaderTitle(section);
        holder.render(title,getList().get(section).isState());
        holder.onClick(section,getHeadItemCick());
    }

    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {}

    /**
     * Provides a layout identifier for the header. Override it to change the appearance of the
     * header view.
     */
    protected @LayoutRes
    int getLayoutResource(){
        return R.layout.item_head_media;
    }

    /**
     * Provides the identifier of the TextView to render the section header title. Override it if
     * you provide a custom layout for a header.
     */
    protected @IdRes
    int getTitleTextID(){
        return R.id.meida_head_time;
    }

    /**
     * Returns the title for a given section
     */
    protected abstract String getSectionHeaderTitle(int section);
    protected abstract List<MediaData> getList();
    protected abstract OnItemClick getHeadItemCick( );
}
