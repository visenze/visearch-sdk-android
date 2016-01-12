/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ViSenze Pte. Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.visenze.visearch.camerademo.Views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.camerademo.R;

import java.util.List;

/**
 * Created by visenze on 15/12/15.
 */
public class StrechImageAdapter extends ArrayAdapter<ImageResult> {
    private Activity mContext;
    private List<ImageResult> imageList;

    public StrechImageAdapter(Activity c, List<ImageResult> imageList) {
        super(c, R.layout.result_layout, imageList);
        this.mContext = c;
        this.imageList = imageList;
    }

    // create a new imageLayout for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View imageLayout = convertView;
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();

            imageLayout = inflater.inflate(R.layout.grid_item, null);
            viewHolder = new ViewHolder();

            viewHolder.scoreView = (TextView)imageLayout.findViewById(R.id.score_view);
            viewHolder.imageView = (ImageView)imageLayout.findViewById(R.id.result_image_view);

            imageLayout.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)imageLayout.getTag();
        }

        Picasso.with(mContext)
                .load(imageList.get(position).getImageUrl())
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .tag(mContext)
                .into(viewHolder.imageView);

        int score = (int)(imageList.get(position).getScore() * 100);
        viewHolder.scoreView.setText("Similarity " + String.valueOf(score) + "%");

        return imageLayout;
    }

    class ViewHolder {
        public TextView scoreView;
        public ImageView imageView;
    }
}
