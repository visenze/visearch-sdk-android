package com.visenze.visearch.android.util;

import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.ViSearchException;
import com.visenze.visearch.android.model.Box;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.android.model.ProductType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Parses ViSearch Search API JSON responses.
 */
public class ResponseParser {

    public static ResultList parseResult(String jsonResponse) {
        try {
            ResultList resultList = new ResultList();

            JSONObject resultObj = new JSONObject(jsonResponse);
            resultList.setErrorMessage(parseResponseError(resultObj));

            resultList.setTotal(resultObj.getInt("total"));
            resultList.setPage(resultObj.getInt("page"));
            resultList.setLimit(resultObj.getInt("limit"));

            if (resultObj.has("transId")) {
                resultList.setTransId(resultObj.getString("transId"));
            }

            if (resultObj.has("qinfo")) {
                JSONObject qinfoObj = resultObj.getJSONObject("qinfo");
                resultList.setQueryInfo(parseQueryInfo(qinfoObj));
            }

            JSONArray resultArray = resultObj.getJSONArray("result");
            resultList.setImageList(parseImageResultList(resultArray));

            if (resultObj.has("product_types")) {
                JSONArray productTypeArray = resultObj.getJSONArray("product_types");
                resultList.setProductTypes(parseProductTypeList(productTypeArray));
            }

            if (resultObj.has("product_types_list")) {
                JSONArray productTypeListArray = resultObj.getJSONArray("product_types_list");
                resultList.setSupportedProductTypeList(parseSupportedProductTypeList(productTypeListArray));
            }

            if (resultObj.has("im_id")) {
                resultList.setImId(resultObj.getString("im_id"));
            }

            return resultList;
        } catch (JSONException e) {
            throw new ViSearchException("Error parsing response " + e.getMessage(), e);
        }
    }

    private static Map<String, String> parseQueryInfo(JSONObject qinfoObj) {
        Map<String, String> queryInfo = new HashMap<String, String>();
        try {
            Iterator<String> nameItr = qinfoObj.keys();
            while (nameItr.hasNext()) {
                String name = nameItr.next();
                queryInfo.put(name, qinfoObj.getString(name));
            }
        } catch (JSONException e) {
            throw new ViSearchException("JsonParse Error: " + e.toString());
        }

        return queryInfo;
    }

    private static List<ImageResult> parseImageResultList(JSONArray resultArray) throws ViSearchException {
        List<ImageResult> resultList = new ArrayList<ImageResult>();
        int size = resultArray.length();

        try {
            for (int i = 0; i < size; i++) {
                JSONObject imageObj = resultArray.getJSONObject(i);
                ImageResult imageResult = new ImageResult();
                imageResult.setImageName(imageObj.getString("im_name"));

                if (imageObj.has("score")) {
                    imageResult.setScore((float)imageObj.getDouble("score"));
                }

                if (imageObj.has("value_map")) {
                    JSONObject valueObj = imageObj.getJSONObject("value_map");
                    Map<String, String> fieldList = new HashMap<String, String>();
                    Iterator<String> nameItr = valueObj.keys();
                    while (nameItr.hasNext()) {
                        String name = nameItr.next();
                        fieldList.put(name, valueObj.getString(name));

                        if (name.equals("im_url")) {
                            imageResult.setImageUrl(valueObj.getString(name));
                        }
                    }
                    imageResult.setFieldList(fieldList);
                }

                resultList.add(imageResult);
            }
        } catch (JSONException e) {
            throw new ViSearchException("Error parsing response result " + e.getMessage(), e);
        }

        return resultList;
    }

    private static List<ProductType> parseProductTypeList(JSONArray resultArray) throws ViSearchException {
        List<ProductType> resultList = new ArrayList<ProductType>();
        int size = resultArray.length();

        try {
            for (int i = 0; i < size; i++) {
                JSONObject typeObj = resultArray.getJSONObject(i);
                ProductType productType = new ProductType();
                productType.setType(typeObj.getString("type"));
                productType.setScore(typeObj.getDouble("score"));
                JSONArray boxCoordinate = typeObj.getJSONArray("box");
                Box box = new Box(boxCoordinate.getInt(0),
                        boxCoordinate.getInt(1),
                        boxCoordinate.getInt(2),
                        boxCoordinate.getInt(3));
                productType.setBox(box);

                if (typeObj.has("attributes")) {
                    JSONObject attrsArray = typeObj.getJSONObject("attributes");
                    JSONArray attrsNames = attrsArray.names();
                    Map attrsMapList = new HashMap<>();
                    for (int j = 0; attrsNames != null && j < attrsNames.length(); j++) {
                        JSONArray attrsItems = (JSONArray) attrsArray.get((String)attrsNames.get(j));
                        List<String> attrs = new ArrayList<>();
                        for (int k = 0; k < attrsItems.length(); k++) {
                            attrs.add((String) attrsItems.get(k));
                        }
                        attrsMapList.put(attrsNames.get(j), attrs);
                    }
                    productType.setAttributeList(attrsMapList);
                }

                resultList.add(productType);
            }
        } catch (JSONException e) {
            throw new ViSearchException("Error parsing response result " + e.getMessage(),e);
        }

        return resultList;
    }

    private static List<ProductType> parseSupportedProductTypeList(JSONArray resultArray) {
        int size = resultArray.length();
        List<ProductType> supportedTypeList = new ArrayList<>();

        try {
            for (int i = 0; i < size; i++) {
                JSONObject typeObj = resultArray.getJSONObject(i);
                ProductType productType = new ProductType();
                productType.setType(typeObj.getString("type"));

                if (typeObj.has("attributes_list")) {
                    JSONObject attrsArray = typeObj.getJSONObject("attributes_list");
                    JSONArray attrsNames = attrsArray.names();
                    Map attrsMapList = new HashMap<>();
                    for (int j = 0; attrsNames != null && j < attrsNames.length(); j++) {
                        JSONArray attrsItems = (JSONArray) attrsArray.get((String)attrsNames.get(j));
                        List<String> attrs = new ArrayList<>();
                        for (int k = 0; k < attrsItems.length(); k++) {
                            attrs.add((String) attrsItems.get(k));
                        }
                        attrsMapList.put(attrsNames.get(j), attrs);
                    }
                    productType.setAttributeList(attrsMapList);
                }

                supportedTypeList.add(productType);
            }
        } catch (JSONException e) {
            throw new ViSearchException("Error parsing response result " + e.getMessage(), e);
        }

        return supportedTypeList;
    }

    private static String parseResponseError(JSONObject jsonObj) {
        try {
            String status = jsonObj.getString("status");
            if (status == null) {
                throw new ViSearchException("Error parsing response: status is null");
            } else {
                if (status.equals("OK")) {
                    return null;
                } else {
                    if (!jsonObj.has("error") || jsonObj.getJSONArray("error").length() == 0) {
                        return "Error parsing response: missing error";
                    } else {
                        return jsonObj.getJSONArray("error").get(0).toString();
                    }
                }
            }
        } catch (JSONException e) {
            throw new ViSearchException("Error parsing response " + e.getMessage(), e);
        }
    }
}
