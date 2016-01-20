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
package com.visenze.visearch.camerademo.views.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.visenze.visearch.camerademo.R;

import java.util.List;

/*
 * List adapter to load and manage list of products in horizontal scroll view
 */
public class HorizontalProductTypeArrayAdapter extends ArrayAdapter<String> {

    private Activity context;
    private List<String> productTypeList;
    private int selected;

    public HorizontalProductTypeArrayAdapter(Activity context, List<String> productTypeList) {
        super(context, R.layout.result_layout, productTypeList);
        this.productTypeList = productTypeList;
        this.context = context;
        this.selected = 0;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.horizontal_item, null);

            final Holder holder = new Holder();
            holder.textView = (TextView) view.findViewById(R.id.horizontal_item_text);

            view.setTag(holder);

        } else {
            view = convertView;
        }

        Holder holder = (Holder)view.getTag();
        holder.textView.setText(productTypeList.get(position).replace("_", " "));

        if (selected == position) {
            holder.textView.setTextColor(getContext().getResources().getColor(R.color.red));
            holder.textView.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_tag_selected));
        } else {
            holder.textView.setTextColor(getContext().getResources().getColor(R.color.gray));
            holder.textView.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_tag));
        }

        return view;
    }

    public void setSelected(int s) {
        if (s >= 0 && s < productTypeList.size()) {
            selected = s;
            notifyDataSetInvalidated();
        }
    }

    /**
     * View holder for the views we need access to
     */
    private class Holder {
        public TextView textView;
    }
}
