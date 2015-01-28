package com.visenze.visearch.camerademo;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

import static android.view.SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS;

/**
 * Modified from goodrich camera
 * Created by yulu on 1/28/15.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback, Camera.AutoFocusCallback {

    private static final int CAMERA_FACING_FRONT = 1;
    private static final int CAMERA_FACING_BACK = 0;

    private static final int PHOTO_TAKEN_SIZE = 1024;

    private static final String FOCUS_MODE = Camera.Parameters.FOCUS_MODE_MACRO;

    private static boolean      LightOn = false;

    //surface holder size (need to be matched with camera preview size
    private int                 mSurfaceWidth;
    private int                 mSurfaceHeight;

    //camera preview size, depend on system and hardware
    private Camera.Size         frameSize;
    private Camera.Size         pictureSize;

    private SurfaceHolder       mHolder;

    private Camera              mCamera;

    private ImageCapturedCallback imageCapturedCallback;

    private boolean             takePhotoFlag = false;

    //zoom
    private int                 currentZoomLevel = 1;

    /**
     * Image capture callback
     */
    public static interface ImageCapturedCallback {
        public void OnImageCaptured(byte[] bytes);
    }

    /**
     * Constructor
     * @param context activity context
     */
    public CameraPreview(Context context) {
        super(context);
        initView();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CameraPreview(Context context, AttributeSet attrs, int params) {
        super(context, attrs, params);
        initView();
    }

    /**
     * init the surface holder, done when constructing
     */
    private void initView() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SURFACE_TYPE_PUSH_BUFFERS);
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

    public void stopPreview() {
        if (mCamera != null)
            mCamera.stopPreview();
    }

    /**
     * Start preview with back camera and the previous setting of flash light
     */
    public void startCameraPreview() {
        initializeCamera(CAMERA_FACING_BACK);
    }

    /**
     * Turn on or off the flash light
     * @return flash light turn ON/OFF
     */
    public boolean turnOnTorch() {

        if (LightOn) {
            LightOn = false;
            //set flash off
            Log.d("Camera", "flash torch off");
            Camera.Parameters parameters = mCamera.getParameters();
            List<String> FlashModes = parameters.getSupportedFlashModes();
            if(FlashModes != null && FlashModes.contains(Camera.Parameters.FLASH_MODE_OFF))
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

            mCamera.setParameters(parameters);
            mCamera.startPreview();

        } else {
            LightOn = true;

            //set flash on
            Log.d("Camera", "flash torch on");
            Camera.Parameters parameters = mCamera.getParameters();
            List<String> FlashModes = parameters.getSupportedFlashModes();
            if(FlashModes != null && FlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH))
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

            mCamera.setParameters(parameters);
            mCamera.startPreview();

        }
        return LightOn;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //do nothing
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        //surface size is always in landscape mode, need to inter-change w and h
        //check aspect ratio
        mSurfaceWidth = h;
        mSurfaceHeight = w;

        Log.d("Camera", "holder resize: " + mSurfaceWidth +", " + mSurfaceHeight);
        initializeCamera(CAMERA_FACING_BACK);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (frameSize != null) {
            //keep aspect ratio and scale holder size
            scaleHolderSize();
            setMeasuredDimension(mSurfaceHeight, mSurfaceWidth);
            Log.d("Camera", "holder resize with respect to camera: " + mSurfaceHeight + ", " + mSurfaceWidth);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        releaseCamera();

        if (imageCapturedCallback != null) {
            imageCapturedCallback.OnImageCaptured(bytes);
        }
    }

    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        Log.d("Camera", "auto focus");
        if (takePhotoFlag) {
            camera.takePicture(null, null, null, this);
            takePhotoFlag = false;
        }
    }

    /*
     * init and start the camera
     */
    private boolean initializeCamera(int facing) {
        Log.d("Camera", "Initialize camera");
        boolean result = true;
        synchronized(this){
            if(mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);

                mCamera.release();
                mCamera = null;
            }
            /*
             * Open Camera--------------------------
             */
            Log.i("Camera", "Trying to open camera");
            int localCameraIndex = -1;

            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int localCameraFacingIdx;
            if(facing == CAMERA_FACING_FRONT)
                localCameraFacingIdx = Camera.CameraInfo.CAMERA_FACING_FRONT;
            else
                localCameraFacingIdx = Camera.CameraInfo.CAMERA_FACING_BACK;

            for(int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                Camera.getCameraInfo(camIdx, cameraInfo);

                if(cameraInfo.facing == localCameraFacingIdx){
                    localCameraIndex = camIdx;
                    break;
                }
            }
            if(localCameraIndex != -1) {
                try{
                    mCamera = Camera.open(localCameraIndex);
                    Log.d("Camera", "Camera #" + localCameraIndex + " open this camera");
                }catch(RuntimeException e){
                    Log.e("Camera", "Camera #" + localCameraIndex + " failed to open: " +
                            e.getLocalizedMessage());
                }
            }else{
                Log.e("Camera", "Back camera not found!");
            }

            if(mCamera == null)
                return false;

            /*
             * set camera parameters------------------------------
             */
            try{
                //get optimal size
                Camera.Parameters params = mCamera.getParameters();
                Log.d("Camera", "getSupportedPreviewSizes()");
                List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
                List<Camera.Size> picSizes = params.getSupportedPictureSizes();

                if(previewSizes != null && picSizes != null){
                    // Select the size that fits surface considering maximum size allowed
                    frameSize = calculateCameraFrameSize(previewSizes, new CameraSizeAccessor(),
                            mSurfaceWidth, mSurfaceHeight);
                    params.setPreviewFormat(ImageFormat.NV21);
                    Log.d("Camera", "Set preview size to "+ frameSize.width + "x" + frameSize.height);

                    // Select the size of picture taken that optimized for upload
                    pictureSize = calculateCameraFrameSize(picSizes, new CameraSizeAccessor(),
                            PHOTO_TAKEN_SIZE, PHOTO_TAKEN_SIZE);

                    if(frameSize.width != 0 && frameSize.height != 0 &&
                            pictureSize.width != 0 && pictureSize.height != 0){

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
                        if(FocusModes != null && FocusModes.contains(FOCUS_MODE)) {
                            params.setFocusMode(FOCUS_MODE);
                        }

                        mCamera.setParameters(params);

                        //rotate display
                        mCamera.setDisplayOrientation((cameraInfo.orientation + 360) % 360);

                        //set display holder
                        mCamera.setPreviewDisplay(mHolder);

                        //ready to start the preview
                        Log.d("Camera", "startPreview");
                        mCamera.startPreview();

                        //focus
                        //TODO: check hardware support
                        mCamera.autoFocus(this);
                    }
                } else
                    result = false;

            } catch(Exception e) {
                result = false;
                e.printStackTrace();
            }

            return result;
        }
    }

    /*
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

    /*
     * scale holder with respect to camera frame size
     */
    private void scaleHolderSize() {
        float frameRatio = (float)frameSize.width / frameSize.height;

        //keep the width of the holder
        mSurfaceWidth = (int)(mSurfaceHeight * frameRatio);
    }

    /*
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

    private interface ListItemAccessor {
        public int getWidth(Object obj);
        public int getHeight(Object obj);
    }

    protected Camera.Size calculateCameraFrameSize(List<?> supportedSizes, ListItemAccessor accessor, int surfaceWidth, int surfaceHeight) {
        int calcWidth = 0;
        int calcHeight = 0;

        for (Object size : supportedSizes) {
            int width = accessor.getWidth(size);
            int height = accessor.getHeight(size);

            if (width <= surfaceWidth && height <= surfaceHeight) {
                if (width >= calcWidth && height >= calcHeight) {
                    calcWidth = width;
                    calcHeight = height;
                }
            }
        }
        return mCamera.new Size(calcWidth, calcHeight);
    }

}
