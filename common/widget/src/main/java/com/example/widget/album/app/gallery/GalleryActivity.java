/*
 * Copyright 2017 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.widget.album.app.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.widget.album.Action;
import com.example.widget.album.Album;
import com.example.widget.album.ItemAction;
import com.example.widget.R;
import com.example.widget.album.api.widget.Widget;
import com.example.widget.album.app.Contract;
import com.example.widget.album.mvp.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by example.widget on 2017/8/16.
 */
public class GalleryActivity extends BaseActivity implements Contract.GalleryPresenter {

    public static Action<ArrayList<String>> sResult;
    public static Action<String> sCancel;

    public static ItemAction<String> sClick;
    public static ItemAction<String> sLongClick;

    private Widget mWidget;
    private ArrayList<String> mPathList;
    private int mCurrentPosition;
    private boolean mCheckable;

    private Map<String, Boolean> mCheckedMap;

    private Contract.GalleryView<String> mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity_gallery);
        mView = new GalleryView<>(this, this);

        Bundle argument = getIntent().getExtras();
        assert argument != null;
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        mPathList = argument.getStringArrayList(Album.KEY_INPUT_CHECKED_LIST);
        mCurrentPosition = argument.getInt(Album.KEY_INPUT_CURRENT_POSITION);
        mCheckable = argument.getBoolean(Album.KEY_INPUT_GALLERY_CHECKABLE);

        mCheckedMap = new HashMap<>();
        for (String path : mPathList) mCheckedMap.put(path, true);

        mView.setTitle(mWidget.getTitle());
        mView.setupViews(mWidget, mCheckable);
        if (!mCheckable) mView.setBottomDisplay(false);
        mView.setLayerDisplay(false);
        mView.setDurationDisplay(false);
        mView.bindData(mPathList);

        if (mCurrentPosition == 0) {
            onCurrentChanged(mCurrentPosition);
        } else {
            mView.setCurrentItem(mCurrentPosition);
        }
        setCheckedCount();
    }

    private void setCheckedCount() {
        int checkedCount = 0;
        for (Map.Entry<String, Boolean> entry : mCheckedMap.entrySet()) {
            if (entry.getValue()) checkedCount += 1;
        }

        String completeText = getString(R.string.album_menu_finish);
        completeText += "(" + checkedCount + " / " + mPathList.size() + ")";
        mView.setCompleteText(completeText);
    }

    @Override
    public void clickItem(int position) {
        if (sClick != null) {
            sClick.onAction(GalleryActivity.this, mPathList.get(mCurrentPosition));
        }
    }

    @Override
    public void longClickItem(int position) {
        if (sLongClick != null) {
            sLongClick.onAction(GalleryActivity.this, mPathList.get(mCurrentPosition));
        }
    }

    @Override
    public void onCurrentChanged(int position) {
        mCurrentPosition = position;
        mView.setSubTitle(position + 1 + " / " + mPathList.size());

        if (mCheckable) mView.setChecked(mCheckedMap.get(mPathList.get(position)));
    }

    @Override
    public void onCheckedChanged() {
        String path = mPathList.get(mCurrentPosition);
        mCheckedMap.put(path, !mCheckedMap.get(path));

        setCheckedCount();
    }

    @Override
    public void complete() {
        if (sResult != null) {
            ArrayList<String> checkedList = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : mCheckedMap.entrySet()) {
                if (entry.getValue()) checkedList.add(entry.getKey());
            }
            sResult.onAction(checkedList);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (sCancel != null) sCancel.onAction("User canceled.");
        finish();
    }

    @Override
    public void finish() {
        sResult = null;
        sCancel = null;
        sClick = null;
        sLongClick = null;
        super.finish();
    }
}