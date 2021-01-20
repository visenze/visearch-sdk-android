package com.visenze.product.search;

import com.google.gson.JsonObject;
import com.visenze.product.search.network.RetrofitQueryMap;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testBaseSearchParams_parsing() {
        BaseSearchParams params = new BaseSearchParams();
        params.setFacetLimit(10);
        params.setDebug(true);
        params.setPlacementId("ssssss");

        List<String> filters = new ArrayList<>();
        filters.add("country:CN");
        filters.add("source:TB");
        params.setFilters(filters);

        List<String> facets = new ArrayList<>();
        facets.add("country");
        params.setFacets(facets);

        RetrofitQueryMap map = params.getQueryMap();


        assertEquals("10", map.get("facet_limit"));
        assertEquals("true", map.get("debug"));
        assertEquals("ssssss", map.get("placement_id"));


    }


}