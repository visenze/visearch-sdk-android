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

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.visenze.visearch.android.model.Image;
import com.visenze.visearch.camerademo.util.Config;
import com.visenze.visearch.camerademo.util.ImageHelper;

import java.util.List;

import static android.view.SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS;

/**
 * Created by yulu on 1/28/15.
 * 
 * CameraPreview module, can be initialized in layout XML file
 *  
 */
public class CameraPreview extends SurfaceView implements 
        SurfaceHolder.Callback, Camera.PictureCallback, Camera.AutoFocusCallback {

    private static final int CAMERA_FACING_FRONT = 1;
    private static final int CAMERA_FACING_BACK = 0;


    /**
     * Screen orientation
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    /**
     * Focus mode
     */
    private static final String FOCUS_MODE = Camera.Parameters.FOCUS_MODE_AUTO;
    /**
     * Flag to control flash on and off
     */
    private static boolean      LightOn = false;
    /**
     * Flag to control back and front camera
     */
    private static int          Facing = CAMERA_FACING_BACK;
    /**
     * Flag to control configuration
     */
    private static boolean      configured = false;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * surface holder size (need to be matched with camera preview size
     */
    private int                 mSurfaceWidth;
    private int                 mSurfaceHeight;

    /**
     * camera preview size, depend on system and hardware
     */
    private Camera.Size         frameSize;

    /**
     * photo size when taken picture 
     */
    private Camera.Size         pictureSize;

    /**
     * surface holder 
     */
    private SurfaceHolder       mHolder;

    /**
     * Camera hardware interface 
     */
    private Camera              mCamera;

    /**
     * image save and processing runnable 
     */
    private ImageRunnable       imageRunnable;

    /**
     * image captured callback interface
     */
    private ImageCapturedCallback imageCapturedCallback;

    private Context context;

    /**
     * Constructor
     * @param context activity context
     */
    public CameraPreview(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CameraPreview(Context context, AttributeSet attrs, int params) {
        super(context, attrs, params);
        this.context = context;
        initView();
    }

    /**
     * init the surface holder, done when constructing
     */
    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SURFACE_TYPE_PUSH_BUFFERS);

        //set click to focus
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    setAutoFocus();
                }
            }
        });
    }

    /**
     * Take photo
     * @param imageCapturedCallback the ImageCaptureCall to process the saved image
     */
    public void takePhoto(ImageCapturedCallback imageCapturedCallback) {
        this.imageCapturedCallback = imageCapturedCallback;

        if (mCamera != null) {
            mCamera.takePicture(null, null, null, this);
        }
    }

    /**
     * stop camera preview
     *
     * call this method only stop the camera preview, but only release
     * the camera module. The preview will be frozen in this case instead
     * of destroyed.
     */
    public void stopPreview() {
        if (mCamera != null)
            mCamera.stopPreview();
    }

    /**
     * Start preview with back camera and the previous setting of flash light
     *
     * call this method to start the preview if it has been stopped
     */
    public void startCameraPreview() {
        configureCamera(Facing);
        initializeCamera();
    }

    /**
     * Turn on or off the flash light
     *
     * @return flash light turn ON/OFF
     */
    public boolean turnOnTorch() {
        if (Facing == CAMERA_FACING_BACK) {
            if (LightOn) {
                LightOn = false;

                //set flash off
                Camera.Parameters parameters = mCamera.getParameters();
                List<String> FlashModes = parameters.getSupportedFlashModes();
                if (FlashModes != null && FlashModes.contains(Camera.Parameters.FLASH_MODE_OFF))
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

                mCamera.setParameters(parameters);
                mCamera.startPreview();

            } else {
                LightOn = true;

                //set flash on
                Camera.Parameters parameters = mCamera.getParameters();
                List<String> FlashModes = parameters.getSupportedFlashModes();
                if (FlashModes != null && FlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH))
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

                mCamera.setParameters(parameters);
                mCamera.startPreview();
            }
        }

        return LightOn;
    }

    public void switchCamera() {
        if (Facing == CAMERA_FACING_BACK) {
            Facing = CAMERA_FACING_FRONT;
            LightOn = false;
            configureCamera(Facing);
            initializeCamera();
        } else {
            Facing = CAMERA_FACING_BACK;
            LightOn = false;
            configureCamera(Facing);
            initializeCamera();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //do nothing
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        //surface size is always in landscape mode, need to inter-change w and h
        //check aspect ratio
        synchronized (this) {
            if ((getContext()).getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                mSurfaceHeight = h;
                mSurfaceWidth = w;
            } else {
                mSurfaceWidth = h;
                mSurfaceHeight = w;
            }

            Log.d("Camera", "holder resize: " + mSurfaceWidth + ", " + mSurfaceHeight);

            if (!configured) {
                configured = configureCamera(CAMERA_FACING_BACK);
                initializeCamera();
            } else
                initializeCamera();
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (frameSize != null) {
            //keep aspect ratio and scale holder size
//            scaleHolderSize();
            if ((getContext()).getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE) {
                setMeasuredDimension(mSurfaceWidth, mSurfaceHeight);
            } else {
                setMeasuredDimension(mSurfaceHeight, mSurfaceWidth);
            }
            Log.d("Camera", "holder resize with respect to camera: " + mSurfaceWidth + ", " + mSurfaceHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            Log.d("Camera", "holder without resize: " + mSurfaceWidth + ", " + mSurfaceHeight);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();

        //set configuration to false;
        configured = false;

        //stop image processing thread if it is running
        if (imageRunnable != null)
            try {
                imageRunnable.stop();
                imageRunnable = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        releaseCamera();

        //start image process in another thread
        imageRunnable = new ImageRunnable(context, bytes);
        imageRunnable.start();
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        Log.d("Camera", "auto focus");
    }

    /**
     * configure camera parameters
     * @param facing back or front facing
     * @return setting successful or not
     */
    private boolean configureCamera(int facing) {
        Log.d("Camera", "Configure camera information");
        synchronized (this) {
            if(mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }

            //open back or front camera
            int localCameraIndex = -1;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int localCameraFacingIdx;
            if (facing == CAMERA_FACING_FRONT)
                localCameraFacingIdx = Camera.CameraInfo.CAMERA_FACING_FRONT;
            else
                localCameraFacingIdx = Camera.CameraInfo.CAMERA_FACING_BACK;

            for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                Camera.getCameraInfo(camIdx, cameraInfo);

                if (cameraInfo.facing == localCameraFacingIdx) {
                    localCameraIndex = camIdx;
                    break;
                }
            }
            if (localCameraIndex != -1) {
                try {
                    mCamera = Camera.open(localCameraIndex);
                    Log.d("Camera", "Camera #" + localCameraIndex + " open this camera");
                } catch (RuntimeException e) {
                    Log.e("Camera", "Camera #" + localCameraIndex + " failed to open: " +
                            e.getLocalizedMessage());
                }
            } else {
                Log.e("Camera", "Back camera not found!");
                return false;
            }

            try {
                //get optimal size
                Camera.Parameters params = mCamera.getParameters();
                Log.d("Camera", "getSupportedPreviewSizes()");
                List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
                List<Camera.Size> picSizes = params.getSupportedPictureSizes();

                if (previewSizes != null && picSizes != null) {
                    // Select the size that fits surface considering maximum size allowed
                    frameSize = calculateCameraFrameSize(previewSizes, new CameraSizeAccessor(),
                            mSurfaceWidth, mSurfaceHeight);

                    params.setPreviewFormat(ImageFormat.NV21);
                    Log.d("Camera", "Set preview size to " + frameSize.width + "x" + frameSize.height);

                    // Select the size of picture taken that optimized for upload
                    pictureSize = calculateCameraFrameSize(picSizes, new CameraSizeAccessor(),
                            Config.PHOTO_TAKEN_SIZE, Config.PHOTO_TAKEN_SIZE);

                    if (frameSize.width != 0 && frameSize.height != 0 &&
                            pictureSize.width != 0 && pictureSize.height != 0) {

                        //set the params to optimal size, and photo quality
                        params.setPreviewSize(frameSize.width, frameSize.height);
                        params.setPictureSize(pictureSize.width, pictureSize.height);
                        params.setJpegQuality(100);

                        Log.d("Camera", "Set frame size to " + frameSize.width + "x" + frameSize.height);
                        Log.d("Camera", "Set picture size to " + pictureSize.width + "x" + pictureSize.height);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            params.setRecordingHint(true);
                        }

                        //focus mode
                        List<String> FocusModes = params.getSupportedFocusModes();
                        if (FocusModes != null && FocusModes.contains(FOCUS_MODE)) {
                            params.setFocusMode(FOCUS_MODE);
                        }
                        mCamera.setParameters(params);

                        //rotate display
                        int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
                        mCamera.setDisplayOrientation(ORIENTATIONS.get(rotation));

                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * init and start the camera
     */
    private boolean initializeCamera() {
        Log.d("Camera", "Initialize camera");
        try {
            //set display holder
            mCamera.setPreviewDisplay(mHolder);

            //start preview
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * stop and release camera
     */
    private void releaseCamera() {
        synchronized(this){
            if(mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
                Log.d("Camera", "release camera");
            }

            mCamera = null;
            Log.d("Camera", "set camera null");
        }
    }

    /**
     * auto focus
     */
    private void setAutoFocus() {
        //check if auto focus support
        PackageManager pm = getContext().getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {

            try {
                mCamera.autoFocus(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * scale holder with respect to camera frame size
     */
    private void scaleHolderSize() {
        float frameRatio = (float) frameSize.width / frameSize.height;

        //portrait mode: keep the width of the holder
        if (frameSize.height < mSurfaceHeight)
            mSurfaceWidth = (int) (mSurfaceHeight * frameRatio);
    }

    /**
     * find best frame size from the list of available sizes for display based on a target size
     * @param supportedSizes list of supported size of camera hardware
     * @param accessor accessor object
     * @param targetWidth target width
     * @param targetHeight target height
     * @return camera size
     */
    private Camera.Size calculateCameraFrameSize(List<?> supportedSizes, ListItemAccessor accessor,
                                                 int targetWidth, int targetHeight) {
        int calcWidth = 0;
        int calcHeight = 0;

        for (Object size : supportedSizes) {
            int width = accessor.getWidth(size);
            int height = accessor.getHeight(size);

            //find the maxi value below target sizes
            if (width <= targetWidth && height <= targetHeight) {
                if (width >= calcWidth && height >= calcHeight) {
                    calcWidth = width;
                    calcHeight = height;
                }
            }
        }
        return mCamera.new Size(calcWidth, calcHeight);
    }

    /**
     * Image capture callback
     */
    public interface ImageCapturedCallback {
        void OnImageCaptured(Image image, String imagePath);
    }

    private interface ListItemAccessor {
        int getWidth(Object obj);

        int getHeight(Object obj);
    }

    /**
     * find optimal size
     */
    private static class CameraSizeAccessor implements ListItemAccessor {

        public int getWidth(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.width;
        }

        public int getHeight(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.height;
        }
    }

    /**
     * Image process in a worker thread
     */
    private class ImageRunnable implements Runnable {
        private Thread thread;
        private byte[] _bytes;
        private Context context;

        public ImageRunnable(Context context, byte[] bytes) {
            _bytes = bytes;
            this.context = context;
        }

        @Override
        public void run() {
            if (imageCapturedCallback != null) {
                //save to image, rotate the image as the image taken is in landscape mode
                int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();

                final Image image = new Image(_bytes, Config.CAMERA_IMAGE_QUALITY, ORIENTATIONS.get(rotation));

                //save to local path
                final String path = ImageHelper.saveImageByte(context, image.getByteArray());

                //run method that to be implemented in main UI thread
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        imageCapturedCallback.OnImageCaptured(image, path);
                    }
                });
            }
        }

        public void start() {
            if (thread == null) {
                thread = new Thread(this, "byte array process in worker thread");
                thread.start();
            }
        }

        public void stop() throws InterruptedException {
            if (thread != null && thread.isAlive()) {
                thread.join();
            }
        }
    }
}