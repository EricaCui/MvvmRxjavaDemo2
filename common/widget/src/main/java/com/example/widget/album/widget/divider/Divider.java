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
package com.example.widget.album.widget.divider;

import android.support.v7.widget.RecyclerView;

/**
 * <p>Divider of {@code RecyclerView}, you can get the width and height of the line.</p>
 * Created by YanZhenjie on 2017/8/16.
 */
public abstract class Divider extends RecyclerView.ItemDecoration {

    /**
     * Get the height of the divider.
     *
     * @return height of the divider.
     */
    public abstract int getHeight();

    /**
     * Get the width of the divider.
     *
     * @return width of the divider.
     */
    public abstract int getWidth();

}