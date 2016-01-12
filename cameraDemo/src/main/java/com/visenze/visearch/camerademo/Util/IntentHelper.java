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

import java.util.Hashtable;

/**
 * Created by yulu on 16/2/15.
 * 
 * hash table save datas that can be transfered between activities
 */
public class IntentHelper {
    public static final String SEARCH_IMAGE_PATH_EXTRA = "image_path";
    public static final String SEARCH_THUMBNAIL_PATH_EXTRA = "thumbnail_path";
    public static final String SEARCH_RESULT_EXTRA = "result_list";

    private static IntentHelper _instance;
    private Hashtable<String, Object> _hash;

    private IntentHelper() {
        _hash = new Hashtable<String, Object>();
    }
    
    private static IntentHelper getInstance() {
        if(_instance==null) {
            _instance = new IntentHelper();
        }
        return _instance;
    }

    /**
     * A container to hold the object temprarely
     * @param object
     * @param key
     */
    public static void addObjectForKey(Object object, String key) {
        getInstance()._hash.put(key, object);
    }

    /**
     * Get the temp object using the key
     * @param key
     * @return
     */
    public static Object getObjectForKey(String key) {
        IntentHelper helper = getInstance();
        Object data = helper._hash.get(key);
        helper._hash.remove(key);
        helper = null;
        return data;
    }

    public static void addCachedObjectForKey(Object object, String key) {
        IntentHelper helper = getInstance();
        if (getObjectForKey(key) != null) {
            helper._hash.remove(key);
        }
        helper._hash.put(key, object);
    }

    public static Object getCachedObjectForKey(String key) {
        IntentHelper helper = getInstance();
        Object data = helper._hash.get(key);
        helper = null;
        return data;
    }
}
