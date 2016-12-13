package com.visenze.visearch.android;

import android.os.Build;

import com.visenze.visearch.android.api.impl.TrackOperationsImpl;
import com.visenze.visearch.android.http.ResponseListener;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Created by visenze on 30/11/15.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class ViSearchTest {
    @Test
    public void testErrorResponse() throws Exception {
        String errorSearchResponse = "{\n" +
                "    \"status\":\"fail\",\n" +
                "    \"method\":\"search\",\n" +
                "    \"error\":[\n" +
                "        \"Image not found with im_name.\"\n" +
                "    ],\n" +
                "    \"page\":0,\n" +
                "    \"limit\":0,\n" +
                "    \"total\":0,\n" +
                "    \"result\":[\n" +
                "        \n" +
                "    ]\n" +
                "}";

        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);
        TrackOperationsImpl trackOperations = Mockito.mock(TrackOperationsImpl.class);
        ResponseListener responseListener = new ResponseListener(resultListener, trackOperations, "search");

        responseListener.onResponse(new JSONObject(errorSearchResponse));

        Mockito.verify(resultListener, times(1)).onSearchError(Mockito.anyString());
        Mockito.verify(resultListener, never()).onSearchResult(Mockito.<ResultList>any());
        Mockito.verify(resultListener, never()).onSearchCanceled();
    }

    @Test
    public void testResultResponse() throws Exception {
        String searchResponse = "{\n" +
                "    \"status\":\"OK\",\n" +
                "    \"method\":\"uploadsearch\",\n" +
                "    \"error\":[\n" +
                "        \n" +
                "    ],\n" +
                "    \"page\":1,\n" +
                "    \"limit\":1,\n" +
                "    \"total\":248,\n" +
                "    \"im_id\":test_im_id,\n" +
                "    \"result\":[\n" +
                "        {\n" +
                "            \"im_name\":\"RMK1647SLG\"\n" +
                "        }" +
                "    ]\n" +
                "}";

        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);
        TrackOperationsImpl trackOperations = Mockito.mock(TrackOperationsImpl.class);
        ArgumentCaptor<ResultList> argument = ArgumentCaptor.forClass(ResultList.class);
        ResponseListener responseListener = new ResponseListener(resultListener, trackOperations, "uploadsearch");

        responseListener.onResponse(new JSONObject(searchResponse));

        Mockito.verify(resultListener, never()).onSearchError(Mockito.anyString());
        Mockito.verify(resultListener, never()).onSearchCanceled();

        Mockito.verify(resultListener, times(1)).onSearchResult(argument.capture());
        assertEquals(1, argument.getValue().getImageList().size());
        assertEquals(248, argument.getValue().getTotal());
        assertEquals(1, argument.getValue().getPageLimit());
        assertEquals(1, argument.getValue().getPage());
        assertEquals("test_im_id", argument.getValue().getImId());
    }

    @Test
    public void testResultResponseFail() throws Exception {
        String searchResponse = "{\n" +
                "    \"status\":\"Fail\",\n" +
                "    \"method\":\"uploadsearch\",\n" +
                "    \"error\":[\n" +
                "        \"Some thing is wrong\"\n" +
                "    ],\n" +
                "    \"page\":1,\n" +
                "    \"limit\":1,\n" +
                "    \"total\":0,\n" +
                "    \"im_id\":test_im_id,\n" +
                "    \"result\":[\n" +
                "    ]\n" +
                "}";

        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);
        TrackOperationsImpl trackOperations = Mockito.mock(TrackOperationsImpl.class);
        ArgumentCaptor<ResultList> argument = ArgumentCaptor.forClass(ResultList.class);
        ResponseListener responseListener = new ResponseListener(resultListener, trackOperations, "uploadsearch");

        responseListener.onResponse(new JSONObject(searchResponse));

        Mockito.verify(resultListener, times(1)).onSearchError(Mockito.anyString());
        Mockito.verify(resultListener, never()).onSearchCanceled();
        Mockito.verify(resultListener, never()).onSearchResult(argument.capture());
    }

    @Test
    public void testResultResponseWithDetection() throws Exception {
        String searchWithDetectionResponse = "{\n" +
                "   \"status\":\"OK\",\n" +
                "   \"method\":\"uploadsearch\",\n" +
                "   \"error\":[\n" +
                "   ],\n" +
                "   \"page\":1,\n" +
                "   \"limit\":2,\n" +
                "   \"total\":1000,\n" +
                "   \"product_types\":[\n" +
                "   {\n" +
                "       \"type\":\"top\",\n" +
                "       \"box\":[5,20,100,100],\n" +
                "       \"score\":0.8\n" +
                "   },\n" +
                "   {\n" +
                "       \"type\":\"bag\",\n" +
                "       \"box\":[0,0,100,100],\n" +
                "       \"score\":0.6\n" +
                "   },\n" +
                "   {\n" +
                "       \"type\":\"dress\",\n" +
                "       \"box\":[0,0,100,100],\n" +
                "       \"score\":0.6\n" +
                "   },\n" +
                "   {\n" +
                "       \"type\":\"shoe\",\n" +
                "       \"box\":[0,0,100,100],\n" +
                "       \"score\":0.6\n" +
                "   }\n" +
                "   ],\n" +
                "   \"result\":[\n" +
                "   {\n" +
                "       \"im_name\":\"image-name-1\",\n" +
                "       “score” : 0.5\n" +
                "   },\n" +
                "   {\n" +
                "       \"im_name\":\"image-name-2\",\n" +
                "       “score” : 0.4\n" +
                "   }\n" +
                "]\n" +
                "}";

        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);
        TrackOperationsImpl trackOperations = Mockito.mock(TrackOperationsImpl.class);
        ArgumentCaptor<ResultList> argument = ArgumentCaptor.forClass(ResultList.class);
        ResponseListener responseListener = new ResponseListener(resultListener, trackOperations, "uploadsearch");

        responseListener.onResponse(new JSONObject(searchWithDetectionResponse));

        Mockito.verify(resultListener, never()).onSearchError(Mockito.anyString());
        Mockito.verify(resultListener, never()).onSearchCanceled();

        Mockito.verify(resultListener, times(1)).onSearchResult(argument.capture());
        assertEquals(2, argument.getValue().getImageList().size());
        assertEquals(1000, argument.getValue().getTotal());
        assertEquals(2, argument.getValue().getPageLimit());
        assertEquals(1, argument.getValue().getPage());

        //detection response
        assertEquals(4, argument.getValue().getProductTypes().size());
        assertEquals("top", argument.getValue().getProductTypes().get(0).getType());
        assertEquals("bag", argument.getValue().getProductTypes().get(1).getType());
        assertEquals("dress", argument.getValue().getProductTypes().get(2).getType());
        assertEquals("shoe", argument.getValue().getProductTypes().get(3).getType());
        assertEquals(5, (int)argument.getValue().getProductTypes().get(0).getBox().getX1());
        assertEquals(20, (int)argument.getValue().getProductTypes().get(0).getBox().getY1());
        assertEquals(100, (int)argument.getValue().getProductTypes().get(0).getBox().getX2());
        assertEquals(100, (int)argument.getValue().getProductTypes().get(0).getBox().getY2());
        assertEquals(0.8f, argument.getValue().getProductTypes().get(0).getScore(), 0.000001f);
    }
}