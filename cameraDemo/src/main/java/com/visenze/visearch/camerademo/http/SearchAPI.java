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

package com.visenze.visearch.camerademo.http;

import android.content.Context;

import com.visenze.visearch.android.ViSearch;

/**
 * search api
 * Created by yulu on 11/21/14.
 */
public class SearchAPI {
    private static ViSearch viSearch;


    public static ViSearch getInstance() throws Exception {
        if (viSearch == null) {
            throw new Exception("init instance before use");
        }

        return viSearch;
    }

    /**
     * Initialise the ViSearcher with a valid access/secret key pair
     *
     * @param context   Activity context
     * @param accessKey the Access Key
     * @param secretKey the Secret Key
     */
    public static void initSearchAPI(Context context, String accessKey, String secretKey) {
        viSearch = new ViSearch.Builder(accessKey, secretKey).build(context);
    }
}
