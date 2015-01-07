package com.visenze.visearch.android;

import com.visenze.visearch.android.http.ResponseListener;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricGradleTestRunner.class)
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
        ResponseListener responseListener = new ResponseListener(resultListener);

        responseListener.onResponse(new JSONObject(errorSearchResponse));

        Mockito.verify(resultListener, Mockito.times(1)).onSearchError(Mockito.anyString());
        Mockito.verify(resultListener, Mockito.never()).onSearchResult(Mockito.<ResultList>any());
        Mockito.verify(resultListener, Mockito.never()).onSearchCanceled();
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
                "    \"result\":[\n" +
                "        {\n" +
                "            \"im_name\":\"RMK1647SLG\"\n" +
                "        }" +
                "    ]\n" +
                "}";

        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);
        ArgumentCaptor<ResultList> argument = ArgumentCaptor.forClass(ResultList.class);
        ResponseListener responseListener = new ResponseListener(resultListener);

        responseListener.onResponse(new JSONObject(searchResponse));

        Mockito.verify(resultListener, Mockito.never()).onSearchError(Mockito.anyString());
        Mockito.verify(resultListener, Mockito.never()).onSearchCanceled();

        Mockito.verify(resultListener, Mockito.times(1)).onSearchResult(argument.capture());
        assertEquals(1, argument.getValue().getImageList().size());
        assertEquals(248, argument.getValue().getTotal());
        assertEquals(1, argument.getValue().getPageLimit());
        assertEquals(1, argument.getValue().getPage());
    }

}