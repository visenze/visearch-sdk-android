package com.visenze.visearch.android.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Handles image decoding and optimisation.
 */
public class Image {
    private static final String         IMAGE_TAG = "image process";

    /**
     * byte array of image
     */
    private byte[]                      byteArray;

    /**
     * scaling factors
     */
    private float                       scaleFactor; //with respect to original size

    /**
     * box
     */
    private Box                         box;

    /**
     * Construct with file path,
     * Image is loaded from the provided file path and re-sized to the optimized size for upload
     *
     * @param filePath path to a local directory
     */
    public Image(String filePath) {
        this(filePath, ResizeSettings.STANDARD);
    }

    /**
     * Construct with file path.
     * Image is loaded from the provided file path and re-sized to the optimized size for upload.
     * The default size is 512, it is allowed to be customized. Use ResizeSettings.HIGH to
     * set the resize limit to 1024.
     *
     * For images with finer patterns, it is recommended to use a large size.Noted that to required
     * a large size image for upload search takes higher network bandwidth and longer response time.
     *
     *
     * @param filePath path to a local directory.
     * @param resizeSettings resize setting
     */
    public Image(String filePath, ResizeSettings resizeSettings)  {

        //set inJustDecodeBounds true to only get image information, not image bytes
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bitmapOptions);

        //find the optimal inSampleSize, with memory constrain
        scaleFactor = calculateScaleFactor(bitmapOptions, resizeSettings);

        //use the scale factor to decode the image from file path
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inDensity = 10000;
        bitmapOptions.inTargetDensity = (int) ((float) bitmapOptions.inDensity * scaleFactor);

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bitmapOptions);

        Log.d(IMAGE_TAG, "scale bitmap to fit the size: " + bitmap.getWidth() + " x " + bitmap.getHeight());

        //get image byte array
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, resizeSettings.getCompressQuality(), outs);
        byteArray = outs.toByteArray();

        //clear bitmap memory
        bitmap.recycle();
    }

    /**
     * Construct with Uri
     * Image is loaded from the provided Uri and re-sized to the optimized size for upload.
     *
     * @param context Activity context
     * @param uri Uri to link to the image
     */
    public Image(Context context, Uri uri) {
        this(context, uri, ResizeSettings.STANDARD);
    }

    /**
     * Construct with Uri
     * Image is loaded from the provided Uri and re-sized to the optimized size for upload.
     * The default size is 512, it is allowed to be customized. Use ResizeSettings.HIGH to
     * set the resize limit to 1024.
     *
     * For images with finer patterns, it is recommended to use a large size.Noted that to required
     * a large size image for upload search takes higher network bandwidth and longer response time.
     *
     * @param context  Activity context
     * @param uri      Uri to link to the image
     * @param resizeSettings resize setting
     */
    public Image(Context context, Uri uri, ResizeSettings resizeSettings) {

        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);

            //find the optimal inSampleSize, with memory constrain
            scaleFactor = calculateScaleFactor(bitmapOptions, resizeSettings);

            //use the scale factor to decode the image from file path
            bitmapOptions.inJustDecodeBounds = false;
            bitmapOptions.inDensity = 10000;
            bitmapOptions.inTargetDensity = (int) ((float) bitmapOptions.inDensity * scaleFactor);

            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);

            Log.d(IMAGE_TAG, "scale bitmap to fit the size: " + bitmap.getWidth() + " x " + bitmap.getHeight());

            //get image byte array
            ByteArrayOutputStream outs = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, resizeSettings.getCompressQuality(), outs);
            byteArray = outs.toByteArray();

            //clear memory
            bitmap.recycle();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct with raw byte array from the camera Callback
     *
     * The image is resize to be processed and transferred efficiently.
     * The default re-size limit is 512
     *
     * @param byteArray byte array from camera callback
     */
    public Image(byte[] byteArray) {
        this(byteArray, ResizeSettings.CAMERA_STANDARD);
    }

    /**
     * Construct with raw byte array from the camera Callback
     *
     * The default size is 512, it is allowed to be customized. Use ResizeSettings.HIGH to
     * set the resize limit to 1024.
     *
     * For images with finer patterns, it is recommended to use a large size.Noted that to required
     * a large size image for upload search takes higher network bandwidth and longer response time.
     *
     * If rotation angle is not indicated, no rotation is performed. The image might not be in the
     * correct orientation
     *
     * @param byteArray byte array from camera callback
     * @param resizeSettings resize setting
     */
    public Image(byte[] byteArray, ResizeSettings resizeSettings) {
        this(byteArray, resizeSettings, 0);
    }

    /**
     * Construct with raw byte array from the camera Callback
     *
     * The default size is 512, it is allowed to be customized. Use ResizeSettings.HIGH to
     * set the resize limit to 1024.
     *
     * For images with finer patterns, it is recommended to use a large size.Noted that to required
     * a large size image for upload search takes higher network bandwidth and longer response time.
     *
     * The captured image will be in landscape for most of the hardware, set rotation value to rotate
     * the image to the correct one
     *
     * @param byteArray byte array from camera callback
     * @param resizeSettings resize setting
     * @param rotation set rotation angle of the image
     */
    public Image(byte[] byteArray, ResizeSettings resizeSettings, float rotation) {
        //get image info from byte array
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapOptions);

        //find the optimal inSampleSize, with memory constrain
        scaleFactor = calculateScaleFactor(bitmapOptions, resizeSettings);

        //use the scale factor to decode the image from file path
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inDensity = 10000;
        bitmapOptions.inTargetDensity = (int) ((float) bitmapOptions.inDensity * scaleFactor);

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapOptions);
        Log.d(IMAGE_TAG, "scale bitmap to fit the size: " + bitmap.getWidth() + " x " + bitmap.getHeight());

        //rotate the image if the given angle
        if (rotation != 0f) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        //get image byte array, use lower quality for byte array (COMPRESS_QUALITY_CAMERA)
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, resizeSettings.getCompressQuality(), outs);
        this.byteArray = outs.toByteArray();

        bitmap.recycle();
    }

    /**
     * Set the coordinates of a region in the image for search, the coordinates needs to be
     * set with respect to the original size of the image
     *
     * @param x1 top-left corner x-coordinate
     * @param y1 top-left corner y-coordinate
     * @param x2 bottom-right corner x-coordinate
     * @param y2 bottom-right corner y-coordinate
     * @return this instance.
     */
    public Image setBox(Integer x1, Integer y1, Integer x2, Integer y2) {
        float scale = getScaleFactor();

        //need to scale the coordinates down to fit the re-sized image
        box = new Box((int) (x1 * scale), (int) (y1 * scale),  (int) (x2 * scale), (int) (y2 * scale));

        Log.d(IMAGE_TAG, "box size: " + box.getX1() + ", " + box.getY1() + ", " + box.getX2() + ", " + box.getY2());

        return this;
    }

    /**
     * return the image byte array after resize and compression
     * @return image byte array
     */
    public byte[] getByteArray() {
        return byteArray;
    }

    /**
     * get the scale factor
     *
     * @return scale factor
     */
    public float getScaleFactor() {
        return scaleFactor;
    }

    /**
     * Get the {@link Box Box}
     *
     * @return Region in the image for search
     */
    public Box getBox() {
        return box;
    }

    /**
     * Down sample the bitmap if its size exceeds the limit
     */
    private float calculateScaleFactor(BitmapFactory.Options options, ResizeSettings resizeSettings) {
        int originalHeight = options.outHeight;
        int originalWidth = options.outWidth;
        int targetHeight = resizeSettings.getMaxHeight();
        int targetWidth = resizeSettings.getMaxWidth();
        float scale;

        Log.d(IMAGE_TAG, "original image size: " + originalWidth + " x " + originalHeight);

        //get the smaller ratio to fit the resize image to the target space
        scale = ( targetWidth / (float)originalWidth < targetHeight / (float)originalHeight)
                ? targetWidth / (float)originalWidth : targetHeight / (float)originalHeight;

        //do not upscale
        if (scale > 1f)
            scale = 1f;

        Log.d(IMAGE_TAG, "scale factor: " + scale);

        return scale;
    }

    /**
     * resize settings
     */
    public static class ResizeSettings {
        /**
         * max size for low, medium and high
         */
        private static final int            STANDARD_SIZE = 512;
        private static final int            LARGE_SIZE = 1024;

        private static final int            COMPRESS_QUALITY = 98; //compression quality
        private static final int            COMPRESS_QUALITY_LOW = 97; //compression quality for camera callback

        public static final ResizeSettings  STANDARD = new ResizeSettings(
                STANDARD_SIZE,
                STANDARD_SIZE,
                COMPRESS_QUALITY);
        public static final ResizeSettings  HIGH = new ResizeSettings(
                LARGE_SIZE,
                LARGE_SIZE,
                COMPRESS_QUALITY);
        public static final ResizeSettings CAMERA_STANDARD = new ResizeSettings(
                STANDARD_SIZE,
                STANDARD_SIZE,
                COMPRESS_QUALITY_LOW);

        public static final ResizeSettings CAMERA_HIGH = new ResizeSettings(
                LARGE_SIZE,
                LARGE_SIZE,
                COMPRESS_QUALITY_LOW);


        private int maxWidth;
        private int maxHeight;
        private int compressQuality;

        public ResizeSettings(int maxWidth, int maxHeight, int compressQuality) {
            this.maxWidth = maxWidth < LARGE_SIZE ? maxWidth : LARGE_SIZE;
            this.maxHeight = maxHeight < LARGE_SIZE ? maxHeight : LARGE_SIZE;

            if (compressQuality < 0)
                compressQuality = 0;

            if (compressQuality > 100)
                compressQuality = 100;

            this.compressQuality = compressQuality;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public int getMaxHeight() {
            return maxHeight;
        }

        public int getCompressQuality() {
            return compressQuality;
        }
    }
}
