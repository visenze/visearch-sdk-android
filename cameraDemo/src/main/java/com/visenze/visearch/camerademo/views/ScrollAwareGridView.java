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

package com.visenze.visearch.camerademo.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * scroll aware grid view
 * Created by yulu on 11/25/14.
 */

public class ScrollAwareGridView extends GridViewWithHeaderAndFooter {
    private static final String SCROLL_AWARE_GRID_VIEW = "scroll aware grid view";

    private OnScrollListener onScrollListener;
    private OnDetectScrollListener onDetectScrollListener;
    private static int BUFFER = 10;

    public interface OnDetectScrollListener {

        void onUpScrolling();

        void onDownScrolling();

        void onTopReached();

        void onBottomReached();

    }


    public ScrollAwareGridView(Context context) {
        super(context);
        initView();
    }

    public ScrollAwareGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ScrollAwareGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setListeners();
    }

    private void setListeners() {
        super.setOnScrollListener(new OnScrollListener() {

            private int oldTop;
            private int oldFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onScrollListener != null) {
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollListener != null) {
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                if (view != null && view.getChildAt(0) != null && onDetectScrollListener != null) {
                    if (view.getChildAt(0).getTop() == 0) {
                        onDetectScrollListener.onTopReached();
                    }
                    else if ((firstVisibleItem + visibleItemCount) >= totalItemCount) {
                        onDetectScrollListener.onBottomReached();
                        Log.d(SCROLL_AWARE_GRID_VIEW, "bottom reached");
                    }
                    else
                        onDetectedListScroll(view, firstVisibleItem);
                }
            }

            private void onDetectedListScroll(AbsListView absListView, int firstVisibleItem) {
                View view = absListView.getChildAt(0);
                int top = (view == null) ? 0 : view.getTop();

                if (firstVisibleItem == oldFirstVisibleItem) {

                    if (top - oldTop > BUFFER) {
                        onDetectScrollListener.onUpScrolling();
                    } else if (top - oldTop < -BUFFER) {
                        onDetectScrollListener.onDownScrolling();
                    }
                } else {
                    if (firstVisibleItem < oldFirstVisibleItem) {
                        onDetectScrollListener.onUpScrolling();
                    } else {
                        onDetectScrollListener.onDownScrolling();
                    }
                }

                oldTop = top;
                oldFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setOnDetectScrollListener(OnDetectScrollListener onDetectScrollListener) {
        this.onDetectScrollListener = onDetectScrollListener;
    }

}
