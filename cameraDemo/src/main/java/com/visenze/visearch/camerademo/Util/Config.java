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

package com.visenze.visearch.camerademo.util;

import com.visenze.visearch.android.model.Image;

/**
 * Created by yulu on 2/17/15.
 * 
 * App Configuration Constants 
 */
public class Config {
    /**
     * image dimension used to resize the downloaded image for search result as grid view 
     */
    public static final int IMAGE_DISPLAY_SIZE = 200;

    /**
     * quality of the image for uploading, now set to high
     */
    public static final Image.ResizeSettings CAMERA_IMAGE_QUALITY = Image.ResizeSettings.CAMERA_HIGH;
    public static final Image.ResizeSettings IMAGE_QUALITY = Image.ResizeSettings.HIGH;

    /**
     * size of photo
     */
    public static final int PHOTO_TAKEN_SIZE = 1024;

    /**
     * image dir
     */
    public static final String ALBUM_NAME = "Visenze/";
    public static final String IMAGE_NAME = "visenze_demo";
}
