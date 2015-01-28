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
import android.widget.TextView;
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


public class MainActivity extends Activity  implements
        ViSearch.ResultListener, CameraPreview.ImageCapturedCallback{

    private static final String MAIN_ACTIVITY = "CameraActivity";
    private static final int RESULT_LOAD_IMAGE_FROM_GALLERY = 0x00;

    //Please change to your access key / secret Key pair
    private static final String ACCESS_KEY = "your_access_key";
    private static final String SECRET_KEY = "your_secret_key";

    //Inject UI
    @InjectView(R.id.camera_preview)        CameraPreview       cameraPreview;

    @InjectView(R.id.camera_image_preview)  ImageView           imagePreview;

    @InjectView(R.id.camera_scan_image)     ImageView           scanImage;

    @InjectViews({  R.id.camera_light_button,
                    R.id.camera_album_button,
                    R.id.camera_shutter_button}) List<ImageView> cameraUIs;

    @InjectView(R.id.camera_light_button)   ImageView           lightButton;

    @InjectView(R.id.camera_cancel_layout)  FrameLayout         cancelLayout;

    @InjectView(R.id.camera_cancel_button)  TextView            cancelButton;

    //Visearch
    private ViSearch                    viSearch;
    private Image                       image;
    private UploadSearchParams          uploadSearchParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        //get ViSearcher instance
        viSearch = new ViSearch.Builder(ACCESS_KEY, SECRET_KEY).build(this);
        viSearch.setListener(this);
    }

    //camera captured image
    @Override
    public void OnImageCaptured(byte[] bytes) {
        image = new Image(bytes);
        uploadSearchParams = new UploadSearchParams(image);

        viSearch.uploadSearch(uploadSearchParams);
    }

    //result from photo gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();

            changeUploadUI();
            cameraPreview.setVisibility(View.GONE);
            imagePreview.setVisibility(View.VISIBLE);
            imagePreview.setImageURI(uri);

            image = new Image(this, uri);

            uploadSearchParams = new UploadSearchParams(image);

            viSearch.uploadSearch(uploadSearchParams);

        }
    }

    @Override
    public void onSearchResult(ResultList resultList) {
        changeUploadUIBack();
        if (resultList.getImageList().size() > 0)
            Toast.makeText(this, resultList.getImageList().get(0).getImageName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchError(String errorMessage) {
        changeUploadUIBack();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchCanceled() {
        changeUploadUIBack();
    }

    /**
     * UI implementations
     */
    @OnClick(R.id.camera_album_button)
    public void openGallery() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(openAlbumIntent, RESULT_LOAD_IMAGE_FROM_GALLERY);
    }

    @OnClick(R.id.camera_light_button)
    public void startFlashLight() {
        lightButton.setSelected(cameraPreview.turnOnTorch());
    }

    @OnClick(R.id.camera_shutter_button)
    public void shutterClicked() {
        changeUploadUI();
        cameraPreview.takePhoto(MainActivity.this);
    }

    @OnClick(R.id.camera_cancel_button)
    public void cancelSearch() {
        viSearch.cancelSearch();
    }

    static final ButterKnife.Action<View> SHOW = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    static final ButterKnife.Action<View> HIDE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    //change UI when uploading starts
    private void changeUploadUI() {
        ButterKnife.apply(cameraUIs, HIDE);

        cancelLayout.setVisibility(View.VISIBLE);

        //start scan
        scanImage.setVisibility(View.VISIBLE);
        Animation scanAnim = AnimationUtils.loadAnimation(this, R.anim.scan_anim);
        scanImage.startAnimation(scanAnim);
        Log.d("Camera", "start scan animation");

    }

    //When cancel button is clicked, bring UI back
    private void changeUploadUIBack() {
        ButterKnife.apply(cameraUIs, SHOW);

        lightButton.setSelected(false);

        //hide cancel UI
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
