package com.visenze.visearch.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Image resize test
 * Test if image is correct resized
 * Test if box selection is corrected resized
 * Created by yulu on 1/15/15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricGradleTestRunner.class)

public class ImageTest {
    //path to test images
    private final String PATH_TEST_IMAGE_LARGE = "assets/test_image_large.png";
    private final String PATH_TEST_IMAGE_SMALL = "assets/test_image_small.png";

    /**
     * test resize byte array
     *
     * @throws Exception
     */
    @Test
    public void SmallImageResizeTest() throws Exception {
        byte[] array = getByteArrayFromResourcePath(PATH_TEST_IMAGE_SMALL);

        Image image = new Image(array);

        float scaleFactor = (float) Image.STANDARD_SIZE / getImageLongestLength(PATH_TEST_IMAGE_SMALL);
        scaleFactor = scaleFactor > 1.0f ? 1.0f : scaleFactor;

        assertEquals(scaleFactor, image.getScaleFactor(), 0.01);
    }

    /**
     * test resize byte array
     *
     * @throws Exception
     */
    @Test
    public void LargeImageResizeTest() throws Exception {
        byte[] array = getByteArrayFromResourcePath(PATH_TEST_IMAGE_LARGE);

        Image image = new Image(array);

        float scaleFactor = (float) Image.STANDARD_SIZE / getImageLongestLength(PATH_TEST_IMAGE_LARGE);
        scaleFactor = scaleFactor > 1.0f ? 1.0f : scaleFactor;

        assertEquals(scaleFactor, image.getScaleFactor(), 0.01);
    }

    /**
     * test resize byte array for large size
     *
     * @throws Exception
     */
    @Test
    public void LargeImageResizeTestLarge() throws Exception {

        byte[] array = getByteArrayFromResourcePath(PATH_TEST_IMAGE_LARGE);

        Image image = new Image(array, Image.LARGE_SIZE);

        float scaleFactor = (float) Image.LARGE_SIZE / getImageLongestLength(PATH_TEST_IMAGE_LARGE);
        scaleFactor = scaleFactor > 1.0f ? 1.0f : scaleFactor;

        assertEquals(scaleFactor, image.getScaleFactor(), 0.01);
    }

    /**
     * test box parameters
     *
     * @throws Exception
     */
    @Test
    public void LargeImageBoxTest() throws Exception {
        byte[] array = getByteArrayFromResourcePath(PATH_TEST_IMAGE_LARGE);

        int x1 = 50;
        int y1 = 50;
        int x2 = 300;
        int y2 = 300;

        Image image = new Image(array).setBox(x1, y1, x2, y2);

        float scaleFactor = (float) Image.STANDARD_SIZE / getImageLongestLength(PATH_TEST_IMAGE_LARGE);
        scaleFactor = scaleFactor > 1.0f ? 1.0f : scaleFactor;

        assertEquals((int)(x1 * scaleFactor), (int)image.getBox().getX1());
        assertEquals((int)(y1 * scaleFactor), (int)image.getBox().getY1());
        assertEquals((int)(x2 * scaleFactor), (int)image.getBox().getX2());
        assertEquals((int)(y2 * scaleFactor), (int)image.getBox().getY2());
    }

    private byte[] getByteArrayFromResourcePath(String path) {
        //TODO: not read byte array correctly
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);

        Bitmap bitmap = BitmapFactory.decodeStream(in);

        //get image byte array
        ByteArrayOutputStream outs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outs);
        return outs.toByteArray();
    }

    private int getImageLongestLength(String path) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
        BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap bitmap = BitmapFactory.decodeStream(in);

        return bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
    }
}

