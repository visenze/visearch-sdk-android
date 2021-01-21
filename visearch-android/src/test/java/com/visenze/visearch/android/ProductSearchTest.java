package com.visenze.visearch.android;


import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.visenze.visearch.android.model.ProductResponse;
import com.visenze.visearch.android.model.ProductResult;
import com.visenze.visearch.android.model.ProductType;
import com.visenze.visearch.android.network.ProductSearchService;
import com.visenze.visearch.android.network.RetrofitQueryMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ProductSearchTest {
    ProductSearchService searchService = new ProductSearchService("https://visearch.visenze.com", "123", "456", "visearch-test");
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    @Test
    public void testBaseSearchParams_parsing() {
        ProductImageSearchParams params = new ProductImageSearchParams();
        params.setFacetLimit(10);
        params.setDebug(true);
        params.setImId("1234555");


        Map<String, String> customMap = new HashMap<String, String>();
        customMap.put("testParam", "testParamVal");
        customMap.put("testParam2", "testParamVal2");
        params.setCustomMap(customMap);

        RetrofitQueryMap map = params.getQueryMap();

        assertEquals("10", map.get("facet_limit"));
        assertEquals("true", map.get("debug"));
        assertEquals("testParamVal", map.get("testParam"));
        assertEquals("testParamVal2", map.get("testParam2"));
        assertEquals("1234555", map.get("im_id"));
    }


    @Test
    public void testErrorResponse() {
        String errorResp = "{\n" +
                "    \"reqid\": \"017722d1909bcda58402b1f1b4c1a6\",\n" +
                "    \"status\": \"fail\",\n" +
                "    \"method\": \"similar-products\",\n" +
                "    \"error\": {\n" +
                "        \"code\": 203,\n" +
                "        \"message\": \"Please provide ''image'', ''im_url'' or ''im_id'' parameter.\"\n" +
                "    }\n" +
                "}";

        ProductResponse response = gson.fromJson(errorResp, ProductResponse.class);
        searchService.handleResponse(response, new ProductSearch.ResultListener() {
            @Override
            public void onSearchResult(ProductResponse response, String errorMsg) {
                assertNull(response);
                assertEquals("Please provide ''image'', ''im_url'' or ''im_id'' parameter.", errorMsg);
            }
        });
    }


    @Test
    public void testSimilarProductResponse() {
        String successResp = "{\n" +
                "    \"im_id\": \"202101213651d741543e7d0145167566a37d3dbf340a4e2c514.jpg\",\n" +
                "    \"reqid\": \"017722d8b9cfcda58402b1f1b4c1e6\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"similar-products\",\n" +
                "    \"product_types\": [\n" +
                "        {\n" +
                "            \"type\": \"top\",\n" +
                "            \"score\": 0.715,\n" +
                "            \"box\": [\n" +
                "                156,\n" +
                "                96,\n" +
                "                1420,\n" +
                "                1889\n" +
                "            ],\n" +
                "            \"attributes\": {}\n" +
                "        }\n" +
                "    ],\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.78059CB19EE49440.12339652SL_2\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/12/12339652SL_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"109.0\"\n" +
                "                },\n" +
                "                \"title\": \"MAJESTIC FILATURES T-shirts\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.7439000440375D81.12402305BV_2\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/12/12402305BV_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"134.0\"\n" +
                "                },\n" +
                "                \"title\": \"MAJESTIC FILATURES T-shirts\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.561FCF54C6438F0E.12222228DX_4\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/12/12222228DX_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"119.0\"\n" +
                "                },\n" +
                "                \"title\": \"KAOS Sweaters\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.6F14195263208D09.12475692PM_3\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/12/12475692PM_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"60.0\"\n" +
                "                },\n" +
                "                \"title\": \"LABORATORIO T-shirts\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.5B002E3F207AE00E.14089493RP_6\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/14/14089493RP_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"430.0\"\n" +
                "                },\n" +
                "                \"title\": \"WLNS by WELLNESS Sweaters\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.A86A9DE94DA6A00D.14089493RP_4\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/14/14089493RP_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"430.0\"\n" +
                "                },\n" +
                "                \"title\": \"WLNS by WELLNESS Sweaters\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.2E33D90466AFB667.14088145DA_5\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/14/14088145DA_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"163.0\"\n" +
                "                },\n" +
                "                \"title\": \"KANGRA CASHMERE Sweaters\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.B40C3F3D7FCF53BE.14035886RU_6\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/14/14035886RU_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"179.0\"\n" +
                "                },\n" +
                "                \"title\": \"LAMBERTO LOSANI Sweaters\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.319679EE77B0ACD0.39904389MT_3\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/39/39904389MT_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"72.0\"\n" +
                "                },\n" +
                "                \"title\": \"ROBERTO COLLINA Sweaters\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"YOOX-AF-US_159390.1.5EDD.F708BC79943FFBE.39904389MT_5\",\n" +
                "            \"main_image_url\": \"https://cdn.yoox.biz/39/39904389MT_14_F.JPG\",\n" +
                "            \"data\": {\n" +
                "                \"price\": {\n" +
                "                    \"currency\": \"USD\",\n" +
                "                    \"value\": \"72.0\"\n" +
                "                },\n" +
                "                \"title\": \"ROBERTO COLLINA Sweaters\"\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"catalog_fields_mapping\": {},\n" +
                "    \"page\": 1,\n" +
                "    \"limit\": 10,\n" +
                "    \"total\": 1000\n" +
                "}";

        ProductResponse response = gson.fromJson(successResp, ProductResponse.class);
        searchService.handleResponse(response, new ProductSearch.ResultListener() {
            @Override
            public void onSearchResult(ProductResponse response, String errorMsg) {
                assertNull(errorMsg);
                assertEquals("202101213651d741543e7d0145167566a37d3dbf340a4e2c514.jpg", response.getImId());
                assertEquals("017722d8b9cfcda58402b1f1b4c1e6", response.getReqId());
                assertEquals(1, response.getProductTypes().size());

                assertEquals(10, response.getLimit());
                assertEquals(1000, response.getTotal());
                ProductType type = response.getProductTypes().get(0);
                assertEquals("top", type.getType());
                assertEquals(0.715f, type.getScore(), 0.000001f);

                assertEquals(156, type.getBox().getX1().intValue());
                assertEquals(96, type.getBox().getY1().intValue());
                assertEquals(1420, type.getBox().getX2().intValue());
                assertEquals(1889, type.getBox().getY2().intValue());

                assertEquals(10, response.getProductResults().size());
                ProductResult result = response.getProductResults().get(0);

                assertEquals("YOOX-AF-US_159390.1.5EDD.78059CB19EE49440.12339652SL_2", result.getProductId());
                assertEquals("https://cdn.yoox.biz/12/12339652SL_14_F.JPG", result.getImageUrl());

                Map<String, Object> data = result.getData();
                assertEquals("MAJESTIC FILATURES T-shirts", data.get("title"));
                Map price = (Map<String, String>) data.get("price");
                assertEquals("USD", price.get("currency"));
                assertEquals("109.0", price.get("value"));

            }
        });


    }



}
