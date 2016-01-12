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

package com.visenze.visearch.camerademo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.visenze.visearch.camerademo.fragments.DetailFragment;
import com.visenze.visearch.camerademo.util.IntentHelper;

/**
 * Created by visenze on 18/12/15.
 */
public class DetailActivity extends FragmentActivity {
    //parameters passed in from result activity
    private String                      imName;
    private String                      url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        url = (String) IntentHelper.getObjectForKey(IntentHelper.SEARCH_IMAGE_PATH_EXTRA);
        imName = (String) IntentHelper.getCachedObjectForKey(IntentHelper.SEARCH_RESULT_EXTRA);

        //add fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_holder, DetailFragment.newInstance());
        fragmentTransaction.commit();
    }

    public String getImName() {
        return imName;
    }

    public String getUrl() {
        return url;
    }
}
