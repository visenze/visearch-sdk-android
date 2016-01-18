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

package com.visenze.visearch.camerademo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by yulu on 22/11/15.
 */
// This works around the issue described here: http://stackoverflow.com/a/12675430/265521
public class StretchyImageView extends ImageView {

    public StretchyImageView(Context context)
    {
        super(context);
    }

    public StretchyImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public StretchyImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // Call super() so that resolveUri() is called.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // If there's no drawable we can just use the result from super.
        if (getDrawable() == null)
            return;

        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = getDrawable().getIntrinsicWidth();
        int h = getDrawable().getIntrinsicHeight();
        if (w <= 0)
            w = 1;
        if (h <= 0)
            h = 1;

        // Desired aspect ratio of the view's contents (not including padding)
        float desiredAspect = (float) w / (float) h;

        // We are allowed to change the view's width
        boolean resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;

        // We are allowed to change the view's height
        boolean resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;

        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        // Get the sizes that ImageView decided on.
        int widthSize = getMeasuredWidth();
        int heightSize = getMeasuredHeight();

        if (resizeWidth && !resizeHeight)
        {
            // Resize the width to the height, maintaining aspect ratio.
            int newWidth = (int) (desiredAspect * (heightSize - ptop - pbottom)) + pleft + pright;
            setMeasuredDimension(newWidth, heightSize);
        }
        else if (resizeHeight && !resizeWidth)
        {
            int newHeight = (int) ((widthSize - pleft - pright) / desiredAspect) + ptop + pbottom;
            setMeasuredDimension(widthSize, newHeight);
        }
    }
}
