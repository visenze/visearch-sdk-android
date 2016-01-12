package com.visenze.visearch.camerademo.util;

import com.visenze.visearch.android.BaseSearchParams;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.model.Box;
import com.visenze.visearch.android.model.ProductType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.littlecheesecake.croplayout.model.ScalableBox;

/**
 * Created by visenze on 16/12/15.
 */
public class DataHelper {

    /**
     * get the detected type from response, set as all if null
     * @param detectedTypes
     * @return
     */
    public static ProductType getSelectedProductType(List<ProductType> detectedTypes) {
        ProductType selectedType;

        if (detectedTypes.size() > 0) {
            selectedType = detectedTypes.get(0);
        } else {
            selectedType = new ProductType();
            selectedType.setType("other");
        }

        return selectedType;
    }

    public static List<ProductType> copyProductTypeList(List<ProductType> productTypeList) {
        List<ProductType> newProductList = new ArrayList<>();
        for (ProductType p : productTypeList) {
            ProductType newP = new ProductType();
            newP.setType(p.getType());
            newP.setBox(p.getBox());
            newP.setScore(p.getScore());

            newProductList.add(p);
        }

        return newProductList;
    }

    public static List<String> getSupportedTypeList(List<ProductType> supportedTypeList, String detectedType) {
        List<String> productList = new ArrayList<>();
        if (detectedType != null) {
            productList.add(detectedType);
        }

        for (ProductType productType : supportedTypeList) {
            if (!productType.getType().equals(detectedType)) {
                productList.add(productType.getType());
            }
        }

        return productList;
    }

    public static String getEstimatedType(ScalableBox box, List<ProductType> productTypes) {
        String type = "all";
        int maxIndex = -1;
        float maxOverlap = 0;
        for (int i = 0; i < productTypes.size() && (!productTypes.get(i).getType().equals("other")); i++) {
            Box productBox = productTypes.get(i).getBox();
            float overlap = calculateOverlap(box.getX1(), box.getX2(), box.getY1(), box.getY2(),
                    productBox.getX1(), productBox.getX2(), productBox.getY1(), productBox.getY2());
            if (overlap > maxOverlap) {
                maxOverlap = overlap;
                maxIndex = i;
            } else if (overlap == maxOverlap && maxOverlap > 1f) {
                return type;
            }
        }

        if (maxOverlap > 0.5f && maxIndex > -1) {
            type = productTypes.get(maxIndex).getType();
        }

        return type;
    }

    public static UploadSearchParams setSearchParams(UploadSearchParams uploadSearchParams, String detection) {
        List<String> fl = new ArrayList<>();
        fl.add("im_url");
        Map<String, String> fq = new HashMap<>();
        uploadSearchParams.getBaseSearchParams().setFq(fq);
        uploadSearchParams.getBaseSearchParams().setFl(fl);
        uploadSearchParams.getBaseSearchParams().setLimit(30);
        uploadSearchParams.getBaseSearchParams().setScore(true);

        //set detection
        Map<String, String> map = new HashMap<>();
        if (detection != null)
            uploadSearchParams.setDetection(detection);

        //set admin
        map.put("vtt_source", "visenze_admin");

        uploadSearchParams.getBaseSearchParams().setCustom(map);

        return uploadSearchParams;
    }

    public static BaseSearchParams setIdSearchParams(BaseSearchParams baseSearchParams) {
        List<String> fl = new ArrayList<>();
        fl.add("im_url");
        Map<String, String> fq = new HashMap<>();
        baseSearchParams.setScore(true);
        baseSearchParams.setLimit(30);
        baseSearchParams.setFl(fl);

        //set admin
        //set detection
        Map<String, String> map = new HashMap<>();
        //set admin
        map.put("vtt_source", "visenze_admin");
        baseSearchParams.setCustom(map);

        return baseSearchParams;
    }

    private static float calculateOverlap(int x1, int x2, int y1, int y2,
                                   int x1p, int x2p, int y1p, int y2p) {
        int width = Math.min(x2, x2p) - Math.max(x1, x1p);
        int height = Math.min(y2, y2p) - Math.max(y1, y1p);
        int overlayArea = 0;
        if (width > 0 && height > 0) {
            overlayArea = width * height;
        }
        int wholeArea = (x2 - x1) * (y2 - y1) + (x2p - x1p) * (y2p - y1p) - overlayArea;

        return (wholeArea == 0 )? 0f : overlayArea / ((float)wholeArea) ;
    }
}
