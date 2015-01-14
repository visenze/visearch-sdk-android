package com.visenze.visearch.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Handles image decoding and optimisation.
 */
public class Image {
    private static final int OPT_WIDTH = 800;
    private static final int OPT_HEIGHT = 800;

    private Bitmap bitmap;

    private BitmapFactory.Options bitmapOptions;

    private int inSampleSize = 1;

    private Box box;

    /**
     * Construct with file path
     *
     * @param filePath path to a local directory.
     */
    public Image(String filePath) {
        this(filePath, true);
    }

    /**
     * Construct with file path and set optimisation option.
     * If the optimisation is set as false, the image is load as its original size
     * otherwise the image is resize to optimise the search process.
     *
     * @param filePath path to a local directory.
     * @param optimize optimisation option.
     */
    public Image(String filePath, boolean optimize) {
        bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bitmapOptions);

        if (optimize) {
            inSampleSize = calculateInSampleSize(bitmapOptions);
        }

        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = inSampleSize;

        bitmapOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filePath, bitmapOptions);
    }

    /**
     * Construct with Uri
     *
     * @param context Activity context
     * @param uri     Uri to link to the image
     */
    public Image(Context context, Uri uri) {
        this(context, uri, true);
    }

    /**
     * Construct with Uri and set optimisation option.
     * If the optimisation is set as false, the image is load as its original size
     * otherwise the image is resize to optimise the search process.
     *
     * @param context  Activity context
     * @param uri      Uri to link to the image
     * @param optimize optimisation option.
     */
    public Image(Context context, Uri uri, boolean optimize) {
        bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);

            if (optimize) {
                inSampleSize = calculateInSampleSize(bitmapOptions);
            }

            bitmapOptions.inJustDecodeBounds = false;
            bitmapOptions.inSampleSize = inSampleSize;

            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the coordinates of a region in the image for search
     *
     * @param x1 top-left corner x-coordinate
     * @param y1 top-left corner y-coordinate
     * @param x2 bottom-right corner x-coordinate
     * @param y2 bottom-right corner y-coordinate
     * @return this instance.
     */
    public Image setBox(Integer x1, Integer y1, Integer x2, Integer y2) {
        box = new Box(x1, y1, x2, y2, inSampleSize);
        return this;
    }

    public byte[] getByteArray() {
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outs);
        return outs.toByteArray();
    }

    /**
     * Get the Bitmap of the image decoded for search
     *
     * @return Bitmap decoded
     */
    public Bitmap getBitmap() {
        return bitmap;
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
     * Call recycle() to free memory taken by the bitmap
     */
    public void recycle() {
        if (bitmap != null)
            bitmap.recycle();
    }

    /*
     * Down sample the bitmap if its size exceeds the limit
     */
    private int calculateInSampleSize(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        while ((height / inSampleSize) > OPT_HEIGHT &&
                (width / inSampleSize) > OPT_WIDTH) {
            inSampleSize *= 2;
        }

        return inSampleSize;
    }

    /**
     * Region selected on the image for search
     */
    public static class Box {
        private Integer x1;

        private Integer x2;

        private Integer y1;

        private Integer y2;

        private int inSampleSize;

        public Box(int inSampleSize) {
            this.inSampleSize = inSampleSize;
        }

        /**
         * Construct with coordinates
         *
         * @param x1 top-left corner x-coordinate.
         * @param y1 top-left corner y-coordinate.
         * @param x2 bottom-right corner x-coordinate.
         * @param y2 bottom-right corner y-coordinate.
         */
        public Box(Integer x1, Integer y1, Integer x2, Integer y2, int inSampleSize) {
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
            this.x1 = (int) (x1 / (float) inSampleSize);
            return this;
        }

        /**
         * Set y1: top-left corner y-coordinate.
         *
         * @param y1 top-left corner y-coordinate.
         * @return this instance.
         */
        public Box setY1(Integer y1) {
            this.y1 = (int) (y1 / (float) inSampleSize);
            return this;
        }

        /**
         * Set x2: bottom-right corner x-coordinate.
         *
         * @param x2 bottom-right corner x-coordinate.
         * @return this instance.
         */
        public Box setX2(Integer x2) {
            this.x2 = (int) (x2 / (float) inSampleSize);
            return this;
        }

        /**
         * Set y2: bottom-right corner y-coordinate.
         *
         * @param y2 bottom-right corner y-coordinate.
         * @return this instance.
         */
        public Box setY2(Integer y2) {
            this.y2 = (int) (y2 / (float) inSampleSize);
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
