package com.visenze.visearch.android.model;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.os.Build;
import com.visenze.visearch.android.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ProductSearchApiTest {

    @Test
    public void testIsMultisearchRelated() {
        assertTrue(ProductSearchApi.MS.isMultisearchRelated());
        assertTrue(ProductSearchApi.MS_COMPLEMENTARY.isMultisearchRelated());
        assertTrue(ProductSearchApi.MS_OUTFIT_REC.isMultisearchRelated());

        assertFalse(ProductSearchApi.SBI.isMultisearchRelated());
    }
}