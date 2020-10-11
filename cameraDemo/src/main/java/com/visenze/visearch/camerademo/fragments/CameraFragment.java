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

package com.visenze.visearch.camerademo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.Image;
import com.visenze.visearch.camerademo.EditPhotoActivity;
import com.visenze.visearch.camerademo.R;
import com.visenze.visearch.camerademo.http.SearchAPI;
import com.visenze.visearch.camerademo.util.Config;
import com.visenze.visearch.camerademo.util.DataHelper;
import com.visenze.visearch.camerademo.util.ImageHelper;
import com.visenze.visearch.camerademo.util.IntentHelper;
import com.visenze.visearch.camerademo.views.CameraPreview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * Created by yulu on 2/17/15.
 *
 * camera view and search actions control is implemented in this fragment 
 */
public class CameraFragment extends Fragment implements
        ViSearch.ResultListener, CameraPreview.ImageCapturedCallback {

    /**
     * UI action: enable a group of UIs---------------------------------------------------------
     */
    static final ButterKnife.Action<View> ENABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setEnabled(true);
        }
    };
    /**
     * UI action: disable a group of UIs
     */
    static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setEnabled(false);
        }
    };
    /**
     * UI action: show a group of UIs
     */
    static final ButterKnife.Action<View> SHOW = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };
    /**
     * UI action: hide a group of UIs
     */
    static final ButterKnife.Action<View> HIDE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 0x00;
    //Inject UIs
    @InjectView(R.id.camera_preview)            CameraPreview       cameraPreview;
    @InjectView(R.id.camera_image_preview)      ImageView           imagePreview;
    @InjectView(R.id.camera_loading)            ImageView           loadingImage;
    @InjectView(R.id.camera_flash_button)       ImageView           lightButton;
    @InjectViews( { R.id.camera_cancel_button,
                    R.id.camera_loading_background,
                    R.id.camera_loading})       List<View>          searchUIs;
    @InjectViews({  R.id.camera_flash_button,
                    R.id.camera_album_button,
                    R.id.camera_switch_button,
                    R.id.camera_shutter_button,
                    R.id.camera_close_button})  List<ImageView>     cameraUIs;
    //ViSearch and Search parameters
    private ViSearch viSearch;
    private String                      imagePath;
    private UploadSearchParams uploadSearchParams;
    //photo loading and process runnable
    private ImageProcessRunnable        imageProcessRunnable;
    
    /**
     * Constructor
     *
     * @return new instance of CameraFragment
     */
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        //can set some properties for the fragment
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_main, container, false);
        ButterKnife.inject(this, view);

        try {
            viSearch = SearchAPI.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        viSearch.setListener(this);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        cameraPreview.stopPreview();
        Log.i("CAMERA", "camera fragment stop");
    }
    
    /**
     * Image selection activity callback:
     *
     * get the image Uri from intent and pass to upload search params to start search
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK && null != data) {
            Uri uri = data.getData();

            changeUploadUI();
            cameraPreview.setVisibility(View.GONE);
            imagePreview.setVisibility(View.VISIBLE);

            int rot = ImageHelper.getRotation(uri, getActivity());
            imagePreview.setRotation(rot);
            imagePreview.setImageURI(uri);

            imageProcessRunnable = new ImageProcessRunnable(uri);
            imageProcessRunnable.start();
        }
    }
    
    /**
     * Camera preview captured callback:
     *
     * Pass the byte array to upload search params and start search
     */
    @Override
    public void OnImageCaptured(Image image, String imagePath) {
        changeUploadUI();

        this.imagePath = imagePath;

        uploadSearchParams = new UploadSearchParams(image);
        DataHelper.setSearchParams(uploadSearchParams, "all");

        viSearch.uploadSearch(uploadSearchParams);
    }

    /**
     * search result callback:
     *
     * get the list of result image and do something
     * check if the result if from camera or photo gallery by checking
     * imagePath and uri are null or not
     *
     * Change the UI back to searching mode
     */
    @Override
    public void onSearchResult(ResultList resultList) {
        IntentHelper.addObjectForKey(resultList, IntentHelper.SEARCH_RESULT_EXTRA);
        IntentHelper.addObjectForKey(imagePath, IntentHelper.SEARCH_THUMBNAIL_PATH_EXTRA);
        IntentHelper.addObjectForKey(imagePath, IntentHelper.SEARCH_IMAGE_PATH_EXTRA);

        Intent intent = new Intent(getActivity(), EditPhotoActivity.class);
        startActivity(intent);

        Intent thisIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, thisIntent);
        getActivity().finish();
    }
    
    /**
     * search error callback:
     *
     * called when there is an error after upload search
     * display the error and change the UI to camera mode
     */
    @Override
    public void onSearchError(String errorMessage) {
        if (isAdded()) {
            changeUploadUIBack();
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * search cancel callback:
     *
     * change the UI to camera mode when search is canceled
     */
    @Override
    public void onSearchCanceled() {
        if (isAdded()) {
            changeUploadUIBack();
        }
    }

    /**
     * Belows are button click action implementations and UI change handling-----------------------
     */
    @OnClick(R.id.camera_close_button)
    public void close() {
        if (imageProcessRunnable != null) {
            try {
                imageProcessRunnable.stop();
                Log.d("Camera Fragment: ", "stop photo loading and processing thread");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        viSearch.cancelSearch();

        getActivity().finish();
    }

    @OnClick(R.id.camera_album_button)
    public void openGallery() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(openAlbumIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
    }

    @OnClick(R.id.camera_flash_button)
    public void startFlashLight() {
        lightButton.setSelected(cameraPreview.turnOnTorch());
    }

    @OnClick(R.id.camera_switch_button)
    public void switchCamera() {
        lightButton.setSelected(false);
        cameraPreview.switchCamera();
    }

    @OnClick(R.id.camera_shutter_button)
    public void shutterClicked() {
        disableUploadUI();
        cameraPreview.takePhoto(this);
    }

    @OnClick(R.id.camera_cancel_button)
    public void cancelSearch() {
        if (imageProcessRunnable != null) {
            try {
                imageProcessRunnable.stop();
                Log.d("Camera Fragment: ", "stop photo loading and processing thread");
            } catch (InterruptedException e) {
                Log.d("Interruption exception ", e.getMessage());
            }
        }

        viSearch.cancelSearch();
    }

    //change UI when uploading starts
    private void disableUploadUI() {
        //disable
        ButterKnife.apply(cameraUIs, DISABLE);
    }

    private void changeUploadUI() {
        ButterKnife.apply(cameraUIs, HIDE);
        ButterKnife.apply(searchUIs, SHOW);

        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        anim.start();
    }

    //When cancel button is clicked, bring UI back and enable
    private void changeUploadUIBack() {
        ButterKnife.apply(cameraUIs, SHOW);
        ButterKnife.apply(cameraUIs, ENABLE);
        ButterKnife.apply(searchUIs, HIDE);

        //set light to turn off state
        lightButton.setSelected(false);

        //hide cancel UI and preview image
        imagePreview.setVisibility(View.GONE);

        //stop animation and hide
        loadingImage.setVisibility(View.GONE);

        //restart camera preview
        cameraPreview.setVisibility(View.VISIBLE);

        cameraPreview.startCameraPreview();
    }

    private class ImageProcessRunnable implements Runnable {
        private Thread thread;
        private Uri _uri;

        public ImageProcessRunnable(Uri uri) {
            this._uri = uri;
        }

        @Override
        public void run() {
            try {
                byte[] bytes = ImageHelper.readBytes(_uri, getActivity(), Config.IMAGE_QUALITY);
                CameraFragment.this.imagePath = ImageHelper.saveImageByteTmp(getActivity(), bytes);
                Image image = new Image(bytes, Config.IMAGE_QUALITY);

                uploadSearchParams = new UploadSearchParams(image);
                DataHelper.setSearchParams(uploadSearchParams, "all");

                viSearch.uploadSearch(uploadSearchParams);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void start() {
            if (thread == null) {
                thread = new Thread(this, "photo load from gallery in worker thread");
                thread.start();
            }
        }

        public void stop() throws InterruptedException {
            if (thread != null && thread.isAlive())
                thread.join();
        }
    }
}