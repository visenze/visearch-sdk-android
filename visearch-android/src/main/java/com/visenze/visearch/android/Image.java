package com.visenze.visearch.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
     * max size for low, medium and high
     */
    public static final int             STANDARD_SIZE = 512;
    public static final int             LARGE_SIZE = 1024;
    private static final int            COMPRESS_QUALITY_CAMERA = 75; //compression quality for raw bytes from camera
    private static final int            COMPRESS_QUALITY_HIGH = 90; //compression quality for image from path

    /**
     * byte array of image
     */
    private byte[]                      byteArray;

    /**
     * max size selected
     */
    private int                         maxSize;

    /**
     * scaling factors
     */
    private float                       scaleFactor; //with respect to original size

    /**
     * box
     */
    private Box                         box;

    /**
     * Construct with file path.
     * Image is loaded from the provided file path and re-sized to the optimized size for upload.
     * The default size is 512
     *
     * @param filePath path to a local directory.
     */
    public Image(String filePath) {
        this(filePath, STANDARD_SIZE);
    }

    /**
     * Construct with file path.
     * Image is loaded from the provided file path and re-sized to the optimized size for upload.
     * The default size is 512, it is allowed to be customized to a larger size of 1024
     * For images with finer patterns, it is recommended to use a large size.Noted that to required
     * a large size image for upload search takes higher network bandwidth and longer response time.
     *
     * @param maxSize the max length of the image size
     * @param filePath path to a local directory.
     */
    public Image(String filePath, int maxSize)  {
        //set max size
        if (maxSize != LARGE_SIZE && maxSize != STANDARD_SIZE)
            throw new ViSearchException("Invalid size setting, please use either STANDARD_SIZE or LARGE_SIZE");

        this.maxSize = maxSize;

        //set inJustDecodeBounds true to only get image information, not image bytes
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bitmapOptions);

        //find the optimal inSampleSize, with memory constrain
        calculateScaleFactor(bitmapOptions);

        //use the scale factor to decode the image from file path
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inDensity = 10000;
        bitmapOptions.inTargetDensity = (int) ((float) bitmapOptions.inDensity * scaleFactor);

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bitmapOptions);

        //get image byte array
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY_HIGH, outs);
        byteArray = outs.toByteArray();

        bitmap.recycle();
    }

    public Image(Context context, Uri uri) {
        this(context, uri, STANDARD_SIZE);
    }

    /**
     * Construct with Uri
     * Image is loaded from the provided Uri and re-sized to the optimized size for upload.
     * The default size is 512, it is allowed to be customized to a larger size of 1024
     * For images with finer patterns, it is recommended to use a large size.Noted that to required
     * a large size image for upload search takes higher network bandwidth and longer response time.
     *
     * @param context  Activity context
     * @param uri      Uri to link to the image
     */
    public Image(Context context, Uri uri, int maxSize) {
        //set max size
        if (maxSize != LARGE_SIZE && maxSize != STANDARD_SIZE)
            throw new ViSearchException("Invalid size setting, please use either STANDARD_SIZE or LARGE_SIZE");

        this.maxSize = maxSize;

        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);

            //find the optimal inSampleSize, with memory constrain
            calculateScaleFactor(bitmapOptions);

            //use the scale factor to decode the image from file path
            bitmapOptions.inJustDecodeBounds = false;
            bitmapOptions.inDensity = 10000;
            bitmapOptions.inTargetDensity = (int) ((float) bitmapOptions.inDensity * scaleFactor);

            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);

            Log.d(IMAGE_TAG, "scale bitmap to fit the size: " + bitmap.getWidth() + " x " + bitmap.getHeight());

            //get image byte array
            ByteArrayOutputStream outs = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY_HIGH, outs);
            byteArray = outs.toByteArray();

            bitmap.recycle();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct with raw byte array from the camera Callback
     * This constructor compresses the image with a lower quality value. Try not to
     * call this constructor when handling images what has been compressed. Only use
     * this by passing in the byte array from camera callback
     *
     * @param byteArray image byte array
     */
    public Image(byte[] byteArray) {
        this(byteArray, STANDARD_SIZE);
    }

    /**
     * Construct with raw byte array from the camera Callback
     * This constructor compresses the image with a lower quality value. Try not to
     * call this constructor when handling images what has been compressed. Only use
     * this to passed in the byte array from camera callback.
     * The default re-size limit is 512, it is allowed to be customized to a larger size of 1024
     * For images with finer patterns, it is recommended to use a large size.Noted that to required
     * a large size image for upload search takes higher network bandwidth and longer response time.
     *
     * @param byteArray byte array
     */
    public Image(byte[] byteArray, int maxSize) {
        //set max size
        if (maxSize != LARGE_SIZE && maxSize != STANDARD_SIZE)
            throw new ViSearchException("Invalid size setting, please use either STANDARD_SIZE or LARGE_SIZE");

        this.maxSize = maxSize;

        //get image info from byte array
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapOptions);

         //find the optimal inSampleSize, with memory constrain
        calculateScaleFactor(bitmapOptions);

        //use the scale factor to decode the image from file path
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inDensity = 10000;
        bitmapOptions.inTargetDensity = (int) ((float) bitmapOptions.inDensity * scaleFactor);

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, bitmapOptions);

        //scale the image to fit the required size

        //get image byte array, use lower quality for byte array (COMPRESS_QUALITY_CAMERA)
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY_CAMERA, outs);
        byteArray = outs.toByteArray();

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
     * Get the {@link com.visenze.visearch.android.Image.Box Box}
     *
     * @return Region in the image for search
     */
    public Box getBox() {
        return box;
    }

    /**
     * Down sample the bitmap if its size exceeds the limit
     */
    private void calculateScaleFactor(BitmapFactory.Options options) {
        int originalHeight = options.outHeight;
        int originalWidth = options.outWidth;

        Log.d(IMAGE_TAG, "original image size: " + originalWidth + " x " + originalHeight);

        scaleFactor = ( originalWidth > originalHeight) ? maxSize / (float)originalWidth : maxSize / (float)originalHeight;

        //do not upscale
        if (scaleFactor > 1f)
            scaleFactor = 1f;

        Log.d(IMAGE_TAG, "scale factor: " + scaleFactor);
    }

    /**
     * Region selected on the image for search
     */
    public static class Box {
        private Integer x1;

        private Integer x2;

        private Integer y1;

        private Integer y2;

        /**
         * Construct with coordinates
         *
         * @param x1 top-left corner x-coordinate.
         * @param y1 top-left corner y-coordinate.
         * @param x2 bottom-right corner x-coordinate.
         * @param y2 bottom-right corner y-coordinate.
         */
        public Box(Integer x1, Integer y1, Integer x2, Integer y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        /**
         * Set x1: top-left corner x-coordinate.
         *
         * @param x1 top-left corner x-coordinate.
         * @return this instance.
         */
        public Box setX1(Integer x1) {
            this.x1 = x1;
            return this;
        }

        /**
         * Set y1: top-left corner y-coordinate.
         *
         * @param y1 top-left corner y-coordinate.
         * @return this instance.
         */
        public Box setY1(Integer y1) {
            this.y1 = y1;
            return this;
        }

        /**
         * Set x2: bottom-right corner x-coordinate.
         *
         * @param x2 bottom-right corner x-coordinate.
         * @return this instance.
         */
        public Box setX2(Integer x2) {
            this.x2 = x2;
            return this;
        }

        /**
         * Set y2: bottom-right corner y-coordinate.
         *
         * @param y2 bottom-right corner y-coordinate.
         * @return this instance.
         */
        public Box setY2(Integer y2) {
            this.y2 = y2;
            return this;
        }

        /**
         * Get x1: top-left corner x-coordinate.
         *
         * @return x1 top-left corner x-coordinate.
         */
        public Integer getX1() {
            return x1;
        }

        /**
         * Get y1: top-left corner y-coordinate.
         *
         * @return y1 top-left corner y-coordinate.
         */
        public Integer getY1() {
            return y1;
        }

        /**
         * Get x2: bottom-right corner x-coordinate.
         *
         * @return x2 bottom-right corner x-coordinate.
         */
        public Integer getX2() {
            return x2;
        }

        /**
         * Get y2: bottom-right corner y-coordinate.
         *
         * @return y2 bottom-right corner y-coordinate.
         */
        public Integer getY2() {
            return y2;
        }
    }
}
