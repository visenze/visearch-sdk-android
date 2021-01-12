package com.visenze.visearch.android;

import android.os.Build;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.visenze.visearch.android.data.ResponseData;
import com.visenze.visearch.android.http.ResponseListener;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.android.model.ObjectResult;
import com.visenze.visearch.android.model.Tag;
import com.visenze.visearch.android.model.TagGroup;
import com.visenze.visearch.android.network.SearchService;
import com.visenze.visearch.android.util.ResponseParser;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

/**
 * Created by visenze on 30/11/15.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)
public class ViSearchTest {

    SearchService searchService = new SearchService("https://visearch.visenze.com", "123", "456", "visearch-test");
    Gson gson = new GsonBuilder()
        .setLenient()
        .create();
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


        ResponseData response = gson.fromJson(errorSearchResponse, ResponseData.class);
        searchService.handleResponse(response, resultListener);


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


        ArgumentCaptor<ResultList> argument = ArgumentCaptor.forClass(ResultList.class);
        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);


        ResponseData response = gson.fromJson(searchResponse, ResponseData.class);
        searchService.handleResponse(response, resultListener);

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


        ArgumentCaptor<ResultList> argument = ArgumentCaptor.forClass(ResultList.class);
        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);


        ResponseData response = gson.fromJson(searchResponse, ResponseData.class);
        searchService.handleResponse(response, resultListener);


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


        ArgumentCaptor<ResultList> argument = ArgumentCaptor.forClass(ResultList.class);
        ViSearch.ResultListener resultListener = Mockito.mock(ViSearch.ResultListener.class);


        ResponseData response = gson.fromJson(searchWithDetectionResponse, ResponseData.class);
        searchService.handleResponse(response, resultListener);

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

        assertNull(argument.getValue().getObjects());
    }

    @Test
    public void testQueryTagsParsing() {
        String s = "{\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"uploadsearch\",\n" +
                "    \"error\": [],\n" +
                "    \"page\": 1,\n" +
                "    \"total\": 4,\n" +
                "    \"product_types\": [\n" +
                "        {\n" +
                "            \"box\": [\n" +
                "                50,\n" +
                "                113,\n" +
                "                233,\n" +
                "                439\n" +
                "            ],\n" +
                "            \"type\": \"product_item\",\n" +
                "            \"score\": 0.4035140872001648,\n" +
                "            \"attributes\": {}\n" +
                "        }\n" +
                "    ],\n" +
                "    \"product_types_list\": [\n" +
                "        {\n" +
                "            \"type\": \"bag\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"belt\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"bottle\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"bottom\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"dress\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"ethnic_wear\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"eyewear\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"furniture\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"gloves\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"headwear\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"jewelry\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"onepiece\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"outerwear\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"package\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"product_item\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"scarf\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"shoe\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"skirt\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"swimwear\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"tie\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"top\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"underwear_bottom\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"underwear_top\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"watch\",\n" +
                "            \"attributes_list\": {}\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\": \"other\",\n" +
                "            \"attributes_list\": {}\n" +
                "        }\n" +
                "    ],\n" +
                "    \"im_id\": \"20200615365321769aac79d380be92e7f3126c082f396dbc0cd.jpg\",\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"im_name\": \"SHOPEE-DF-SG_438289555\",\n" +
                "            \"score\": 0.9,\n" +
                "            \"s3_url\": \"s3.xxx\",\n" +
                "            \"value_map\": {\n" +
                "                \"store_id\": \"338660587\",\n" +
                "                \"updated_time\": \"1589609171\",\n" +
                "                \"original_price\": \"280.0\",\n" +
                "                \"product_url\": \"https://shopee.sg/universal-link/product/31708369/438289555\",\n" +
                "                \"gender\": \"\",\n" +
                "                \"vi_category_ids\": \"\",\n" +
                "                \"pgid\": \"22773b7509925e3286e1c0f78c2bb21e\",\n" +
                "                \"source\": \"SHOPEE-DF-SG\",\n" +
                "                \"title\": \"Tory Burch Robinson Mini Zip Continental Wallet\",\n" +
                "                \"im_url\": \"http://cf.shopee.sg/file/ccd034ae6aace80917c3a167afa6cd0c\",\n" +
                "                \"brand_id\": \"776655171.0\",\n" +
                "                \"price_unit\": \"SGD\",\n" +
                "                \"price\": \"280.0\",\n" +
                "                \"category\": \"Women's Bags > Branded Bags > Wallets\",\n" +
                "                \"brand\": \"josephinetss\",\n" +
                "                \"desc\": \"\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"im_name\": \"AMAZON-UK_B07256CLNK\",\n" +
                "            \"score\": 0.71846182816964,\n" +
                "            \"value_map\": {\n" +
                "                \"store_id\": \"1641533398\",\n" +
                "                \"updated_time\": \"1588944185\",\n" +
                "                \"original_price\": \"60.0\",\n" +
                "                \"product_url\": \"https://www.amazon.co.uk/dp/B07256CLNK\",\n" +
                "                \"gender\": \"\",\n" +
                "                \"vi_category_ids\": \"\",\n" +
                "                \"pgid\": \"09fc703b52855038a117124d6b886f9f\",\n" +
                "                \"source\": \"AMAZON-UK\",\n" +
                "                \"title\": \"Tommy Hilfiger Womens Honey Large Za Wallet Wallet\",\n" +
                "                \"im_url\": \"https://m.media-amazon.com/images/I/91t1vb+YjZL.jpg\",\n" +
                "                \"brand_id\": \"\",\n" +
                "                \"price_unit\": \"GBP\",\n" +
                "                \"price\": \"46.360001\",\n" +
                "                \"category\": \"Luggage|Wallets, Card Cases & Money Organizers|Women's\",\n" +
                "                \"brand\": \"\",\n" +
                "                \"desc\": \"\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"im_name\": \"AMAZON-UK_B07V8YPQYX\",\n" +
                "            \"score\": 0.6628711714411721,\n" +
                "            \"value_map\": {\n" +
                "                \"store_id\": \"1641533398\",\n" +
                "                \"updated_time\": \"1588950491\",\n" +
                "                \"original_price\": \"60.0\",\n" +
                "                \"product_url\": \"https://www.amazon.co.uk/dp/B07V8YPQYX\",\n" +
                "                \"gender\": \"\",\n" +
                "                \"vi_category_ids\": \"\",\n" +
                "                \"pgid\": \"09728d8edb4d5f1781f1ed09b4703754\",\n" +
                "                \"source\": \"AMAZON-UK\",\n" +
                "                \"title\": \"Tommy Hilfiger Women's Honey Lrg Za Wallet\",\n" +
                "                \"im_url\": \"https://m.media-amazon.com/images/I/71mspFfzZOL.jpg\",\n" +
                "                \"brand_id\": \"\",\n" +
                "                \"price_unit\": \"GBP\",\n" +
                "                \"price\": \"47.419998\",\n" +
                "                \"category\": \"Luggage|Wallets, Card Cases & Money Organizers|Women's\",\n" +
                "                \"brand\": \"\",\n" +
                "                \"desc\": \"\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"im_name\": \"AMAZON-UK_B07K1Q6SF3\",\n" +
                "            \"score\": 0.644561556319488,\n" +
                "            \"value_map\": {\n" +
                "                \"store_id\": \"1641533398\",\n" +
                "                \"updated_time\": \"1588917928\",\n" +
                "                \"original_price\": \"53.34\",\n" +
                "                \"product_url\": \"https://www.amazon.co.uk/dp/B07K1Q6SF3\",\n" +
                "                \"gender\": \"\",\n" +
                "                \"vi_category_ids\": \"\",\n" +
                "                \"pgid\": \"c03a2efbb2395c94bc3bda7bb4c8d6c0\",\n" +
                "                \"source\": \"AMAZON-UK\",\n" +
                "                \"title\": \"Tommy Hilfiger Women's Modern Hardware Lrg Za Wallet\",\n" +
                "                \"im_url\": \"https://m.media-amazon.com/images/I/716DjSYs94L.jpg\",\n" +
                "                \"brand_id\": \"\",\n" +
                "                \"price_unit\": \"GBP\",\n" +
                "                \"price\": \"53.34\",\n" +
                "                \"category\": \"Luggage|Wallets, Card Cases & Money Organizers\",\n" +
                "                \"brand\": \"\",\n" +
                "                \"desc\": \"\"\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"query_tags\": [\n" +
                "        {\n" +
                "            \"tag_group\": \"bqf_tagging\",\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"tag\": \"category:product\",\n" +
                "                    \"score\": 1.0\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"tag_group\": \"exact_match\",\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"tag\": \"SHOPEE-DF-SG_438289555\",\n" +
                "                    \"score\": 0.8512772793066337\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"tag_group\": \"category\",\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"tag\": \"Bag|ClutchWallet\",\n" +
                "                    \"score\": 0.8\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"recognition_reqid\": \"1039660405993140224\",\n" +
                "    \"feature_extraction_reqid\": \"1039660407356180187\",\n" +
                "    \"recalling_reqid\": \"1039660407356180187\",\n" +
                "    \"reqid\": \"34462732626671404466192938475832783780\"\n" +
                "}";

        ResponseData response = gson.fromJson(s, ResponseData.class);
        ResultList resultList = response.getResultList();
        assertEquals("34462732626671404466192938475832783780", resultList.getReqid());
        List<TagGroup> tagGroupList = resultList.getQueryTags();
        assertTrue(tagGroupList.size() == 3);

        TagGroup bqf = tagGroupList.get(0);
        assertEquals("bqf_tagging" , bqf.getName());
        assertTrue( 1 == bqf.getTags().size());
        Tag bqfTag = bqf.getTags().get(0);
        assertEquals("category:product", bqfTag.getTag());
        assertTrue(1.0 == bqfTag.getScore());

        TagGroup exact = tagGroupList.get(1);
        assertEquals("exact_match" , exact.getName());
        assertTrue( 1 == exact.getTags().size());
        Tag exactTag = exact.getTags().get(0);
        assertEquals("SHOPEE-DF-SG_438289555", exactTag.getTag());
        assertTrue(0.8512772793066337 == exactTag.getScore());

        TagGroup category = tagGroupList.get(2);
        assertEquals("category" , category.getName());
        assertTrue( 1 == category.getTags().size());
        Tag categoryTag = category.getTags().get(0);
        assertEquals("Bag|ClutchWallet", categoryTag.getTag());
        assertTrue(0.8 == categoryTag.getScore());

        assertEquals("s3.xxx", resultList.getImageList().get(0).getS3Url());
        assertNull(resultList.getImageList().get(1).getS3Url());

        ImageResult image = resultList.getImageList().get(0);
        assertEquals("SHOPEE-DF-SG_438289555", image.getImageName());
        assertEquals("0.9", String.valueOf(image.getScore()) );
        Map<String, String> map = image.getMetaData();
        assertEquals("338660587", map.get("store_id"));
        assertEquals("280.0", map.get("original_price"));
        assertEquals("Tory Burch Robinson Mini Zip Continental Wallet", map.get("title"));

    }

    @Test
    public void testDiscoverSearch() throws Exception {
        String s = "{\n" +
                "    \"status\":\"OK\",\n" +
                "    \"method\":\"discoversearch\",\n" +
                "    \"error\":[\n" +
                "        \n" +
                "    ],\n" +
                "    \"result_limit\":1,\n" +
                "    \"detection_limit\":2,\n" +
                "    \"page\":1,\n" +
                "    \"objects\":[\n" +
                "        {\n" +
                "            \"type\":\"bottom\",\n" +
                "            \"attributes\":{\n" +
                "                \n" +
                "            },\n" +
                "            \"score\":0.8166874647140503,\n" +
                "            \"box\":[\n" +
                "                241,\n" +
                "                381,\n" +
                "                489,\n" +
                "                894\n" +
                "            ],\n" +
                "            \"total\":1000,\n" +
                "            \"result\":[\n" +
                "                {\n" +
                "                    \"im_name\":\"ZALORA-SG_053B7AA66DC684GS\",\n" +
                "                    \"score\":1,\n" +
                "                    \"value_map\":{\n" +
                "                        \"title\":\"Buy Pomelo Cropped High Waist Button Slit Pants Online on ZALORA Singapore\",\n" +
                "                        \"im_url\":\"http://static.sg.zalora.net/p/pomelo-2667-288219-1.jpg\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"top\",\n" +
                "            \"attributes\":{\n" +
                "                \n" +
                "            },\n" +
                "            \"score\":0.5112888216972351,\n" +
                "            \"box\":[\n" +
                "                272,\n" +
                "                174,\n" +
                "                546,\n" +
                "                400\n" +
                "            ],\n" +
                "            \"total\":1000,\n" +
                "            \"result\":[\n" +
                "                {\n" +
                "                    \"im_name\":\"ZALORA-SG_9A39AAA26CE98BGS\",\n" +
                "                    \"score\":0.740113377571106,\n" +
                "                    \"value_map\":{\n" +
                "                        \"title\":\"Buy Pomelo Fighting The Patriarchy Graphic Tee\",\n" +
                "                        \"im_url\":\"http://static.sg.zalora.net/p/pomelo-8263-742219-1.jpg\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"object_types_list\":[\n" +
                "        {\n" +
                "            \"type\":\"bag\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"belt\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"bottom\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"dress\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"eyewear\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"gloves\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"headwear\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"jewelry\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"onepiece\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"outerwear\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"scarf\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"shoe\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"skirt\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"swimwear\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"tie\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"top\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"underwear_bottom\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"underwear_top\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"watch\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"type\":\"other\",\n" +
                "            \"attributes_list\":{\n" +
                "                \n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"im_id\":\"20201011365cfd51d3a6cfc1c1e9f4c5b0ef8201dd9c569b23a.jpg\",\n" +
                "    \"reqid\":\"1082464812702743783\"\n" +
                "}";

        ResponseData response = gson.fromJson(s, ResponseData.class);
//        ResultList resultList = ResponseParser.parseResult(s);
        ResultList resultList = response.getResultList();
        assertNull(resultList.getImageList());
        List<ObjectResult> objects = resultList.getObjects();
        assertEquals( 2, objects.size());

        ObjectResult object1 = objects.get(0);
        ObjectResult object2 = objects.get(1);

        assertEquals("bottom", object1.getType());
        assertEquals("241,381,489,894",
                object1.getBox().getX1() + "," +
                object1.getBox().getY1() + ","
                + object1.getBox().getX2() + "," + object1.getBox().getY2() );

        assertEquals("ZALORA-SG_053B7AA66DC684GS", object1.getResult().get(0).getImageName());


        assertEquals("top", object2.getType());
        assertEquals("272,174,546,400",
                object2.getBox().getX1() + "," +
                        object2.getBox().getY1() + ","
                        + object2.getBox().getX2() + "," + object2.getBox().getY2() );

        assertEquals("ZALORA-SG_9A39AAA26CE98BGS", object2.getResult().get(0).getImageName());
        assertEquals("0.7401134", String.valueOf(object2.getResult().get(0).getScore()));


    }
}