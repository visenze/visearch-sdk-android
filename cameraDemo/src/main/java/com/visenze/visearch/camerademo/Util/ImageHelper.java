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

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.visenze.visearch.android.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yulu on 29/1/15.
 * 
 * Helper class to handle image related task such resize, rotate, save to local storage
 */
public class ImageHelper {

    /**
     * rotate image with degrees
     * @param bitmap bitmap
     * @param degree rotate degree
     * @return rotated bitmap
     */
    public static Bitmap rotateImage(Bitmap bitmap, int degree) {
        //bitmap = scaleBitmap(bitmap);

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return bitmap;
    }

    /**
     * get resized photo (for display) from uri
     *
     * @param uri uri of photo in the gallery
     * @return bitmap from byte array
     */
    public static Bitmap getResizedBitmapFromUri(Uri uri, Context context, Image.ResizeSettings resizeSettings) throws FileNotFoundException {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);

        //find the optimal inSampleSize, with memory constrain
        float scaleFactor = calculateScaleFactor(bitmapOptions, resizeSettings);

        //use the scale factor to decode the image from file path
        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inDensity = 10000;
        bitmapOptions.inTargetDensity = (int) ((float) bitmapOptions.inDensity * scaleFactor);

        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapOptions);

        return bitmap;
    }

    /**
     * save the image byte to a temp local path
     * @param bytes image byte array
     * @return path to save the image
     */
    public static String saveImageByte(Context context, byte[] bytes) {
        //Get the album local path, photo name
        String fullPath = null;
        String directoryPath = Environment.getExternalStorageDirectory().toString()  +
                "/Pictures/" + Config.ALBUM_NAME;
        String imageName = Config.IMAGE_NAME + System.currentTimeMillis() + ".jpg";

        //check if /Pictures exist
        String picDirPath = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        File picDir = new File(picDirPath);
        if (!picDir.exists()) {
            picDir.mkdir();
        }

        //Save image to local path
        try {
            File fileDir = new File(directoryPath);
            if(!fileDir.exists())
                fileDir.mkdir();

            File imageFile = new File(directoryPath, imageName);
            FileOutputStream fos = new FileOutputStream(imageFile);

            fos.write(bytes);

            fos.flush();
            fos.close();

            fullPath = imageFile.getPath();

            new MediaScannerNotifier(context, fullPath, "image/jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullPath;
    }

    /**
     * save the image byte to a temp local path
     * @param bytes image byte array
     * @return path to save the image
     */
    public static String saveImageByteTmp(Context context, byte[] bytes) {
        String imageName = "." + Config.IMAGE_NAME + ".jpg";
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        // create directory
        File imageFile = new File(directory, imageName);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(imageFile);
            fos.write(bytes);
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getPath();
    }

    /**
     * load bitmap from path
     * @param path full image file path
     * @return bitmap
     */
    public static Bitmap getBitmapFromPath(String path) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inScaled = false;
        bitmapOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, bitmapOptions);
    }

    public static int getRotation(Uri uri, Context context) {
        int rotation = 0;

        try {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                    null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    rotation = cursor.getInt(0);
                } else {
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return rotation;
    }

    public static byte[] readBytes(Uri uri, Context context, Image.ResizeSettings resizeSettings) throws IOException {
        // this dynamically extends to take the bytes you read
        Bitmap bitmap = getResizedBitmapFromUri(uri, context, resizeSettings);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int rotation = getRotation(uri, context);
        if (rotation != 0) {
            bitmap = rotateImage(bitmap, rotation);
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    /**
     * Down sample the bitmap if its size exceeds the limit
     */
    private static float calculateScaleFactor(BitmapFactory.Options options, Image.ResizeSettings resizeSettings) {
        int originalHeight = options.outHeight;
        int originalWidth = options.outWidth;
        int targetHeight = resizeSettings.getMaxHeight();
        int targetWidth = resizeSettings.getMaxWidth();
        float scale;

        //get the smaller ratio to fit the resize image to the target space
        scale = ( targetWidth / (float)originalWidth < targetHeight / (float)originalHeight)
                ? targetWidth / (float)originalWidth : targetHeight / (float)originalHeight;

        //do not upscale
        if (scale > 1f)
            scale = 1f;


        return scale;
    }
}
