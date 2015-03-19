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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.visenze.visearch.android.Image;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

/**
 * Main activity: start camera and implement upload search function
 * Created by yulu on 1/28/15. 
 */
public class MainActivity extends Activity implements
        ViSearch.ResultListener, CameraPreview.ImageCapturedCallback {
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 0x00;

    //TODO: Please change to your access key / secret key pair
    private static final String ACCESS_KEY = "your_access_key";
    private static final String SECRET_KEY = "your_secret_key";

    //Inject UIs
    @InjectView(R.id.camera_preview)        CameraPreview       cameraPreview;
    @InjectView(R.id.camera_image_preview)  ImageView           imagePreview;
    @InjectView(R.id.camera_scan_image)     ImageView           scanImage;
    @InjectView(R.id.camera_flash_button)   ImageView           lightButton;
    @InjectView(R.id.camera_cancel_layout)  FrameLayout         cancelLayout;
    @InjectViews({  R.id.camera_flash_button,
                    R.id.camera_album_button,
                    R.id.camera_shutter_button})List<ImageView> cameraUIs;
    
    //ViSearch and Search parameters
    private ViSearch                    viSearch;
    private UploadSearchParams          uploadSearchParams;

    //photo loading and process runnable
    private ImageProcessRunnable        imageProcessRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        //get ViSearcher instance and set listener
        viSearch = new ViSearch.Builder(ACCESS_KEY, SECRET_KEY).build(this);
        viSearch.setListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        cameraPreview.stopPreview();
    }

    /**
     * Camera preview captured callback:
     * 
     * Pass the byte array to upload search params and start search 
     */
    @Override
    public void OnImageCaptured(Image image) {
        changeUploadUI();
        uploadSearchParams = new UploadSearchParams(image);

        viSearch.uploadSearch(uploadSearchParams);
    }

    /**
     * Image selection activity callback:
     * 
     * get the image Uri from intent and pass to upload search params to start search 
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();

            changeUploadUI();
            cameraPreview.setVisibility(View.GONE);
            imagePreview.setVisibility(View.VISIBLE);
            imagePreview.setImageURI(uri);

            imageProcessRunnable = new ImageProcessRunnable(uri);
            imageProcessRunnable.start();
        }
    }

    /**
     * search result callback:
     * 
     * get the list of result image and do something 
     */
    @Override
    public void onSearchResult(ResultList resultList) {
        changeUploadUIBack();
        if (resultList.getImageList().size() > 0)
            Toast.makeText(this, resultList.getImageList().get(0).getImageName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * search error callback:
     * 
     * called when there is an error after upload search
     * display the error and change the UI to camera mode
     */
    @Override
    public void onSearchError(String errorMessage) {
        changeUploadUIBack();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * search cancel callback:
     * 
     * change the UI to camera mode when search is canceled 
     */
    @Override
    public void onSearchCanceled() {
        changeUploadUIBack();
    }

    private class ImageProcessRunnable implements Runnable {
        private Thread thread;
        private Uri _uri;

        public ImageProcessRunnable(Uri uri) {
            this._uri = uri;
        }

        @Override
        public void run() {
            Image image = new Image(MainActivity.this, _uri, Image.ResizeSettings.STANDARD);

            uploadSearchParams = new UploadSearchParams(image);
            viSearch.uploadSearch(uploadSearchParams);
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

    /**
     * Belows are button click action implementations and UI change handling-----------------------
     */
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
                e.printStackTrace();
            }
        }

        viSearch.cancelSearch();
    }

    //change UI when uploading starts
    private void disableUploadUI() {
        //disable
        ButterKnife.apply(cameraUIs, DISABLE);

    }

    //change UI when uploading starts
    private void changeUploadUI() {
        ButterKnife.apply(cameraUIs, HIDE);

        //show cancel UI
        cancelLayout.setVisibility(View.VISIBLE);

        //start scan
        scanImage.setVisibility(View.VISIBLE);
        Animation scanAnim = AnimationUtils.loadAnimation(this, R.anim.scan_anim);
        scanImage.startAnimation(scanAnim);
    }

    //When cancel button is clicked, bring UI back
    private void changeUploadUIBack() {
        ButterKnife.apply(cameraUIs, SHOW);
        ButterKnife.apply(cameraUIs, ENABLE);

        //set light to turn off state
        lightButton.setSelected(false);

        //hide cancel UI and preview image
        cancelLayout.setVisibility(View.GONE);
        imagePreview.setVisibility(View.GONE);

        //stop animation and hide
        scanImage.clearAnimation();
        scanImage.setVisibility(View.GONE);

        //restart camera preview
        cameraPreview.setVisibility(View.VISIBLE);
        cameraPreview.startCameraPreview();
    }
}
