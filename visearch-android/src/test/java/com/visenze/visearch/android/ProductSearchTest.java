package com.visenze.visearch.android;


import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.visenze.visearch.android.model.ErrorData;
import com.visenze.visearch.android.model.Experiment;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.android.model.ObjectResult;
import com.visenze.visearch.android.model.ProductObject;
import com.visenze.visearch.android.model.ProductResponse;
import com.visenze.visearch.android.model.Product;
import com.visenze.visearch.android.model.ProductType;
import com.visenze.visearch.android.network.ProductSearchService;
import com.visenze.visearch.android.network.RetrofitQueryMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ProductSearchTest {
    ProductSearchService searchService = new ProductSearchService("https://visearch.visenze.com", "123", 456, "visearch-test");
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    @Test
    public void testBaseSearchParams_parsing() {
        ProductSearchByImageParams params = new ProductSearchByImageParams();
        params.setFacetLimit(10);
        params.setImId("1234555");


        Map<String, String> customMap = new HashMap<String, String>();
        customMap.put("testParam", "testParamVal");
        customMap.put("testParam2", "testParamVal2");
        customMap.put("debug", "true");
        params.setCustomParams(customMap);


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
            public void onSearchResult(ProductResponse response, ErrorData error) {
                assertNull(response);
                assertEquals("Please provide ''image'', ''im_url'' or ''im_id'' parameter.", error.getMessage());
            }
        });
    }

    @Test
    public void testObjectResponse() {
        String successResp = "{\n" +
                "    \"im_id\": \"202103093651675ff131da7abc242e2adf1dceed5177ca3110e.jpg\",\n" +
                "    \"reqid\": \"0178162043b8002b2c6853f0a8740f\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"product/search_by_image\",\n" +
                "    \"page\": 1,\n" +
                "    \"product_types\": [],\n" +
                "    \"objects\": [\n" +
                "        {\n" +
                "            \"type\": \"top\",\n" +
                "            \"score\": 0.91,\n" +
                "            \"box\": [\n" +
                "                14,\n" +
                "                143,\n" +
                "                807,\n" +
                "                870\n" +
                "            ],\n" +
                "            \"attributes\": {},\n" +
                "            \"total\": 1000,\n" +
                "            \"result\": [\n" +
                "                {\n" +
                "                    \"product_id\": \"YOOX-AF-US_159390.1.5EDD.1FF02CAF2478AE01.12556480RV_7\",\n" +
                "                    \"main_image_url\": \"https://cdn.yoox.biz/12/12556480RV_14_F.JPG\",\n" +
                "                    \"data\": {\n" +
                "                        \"price\": {\n" +
                "                            \"currency\": \"USD\",\n" +
                "                            \"value\": \"44.0\"\n" +
                "                        },\n" +
                "                        \"title\": \"ADIDAS ORIGINALS T-shirts\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"product_id\": \"FARFETCH-PRF-AF-GB_16172209\",\n" +
                "                    \"main_image_url\": \"https://cdn-images.farfetch-contents.com/16/17/22/09/16172209_31551083_800.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"price\": {\n" +
                "                            \"currency\": \"GBP\",\n" +
                "                            \"value\": \"54.0\"\n" +
                "                        },\n" +
                "                        \"title\": \"Diesel Kids - TEEN logo short-sleeve T-shirt - kids - Polyester/Cotton - 14 yrs, 16 yrs - White\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"product_id\": \"YOOX-AF-US_159390.1.5EDD.4521B2BD301631D9.12558949VR_5\",\n" +
                "                    \"main_image_url\": \"https://cdn.yoox.biz/12/12558949VR_14_F.JPG\",\n" +
                "                    \"data\": {\n" +
                "                        \"price\": {\n" +
                "                            \"currency\": \"USD\",\n" +
                "                            \"value\": \"84.0\"\n" +
                "                        },\n" +
                "                        \"title\": \"NONNATIVE T-shirts\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"product_id\": \"MYTHERESA-US_180260.2.7FDB09775E3B9DE4.7FE5A568AC4C54A8.P00535115-1\",\n" +
                "                    \"main_image_url\": \"https://img.mytheresa.com/1000/1000/95/jpeg/catalog/product/64/P00535115.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"price\": {\n" +
                "                            \"currency\": \"USD\",\n" +
                "                            \"value\": \"89.0\"\n" +
                "                        },\n" +
                "                        \"title\": \"Play logo cotton T-shirt\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"product_id\": \"MYTHERESA-US_180260.2.7FDB09775E3B9DE4.8A5544F8CC27465A.P00535115-3\",\n" +
                "                    \"main_image_url\": \"https://img.mytheresa.com/1000/1000/95/jpeg/catalog/product/64/P00535115.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"price\": {\n" +
                "                            \"currency\": \"USD\",\n" +
                "                            \"value\": \"89.0\"\n" +
                "                        },\n" +
                "                        \"title\": \"Play logo cotton T-shirt\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"result\": []\n" +
                "}";
        ProductResponse response = gson.fromJson(successResp, ProductResponse.class);
        searchService.handleResponse(response, new ProductSearch.ResultListener() {
            @Override
            public void onSearchResult(ProductResponse response, ErrorData error) {
                assertNull(error);
                assertEquals("202103093651675ff131da7abc242e2adf1dceed5177ca3110e.jpg", response.getImId());
                assertEquals("0178162043b8002b2c6853f0a8740f", response.getReqId());
                assertEquals(1, response.getObjects().size());

                ProductObject obj = response.getObjects().get(0);
                assertEquals("top", obj.getType());

                assertEquals(5, obj.getResult().size());
                Product result = obj.getResult().get(0);

                assertEquals("YOOX-AF-US_159390.1.5EDD.1FF02CAF2478AE01.12556480RV_7", result.getProductId());
                assertEquals("https://cdn.yoox.biz/12/12556480RV_14_F.JPG", result.getImageUrl());

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
            public void onSearchResult(ProductResponse response, ErrorData error) {
                assertNull(error);
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

                assertEquals(10, response.getProducts().size());
                Product result = response.getProducts().get(0);

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


    @Test
    public void testRecommendationsResponse() {
        String json = "{\n" +
                "    \"reqid\": \"017a3bb0a050fb56218beb28b9e5ec\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"product/recommendations\",\n" +
                "    \"page\": 1,\n" +
                "    \"limit\": 10,\n" +
                "    \"total\": 2,\n" +
                "    \"product_types\": [],\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"product_id\": \"top-name-1\",\n" +
                "            \"main_image_url\": \"https://localhost/top-name-1.jpg\",\n" +
                "            \"data\": {\n" +
                "                \"title\": \"top-name-001\"\n" +
                "            },\n" +
                "            \"tags\": {\n" +
                "                \"category\": \"top\"\n" +
                "            },\n" +
                "            \"alternatives\": [\n" +
                "                {\n" +
                "                    \"product_id\": \"top-name-2\",\n" +
                "                    \"main_image_url\": \"https://localhost/top-name-2.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"top-name-002\"\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"product_id\": \"top-name-3\",\n" +
                "                    \"main_image_url\": \"https://localhost/top-name-3.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"top-name-003\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"product_id\": \"pants-name-1\",\n" +
                "            \"main_image_url\": \"https://localhost/pants-name-1.jpg\",\n" +
                "            \"data\": {\n" +
                "                \"title\": \"pants-name-001\"\n" +
                "            },\n" +
                "            \"tags\": {\n" +
                "                \"category\": \"pants\"\n" +
                "            },\n" +
                "            \"alternatives\": [\n" +
                "                {\n" +
                "                    \"product_id\": \"pants-name-2\",\n" +
                "                    \"main_image_url\": \"https://localhost/pants-name-2.jpg\",\n" +
                "                    \"data\": {\n" +
                "                        \"title\": \"pants-name-002\"\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"strategy\": {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Visually similar\",\n" +
                "        \"algorithm\": \"VSR\"\n" +
                "    },\n" +
                "    \"alt_limit\": 5\n" +
                "}";

        ProductResponse response = gson.fromJson(json, ProductResponse.class);
        searchService.handleResponse(response, new ProductSearch.ResultListener() {
            @Override
            public void onSearchResult(ProductResponse response, ErrorData error) {
                assertNull(error);

                assertEquals(1, response.getPage());
                assertEquals(10, response.getLimit());
                assertEquals(2, response.getTotal());
                assertEquals(5, response.getAltLimit().intValue());
                assertEquals(1, response.getStrategy().getId().intValue());
                assertEquals("Visually similar", response.getStrategy().getName());
                assertEquals("VSR", response.getStrategy().getAlgorithm());
                assertEquals(2, response.getProducts().size());

                assertEquals("top-name-1", response.getProducts().get(0).getProductId());
                assertEquals("top", response.getProducts().get(0).getTags().get("category"));
                assertEquals("https://localhost/top-name-1.jpg", response.getProducts().get(0).getImageUrl());
                assertEquals("top-name-001", response.getProducts().get(0).getData().get("title"));
                assertEquals(2, response.getProducts().get(0).getAlternatives().size());
                assertEquals("top-name-2", response.getProducts().get(0).getAlternatives().get(0).getProductId());
                assertEquals("https://localhost/top-name-2.jpg", response.getProducts().get(0).getAlternatives().get(0).getImageUrl());
                assertEquals("top-name-002", response.getProducts().get(0).getAlternatives().get(0).getData().get("title"));
                assertEquals("top-name-3", response.getProducts().get(0).getAlternatives().get(1).getProductId());
                assertEquals("https://localhost/top-name-3.jpg", response.getProducts().get(0).getAlternatives().get(1).getImageUrl());
                assertEquals("top-name-003", response.getProducts().get(0).getAlternatives().get(1).getData().get("title"));

                assertEquals("pants-name-1", response.getProducts().get(1).getProductId());
                assertEquals("pants", response.getProducts().get(1).getTags().get("category"));
                assertEquals("https://localhost/pants-name-1.jpg", response.getProducts().get(1).getImageUrl());
                assertEquals("pants-name-001", response.getProducts().get(1).getData().get("title"));
                assertEquals(1, response.getProducts().get(1).getAlternatives().size());
                assertEquals("pants-name-2", response.getProducts().get(1).getAlternatives().get(0).getProductId());
                assertEquals("https://localhost/pants-name-2.jpg", response.getProducts().get(1).getAlternatives().get(0).getImageUrl());
                assertEquals("pants-name-002", response.getProducts().get(1).getAlternatives().get(0).getData().get("title"));

            }
        });

    }

    @Test
    public void testExperimentResponse() {
        String json =
                "{\n" +
                "    \"reqid\": \"01806a667776c6f8a31c28105fd99b\",\n" +
                "    \"status\": \"OK\",\n" +
                "    \"method\": \"product/recommendations\",\n" +
                "    \"page\": 1,\n" +
                "    \"limit\": 10,\n" +
                "    \"total\": 2,\n" +
                "    \"product_types\": [],\n" +
                "    \"result\": [\n" +
                "    ],\n" +
                "    \"strategy\": {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"test\",\n" +
                "        \"algorithm\": \"VSR\"\n" +
                "    },\n" +
                "   \"experiment\": {\n" +
                "        \"experiment_id\": 522,\n" +
                "        \"variant_id\": 2019,\n" +
                "        \"strategy_id\": 3,\n" +
                "        \"experiment_no_recommendation\": false,\n" +
                "        \"debug\": {\n" +
                "            \"experimentDebugLogs\": [\n" +
                "                {\n" +
                "                    \"experimentID\": 522,\n" +
                "                    \"msg\": \"matched all constraints. rollout yes. {BucketNum:260 DistributionArray:{VariantIDs:[2019 2020] PercentsAccumulated:[500 1000]} VariantID:2019 RolloutPercent:100}\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }," +
                "    \"alt_limit\": 15\n" +
                "}";
        ProductResponse response = gson.fromJson(json, ProductResponse.class);
        searchService.handleResponse(response, new ProductSearch.ResultListener() {
            @Override
            public void onSearchResult(ProductResponse response, ErrorData error) {
                assertNull(error);

                assertEquals(15, response.getAltLimit().intValue());
                assertEquals(3, response.getStrategy().getId().intValue());
                assertEquals("test", response.getStrategy().getName());
                assertEquals("VSR", response.getStrategy().getAlgorithm());
                assertEquals(0, response.getProducts().size());

                Experiment experiment = response.getExperiment();
                assertEquals(522, experiment.getExperimentId());
                assertEquals(2019, experiment.getVariantId());
                assertFalse(experiment.isExpNoRecommendation());
                assertTrue(3 == experiment.getStrategyId());

            }
        });

    }

    @Test
    public void testPinExcludeResponse() {
        String json =
                "{\n" +
                        "    \"reqid\": \"01806a667776c6f8a31c28105fd99e\",\n" +
                        "    \"status\": \"OK\",\n" +
                        "    \"method\": \"product/recommendations\",\n" +
                        "    \"page\": 1,\n" +
                        "    \"limit\": 10,\n" +
                        "    \"total\": 1,\n" +
                        "    \"product_types\": [],\n" +
                        "    \"result\": [\n" +
                        "        {\n" +
                        "            \"product_id\": \"top-name-11\",\n" +
                        "            \"main_image_url\": \"https://localhost/top-name-11.jpg\",\n" +
                        "            \"data\": {\n" +
                        "                \"title\": \"top-name-001\"\n" +
                        "            },\n" +
                        "            \"tags\": {\n" +
                        "                \"category\": \"top\"\n" +
                        "            },\n" +
                        "            \"pinned\": \"true\",\n" +
                        "            \"alternatives\": [\n" +
                        "                {\n" +
                        "                    \"product_id\": \"top-name-22\",\n" +
                        "                    \"main_image_url\": \"https://localhost/top-name-22.jpg\",\n" +
                        "                    \"data\": {\n" +
                        "                        \"title\": \"top-name-002\"\n" +
                        "                    }\n" +
                        "                },\n" +
                        "                {\n" +
                        "                    \"product_id\": \"top-name-33\",\n" +
                        "                    \"main_image_url\": \"https://localhost/top-name-33.jpg\",\n" +
                        "                    \"data\": {\n" +
                        "                        \"title\": \"top-name-003\"\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"strategy\": {\n" +
                        "        \"id\": 3,\n" +
                        "        \"name\": \"test\",\n" +
                        "        \"algorithm\": \"STL\"\n" +
                        "    },\n" +
                        "    \"excluded_pids\" : [\"p1\", \"p2\"],\n" +
                        "    \"alt_limit\": 5\n" +
                        "}";

        ProductResponse response = gson.fromJson(json, ProductResponse.class);
        searchService.handleResponse(response, new ProductSearch.ResultListener() {
            @Override
            public void onSearchResult(ProductResponse response, ErrorData error) {
                assertNull(error);

                assertTrue(response.getProducts().get(0).getPinned());
                assertEquals(2, response.getExcludedPids().size());
                assertEquals("p1" , response.getExcludedPids().get(0));
                assertEquals("p2" , response.getExcludedPids().get(1));
            }
        });

    }
}
