# ProductSearch

With the release of ViSenze's Catalog system, ViSearch Android SDK will now include the Product Search API suite - an additional set of APIs that ideally can achieve the following:

- Select the right product image for indexing for best search performance
- Aggregate search results on a product level instead of image level
- Consistent data type in API response with Catalog’s schema

> Current stable version: 2.4.1
> Minimum Android SDK Version: API 9, Android 2.3

---

## Table of Contents

- [ProductSearch](#productsearch)
  - [Table of Contents](#table-of-contents)
  - [1. Setup](#1-setup)
    - [1.1 Running the Demo](#11-running-the-demo)
    - [1.2 Installing the SDK](#12-installing-the-sdk)
    - [1.3 Add User Permissions](#13-add-user-permissions)
  - [2. Initialization](#2-initialization)
  - [3. Solution APIs](#3-solution-apis)
    - [3.1 Search By Image](#31-search-by-image)
    - [3.2 Recommendations](#32-recommendations)
    - [3.3 Multisearch](#33-multisearch)
    - [3.4 Multisearch Autocomplete](#34-multisearch-autocomplete)
    - [3.5 Multisearch Complementary](#35-multisearch-complementary)
    - [3.6 Multisearch Outfit Recommendations](#36-multisearch-outfit-recommendations)
  - [4. Search Parameters](#4-search-parameters)
    - [4.1 BaseProductSearchParams](#41-baseproductsearchparams)
    - [4.2 ProductSearchByImageParams](#42-productsearchbyimageparams)
    - [4.3 ProductSearchByIdParams](#43-productsearchbyidparams)
  - [5. Search Results](#5-search-results)
    - [5.1 ProductResponse](#51-productresponse)
    - [5.2 ErrorData](#52-errordata)
    - [5.3 ProductType](#53-producttype)
    - [5.4 Product](#54-product)
      - [5.4.1 Data](#541-data)
    - [5.5 ProductObject](#55-productobject)
    - [5.6 Facet](#56-facet)
    - [5.7 FacetItem](#57-facetitem)
    - [5.8 FacetRange](#58-facetrange)
    - [5.9 AutoCompleteResponse](#59-autocompleteresponse)
      - [5.9.1 AutoCompleteResultItem](#591-autocompleteresultitem)
  - [6. Search Examples](#6-search-examples)
  - [7. Event Tracking](#7-event-tracking)
    - [7.1 Setup Tracking](#71-setup-tracking)
    - [7.2 Send Events](#72-send-events)

---

## 1. Setup

### 1.1 Running the Demo

The source code of a demo application is provided together with the SDK ([demo](https://github.com/visenze/visearch-sdk-android/tree/master/cameraDemo)). You can simply open **visearch-sdk-android** project in Android Studio and run the **cameraDemo** project.

### 1.2 Installing the SDK

As bintray has expired. we have moved our repository to jitpack. Add repository in your root build.gradle

```gradle
allprojects {
 repositories {
  ...
  maven { url 'https://jitpack.io' }
 }
}
```

include the dependency in your project using gradle. Please change the version to latest.

```gradle
implementation 'com.github.visenze:visenze-tracking-android:0.2.3'
implementation 'com.github.visenze:visearch-sdk-android:2.5.0'
```

### 1.3 Add User Permissions

Our SDK additionally needs these user permissions to work. Add the following declarations to the `AndroidManifest.xml` file. Note that camera permission is optional.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.visenze.android.visenze_demo_test">

    <uses-permission android:name="android.permission.CAMERA"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

    <application>
    ...
    </application>
</manifest>
```

> Network permission allows the app to connect to network services. Write/read to external storage permissions allow the app to load and save images on the device.

## 2. Initialization

`ProductSearch` must be initialized with an app key and a placement id before it can be used. In order for it to be notified of the search result, `ProductSearch.ResultListener` callback must be provided when making the actual API call.

```java
public class MyActivity extends Activity {
    private static final String appKey = "YOUR_APP_KEY";
    private static final Integer placementId = 1;
    ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SearchAPI.initProductSearchAPI(this, appKey, placementId);
        ...
    }
    ...
}
```

Please init ProductSearch client with the following if there is a need for changing the default endpoint `https://search.visenze.com`.

```java
ProductSearch productSearch = new ProductSearch
                            .Builder(appKey, placementId)
                            .setApiEndPoint("https://custom-visearch.yourdomain.com")
                            .build(context);
```

For searches in China, please change the endpoint to `https://search.visenze.com.cn`.

## 3. Solution APIs

There are two main APIs provided in this suite, one allows searching for products based on an image input, the other searches using a product's ID (Recommendations API). A product's ID can be retrieved from a [Search Result](#5-search-results).

### 3.1 Search By Image

POST /v1/product/search_by_image

Searching by Image can happen in three different ways - by url, id or File. Assuming that you have initialized the SDK according to section [Initialization](#2-initialization):

- Example of image URL:

```java
String imageUrl = "https://some_website.com/some_image.jpg";
ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);
ProductSearch ps = SearchAPI.getProductSearchInstance();
ps.searchByImage(params, new ProductSearch.ResultListener() {
    @Override
    public void onSearchResult(ProductResponse response, ErrorData error) {
        // LOGIC HERE
    }
});
```

- Example of image ID:

```java
public void yourFunction(ProductResponse yourPriorResponse)
{
    String imageID = yourPriorResponse.getImId();
    ProductSearchByImageParams params = new ProductSearchByImageParams(imageID);
    ProductSearch ps = SearchAPI.getProductSearchInstance();
    ps.searchByImage(params, new ProductSearch.ResultListener() {
        @Override
        public void onSearchResult(ProductResponse response, ErrorData error) {
            // LOGIC HERE
        }
    });
}
```

> Image ID refers to the ID that is assigned to each image that the API receives. Meaning, on every successful search (via URl or File), the image will have an ID assigned to it that can be reused. The example above assumes that you have stored a prior successful ProductResponse somewhere and are using it as a parameter.

- Example of image File:

```java
@Override
public void OnImageCaptured(Image image, String imagePath) {
    ProductSearchByImageParams params = new ProductSearchByImageParams(image);
    ProductSearch ps = SearchAPI.getProductSearchInstance();
    ps.searchByImage(params, new ProductSearch.ResultListener() {
        @Override
        public void onSearchResult(ProductResponse response, ErrorData error) {
            // LOGIC HERE
        }
    });
}
```

> Image File refers to an actual file with bytes representing the image (i.e. opened from file upload, or taken from camera). The example above is in a scenario where the android camera captures an image. You can construct the Image object by doing 1 of the following:

- Using an image from a local file path:

```java
Image image = new Image("/local/path/to/image.jpg");
```

- Using an image by providing the Uri of the image in photo gallery:

```java
Image image = new Image(context, uri);
```

- Construct the `image` from the byte array returned by the camera preview callback:

```java
@Override
public void onPictureTaken(byte[] bytes, Camera camera) {
    Image image = new Image(bytes);
}
```

### 3.2 Recommendations

GET /v1/product/recommendations/{product_id}

Sample code for calling Recommendations:

```java
...
public void yourFunction(ProductResponse yourPriorResponse)
{
    String productID = yourPriorResponse.getProducts().get(0).getProductId();
    ProductSearchByIdParams params = new ProductSearchByIdParams(productID);
    ProductSearch ps = SearchAPI.getProductSearchInstance();
    ps.recommendations(params, new ProductSearch.ResultListener()) {
        @Override
        public void onSearchResult(ProductResponse response, ErrorData error) {
            // LOGIC HERE
        }       
    }

}
```

The example above assumes that you have stored a prior successful ProductResponse somewhere and are using it as a parameter. The `yourPriorResponse.getProducts().get(0).getProductId()` is an arbitrary product ID, you should implement a proper way of choosing which product in the response's products list to use.

> Pseudo steps:
>
> - make an initial search using an image
> - store the search results (contains the list of products)
> - use the product ID of one of these products in the next search

### 3.3 Multisearch

POST /v1/product/multisearch

Multimodal Search API: given an input text and/or input image and/or product ID finds all the products matching the inputs.

The API requires 1 of the following params: product ID, text query, or image (via url, file or image ID). Assuming that you have initialized the SDK according to section [Initialization](#2-initialization).

Sample code for searching with text query:

```java
String imageUrl = "https://some_website.com/some_image.jpg";
ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);
params.setQ("your-text-query");

ProductSearch ps = SearchAPI.getProductSearchInstance();
ps.multisearch(params, new ProductSearch.ResultListener() {
    @Override
    public void onSearchResult(ProductResponse response, ErrorData error) {
        // LOGIC HERE
    }
});
```

### 3.4 Multisearch autocomplete

POST /v1/product/multisearch/autocomplete

Multisearch autocomplete can happen in four different ways - by text, url, id or File. Assuming that you have initialized the SDK according to section [Initialization](#2-initialization):

For url, id or File example, you can refer to [Search by image](#31-search-by-image)

Sample code for searching with a partial text query:

```java
String imageUrl = "https://some_website.com/some_image.jpg";
ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);
params.setQ("your-partial-text-query");

ProductSearch ps = SearchAPI.getProductSearchInstance();
ps.multisearchAutocomplete(params, new ProductSearch.AutoCompleteResultListener() {
    @Override
    public void onResult(AutoCompleteResponse response, ErrorData error) {
        // LOGIC HERE
    }
});
```

### 3.5 Multisearch Complementary

POST /v1/product/multisearch/complementary

Multimodal Complemetary Search API: given an input product id or input image finds all the products matching the styles that complements the user's query.

The API requires 1 of the following params: product ID or image (via url, file or image ID). Assuming that you have initialized the SDK according to section [Initialization](#2-initialization).

For image search with url, image id or file example, you can refer to [Search by image](#31-search-by-image)

Sample code for search:

```java
// search with image URL
String imageUrl = "https://some_website.com/some_image.jpg";
ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);

// search with product ID
ProductSearchByImageParams params = new ProductSearchByImageParams();
params.setPid("your-product-id");

// optional text query
params.setQ("your-text-query");


ProductSearch ps = SearchAPI.getProductSearchInstance();
ps.multisearchComplementary(params, new ProductSearch.ResultListener() {
    @Override
    public void onSearchResult(ProductResponse response, ErrorData error) {
        // LOGIC HERE
    }
});
```

### 3.6 Multisearch Outfit Recommendations

POST /v1/product/multisearch/outfit-recommendations

Multimodal Outfit Recommendations Search API: given an input product id or input image finds all the products matching the outfit in the user's query.

The API requires 1 of the following params: product ID or image (via url, file or image ID). Assuming that you have initialized the SDK according to section [Initialization](#2-initialization).

For image search with url, image id or file example, you can refer to [Search by image](#31-search-by-image)

Sample code for search:

```java
// search with image URL
String imageUrl = "https://some_website.com/some_image.jpg";
ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);

// search with product ID
ProductSearchByImageParams params = new ProductSearchByImageParams();
params.setPid("your-product-id");

// optional text query
params.setQ("your-text-query");

ProductSearch ps = SearchAPI.getProductSearchInstance();
ps.multisearchOutfitRec(params, new ProductSearch.ResultListener() {
    @Override
    public void onSearchResult(ProductResponse response, ErrorData error) {
        // LOGIC HERE
    }
});
```


## 4. Search Parameters

Depending on which API function you call, it requires their own parameters both of which is extends off the [BaseProductSearchParams](#41-baseproductsearchparams) class:

- [Search by Image](#31-search-by-image) uses [ProductSearchByImageParams](#42-productsearchbyimageparams)
- [Recommendations](#32-recommendations) uses [ProductSearchByIdParams](#43-productsearchbyidparams)
- [Multisearch](#33-multisearch) uses [ProductSearchByImageParams](#42-productsearchbyimageparams)
- [Multisearch autocomplete](#34-multisearch-autocomplete) uses [ProductSearchByImageParams](#42-productsearchbyimageparams)

### 4.1 BaseProductSearchParams

| Name | Type | Description |
|:---|:---|:---|
| page | Integer | Pagination - This indicate which page should the results be retrieved from. |
| limit | Integer | Pagination - This is the number of results that per page. |
| vaUid | String | Unique string that can identify the user/app, e.g. device serial number, advertising ID or a server generated string (reqid for first search) for analytics purposes. Will be autogenerated as a random string by ViSenze tracking SDK by default. |
| vaSid | String | Analytics session ID. |
| facets | List\<String>  |  |
| facetLimit | Integer |  |
| facetsShowCount | Boolean |  |
| sortBy | String | The product metadata field used to sort the results per page. Ascending `:asc` or descending `:desc` order needs to be specified after the field name. |
| groupBy | String | The product metadata field used to group the search results. Only non-numerical fields are supported. |
| groupLimit | Integer | The maximum number of groups displayed per page. The maximum value is 100. |
| score | Boolean | If the score of the detection should be returned in the response. |
| score_min | Float | Minimum score of any detected object requires to be considered. |
| scoreMax | Float | Maximum score of any detected object to be considered. |
| returnFieldsMapping | Boolean | Default to false, if set to true, will return catalog fields mappings. |
| attrsToGet | List\<String>  | List of product metadata fields to be retrieved in the results list. Product id, title, main image URL, product URL, price are retrieved by default.  |
| filters | Map\<String, String>  | The filter queries are treated as exact match conditions. Applies to String, Integer and Float type fields. Refer to [link](https://ref-docs.visenze.com/reference/filters-and-text-filters) for more details. |
| textFilters | Map\<String, String>  | The filter queries are treated as partial match filters. Only applies to String type fields. |
| customParams | Map\<String, String>  | Reserved for sending new future API parameters that are not yet supported by SDK |
| vaSdk | String | Analytics field. |
| vaSdkVersion | String | Analytics field. |
| vaOs | String | Analytics field. |
| vaOsv | String | Analytics field. |
| vaDeviceBrand | String | Analytics field. |
| vaDeviceModel | String | Analytics field. |
| vaAppBundleId | String | Analytics field. |
| vaAppName | String | Analytics field. |
| vaAppVersion | String | Analytics field. |
| dedup | Boolean | Analytics field. |
| dedupScoreThreshold | Double | Analytics field. |
| vaAaid | String | Analytics field. |
| vaDidmd5 | String | Analytics field. |
| vaN1 | Double | Analytics field. |
| vaN2 | Double | Analytics field. |
| vaS1 | String | Analytics field. |
| vaS2 | String | Analytics field. |

### 4.2 ProductSearchByImageParams

| Name | Type | Description |
|:---|:---|:---|
| imUrl | String | Image URL. |
| imId | String | Image ID (can be retrieved from a prior search response). |
| image | Image | Image file object (data). |
| box | int[] | Array of image-space coordinates of the detection box if only a specific part of the image is to be used. \[x1,y1,x2,y2\] format.  |
| detection | String |  |
| detectionLimit | Integer |  |
| detectionSensitivity | String |  |
| searchAllObjects | Boolean | Default to false. If set to true will return all objects (same as [ViSearch /discoversearch](./ViSearch.md)). |

### 4.3 ProductSearchByIdParams

| Name | Type | Description |
|:---|:---|:---|
| productId | String | The product's ID can be retrieved from a prior search response's [Product](#54-product). |


## 5. Search Results

As the SDK performs itssearches on a separate thread, you will receive the results of the search via a callback that you will need to provide as a parameter to the search function.

- The callback's signature:

```java
@Override
public void onSearchResult(ProductResponse response, ErrorData error) {
    // do your code here
}
```

> On a successful API call, the `ProductResponse` will be valid while `ErrorData` will be `null`, vice-versa on failure.

### 5.1 ProductResponse

| Name | Type | Description |
|:---|:---|:---|
| status | String | The request status, either `OK`, `warning`, or `fail` |
| imId | String | Image ID. Can be used to search again without reuploading |
| method | String |  |
| error | ErrorData | Error message and code if the request was not successful i.e. when status is `warning` or `fail` |
| productTypes | List\<ProductType> | Detected products' types, score and their bounding box in (x1,y1,x2,y2) format |
| products | List\<Product> | The list of products in the search results. Important fields for first release. If missing, it will be set to blank. Note that we are displaying customer’s original catalog fields in “data” field |
| objects | List\<ProductObject> | When the `searchAllObjects` parameter is set to `true` |
| catalogFieldsMapping | Map\<String, String> | Original catalog’s fields mapping |
| facets | List\<Facet> | List of facet fields value and response for filtering |
| page | int | The result page number |
| limit | int | The number of results per page |
| total | int | Total number of search result |
| strategy | Strategy | The recommendation strategy applied. |
| experiment | Experiment | the current A/B test experiment details |
| altLimit | int | The max number of alternatives (only applicable for Model Outfit algorithm) for each recommended product. |
| reqId | String | ID assigned to the request made |

### 5.2 ErrorData

| Name | Type | Description |
|:---|:---|:---|
| code | int | Error code, e.g. 401, 404 etc... |
| message | String | The server response message.  |

### 5.3 ProductType

| Name | Type | Description |
|:---|:---|:---|
| boxArray | int[] | The image-space coordinates of the detection box that represents the product. |
| type | String | The detected type of the product. |
| score | double | The detection's score of the product. |
| attributeList | Map |  |

### 5.4 Product

| Name | Type | Description |
|:---|:---|:---|
| productId | String | The product's ID which can be used in [recommendations](#32-recommendations). |
| imageUrl | String | Image URL. |
| data | Map\<String, Object> | This data field is slightly more complicated and deserves its own section over [here](#541-data). |
| score | Float | The detection score of the product. |
| s3Url | String | The product's image's S3 URL. |
| detect | String |  |
| keyword | String |  |
| boxArray | int[] | The image space coordinates of the detected object. |

#### 5.4.1 Data

To better explain what the `data` field is, take a look at the table below (database field_names):

|ViSenze pre-defined catalog fields|Client X's catalog original names|
|:---|:---|
|product_id|sku|
|main_image_url|medium_image|
|title|product_name|
|product_url|link|
|price|sale_price|
|brand|brand|

The table is a representation of how ViSenze's Catalog name its fields vs how Client X's database name its fields - both fields essentially mean the same thing just named differently.

> i.e. visenze_database["product_id"] == client_x_database["sku"]

You can find the schema mapping of ViSenze and the Client's in the `catalogFieldsMapping` variable found in [ProductResponse](#51-productresponse) - if the [ProductSearchByImageParams](#42-productsearchbyimageparams) have its `returnFieldsMapping` variable set to `true` when the search was called.

### 5.5 ProductObject

When using the `searchAllObjects` is set to `true`, the search response will return the results in a list of ProductObject instead of a list of Product directly. The difference is that ProductObject will split the products according to type.

| Name | Type | Description |
|:---|:---|:---|
| result | List\<Product> | The list of products belonging to this type. |
| total | Integer | The total number of results in this type. |

### 5.6 Facet

Facets are used to perform potential filtering of results.

| Name | Type | Description |
|:---|:---|:---|
| key | String |  |
| items | List\<FacetItem> |  |
| range | FacetRange |  |

### 5.7 FacetItem

Facet for distinct value filtering.

| Name | Type | Description |
|:---|:---|:---|
| value | String |  |
| count | Integer |  |

### 5.8 FacetRange

Facet for value range filtering.

| Name | Type | Description |
|:---|:---|:---|
| min | Number |  |
| max | Number |  |

### 5.9 AutoCompleteResponse

| Name | Type | Description |
|:---|:---|:---|
| status | String | The request status, either `OK` or `fail` |
| method | String |  |
| error | ErrorData | Error message and code if the request was not successful i.e. when status is `fail` |
| result | [AutoCompleteResultItem] (#591-autocompleteresultitem) |  |
| page | int | The result page number |
| limit | int | The number of results per page |
| total | int | Total number of search result |
| reqId | String | ID assigned to the request made |

#### 5.9.1 AutoCompleteResultItem

| Name | Type | Description |
|:---|:---|:---|
| text | String |  |
| score | double |  |

## 6. Search Examples

Here are a set of complex search examples that makes use of the other search parameters and how their response works. Lets start off by ensuring that you know what attributes/ fields are available to your app key (Attributes are set using the Dashboard when creating the App).

- By setting `returnFieldsMapping` to true, you will retrieve a map of field names that can be used for other advanced searches like filtering:

    ```java
    String imageUrl = "REPLACE WITH VALID URL";
    ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);
    params.setReturnFieldsMapping(true);

    // The productSearch variable is a class member variable assigned via
    // SearchAPI.getProductSearchInstance() in a previous function (constructor etc)
    productSearch.searchByImage(params, new ProductSearch.ResultListener() {
        @Override
        public void onSearchResult(ProductResponse response, ErrorData error) {
            // This mapping represents how ViSenze's fields are mapped to a Client's fields
            // Visenze Field -> Client Field
            Map<String, String> mappings = response.getCatalogFieldsMapping();

            // The data field will contain minimal information as we did not set any attributes to get
            for (Product p: response.getProducts()) {
                Map<String, Object> data = p.getData();
            }
        }
    });
    ```

- Once you know what attributes/fields you can use, we can try retrieving more data in the result. By default, there is not alot of information returned in the `data` field of the search results' products. However we can request for more information about the products by setting `attrsToGet`:

    ```java
    String imageUrl = "REPLACE WITH VALID URL";
    ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);
    params.setReturnFieldsMapping(true);

    // Tell the API to return the `merchant_category` as part of the product's `data` field.
    List<String> attributes = new ArrayList<String>();
    attributes.add("merchant_category");
    params.setAttrsToGet(attributes);

    // The productSearch variable is a class member variable assigned via
    // SearchAPI.getProductSearchInstance() in a previous function (constructor etc)
    productSearch.searchByImage(params, new ProductSearch.ResultListener() {
        @Override
        public void onSearchResult(ProductResponse response, ErrorData error) {
            Map<String, String> mappings = response.getCatalogFieldsMapping();

            // By setting attrsToGet, we can now find the `merchant_category` field in `data`.
            for (Product p: response.getProducts()) {
                Map<String, Object> data = p.getData();
            }
        }
    });
    ```

    > Remember to replace `"merchant_category"` to a field that is valid for you (found in the `catalogFieldsMapping`).

- After getting a hang of how attributes/fields work for you, we can further expand the search parameters to perform filterings as well:

    ```java
    String imageUrl = "REPLACE WITH VALID URL";
    ProductSearchByImageParams params = new ProductSearchByImageParams(imageUrl);
    params.setReturnFieldsMapping(true);

    // Tell the API to return the `merchant_category` as part of the product's `data` field.
    List<String> attributes = new ArrayList<String>();
    attributes.add("merchant_category");
    params.setAttrsToGet(attributes);

    // Tell the API to filter products that have the `merchant_category` == "Clothing/Tops/Blouse"
    Map<String, String> filters = new HashMap<String,String>();
    filters.put("merchant_category", "Clothing/Tops/Blouse");
    params.setFilters(filters);

    // The productSearch variable is a class member variable assigned via
    // SearchAPI.getProductSearchInstance() in a previous function (constructor etc)
    productSearch.searchByImage(params, new ProductSearch.ResultListener() {
        @Override
        public void onSearchResult(ProductResponse response, ErrorData error) {
            Map<String, String> mappings = response.getCatalogFieldsMapping();

            // By setting `attrsToGet`, we can now find the `merchant_category` field in `data`.
            // By setting `filters`, we can now see that all products belongs to the "Clothing/Tops/Blouse" category from `data["merchant_category"]`.
            for (Product p: response.getProducts()) {
                Map<String, Object> data = p.getData();
            }
        }
    });
    ```

    > Remember to replace `"merchant_category"` to a field that is valid for you (found in the `catalogFieldsMapping`).

    >Remember to replace `"Clothing/Tops/Blouse"` to a valid value depending on your fields.

With these advance examples, you should be able to start playing around with the other parameters!

## 7. Event Tracking

ViSearch Android SDK provides methods to track how your customer interacts with the search results.

In addition, to improve subsequent search quality, it is recommended to send user actions when they interact with the results.

### 7.1 Setup Tracking

You can initialize ViSenze Analytics tracker for sending analytics events as follow.

```java
ProductSearch productSearch = SearchAPI.getProductSearchInstance();
Tracker tracker = productSearch.newTracker();
```

For China, please change the tracker to:
```
Tracker tracker = productSearch.newTracker(true);
```

### 7.2 Send Events

Currently we support the following event actions: `click`, `view`, `product_click`, `product_view`, `add_to_cart`, and `transaction`. The `action` parameter can be an arbitrary string and custom events can be sent.

To send events, first retrieve the search query ID found in the search results listener:

```java
        public void onSearchResult(ProductResponse response, ErrorData error) {
            String queryId = response.getReqid();

	    // send event here
        }

```

Then, create the event using 1 of the helper methods Event.createXXXEvent(). For `product_click`, `product_view` events, queryId, pid, imgUrl and pos are all required.

```java
Event.createResultLoadEvent(String queryId, String pid) // send result_load event

Event.createProductClickEvent(String queryId, String pid, String imgUrl, int pos)

Event.createProductImpressionEvent(String queryId, String pid, String imgUrl, int pos)

Event.createAddCartEvent(String queryId, String pid, String imgUrl, int pos)

Event.createTransactionEvent(String queryId, String transactionId, double value)

// custom event with arbitray action
Event.createCustomEvent(String action)

```

Finally send the event via the tracker:

```java
tracker.sendEvent(event);
```

User action(s) can also be sent through an batch event handler.

A common use case for this batch event method is to group up all transaction by sending it in a batch. This SDK will automatically generate a transaction ID to group transactions as an order if the transaction ID is not provided.

```java
tracker.sendEvents(eventList);
```

Below are the brief description for various parameters:

Field | Description | Required
--- | --- | ---
queryId| The request id of the search request. This reqid can be obtained from all the search result:```resultList.getReqid()``` | Yes
action | Event action. Currently we support the following event actions: `click`, `view`, `product_click`, `product_view`, `add_to_cart`, and `transaction`. | Yes
pid | Product ID ( generally this is the `im_name`) for this product. Can be retrieved via `ImageResult.getImageName()` | Required for product view, product click and add to cart events
imgUrl | Image URL ( generally this is the `im_url`) for this product. Can be retrieved via `ImageResult.getImageUrl()` | Required for product view, product click and add to cart events
pos | Position of the product in Search Results e.g. click position/ view position. Note that this start from 1 , not 0. | Required for product view, product click and add to cart events
transactionId | Transaction ID | Required for transaction event.
value | Transaction value e.g. order value | Required for transaction event.
uid | Unique user/device ID. If not provided, a random (non-personalizable) UUID will be generated to track the device. | No
category | A generic string to categorize / group the events in related user flow. For example: `privacy_flow`, `videos`, `search_results`. Typically, categories are used to group related UI elements. Max length: 32 | No
name | Event name e.g. `open_app` , `click_on_camera_btn`. Max length: 32. | No
label | label for main interaction object such as product title, page title. This together with `action` can be used to decide whether an event is unique e.g. if user clicks on same product twice, only 1 unique click . Max length: 32. | No
fromReqId | Generic request ID field to specify which request leads to this event e.g. click request ID that leads to the purchase. The chain can be like this queryId → clickId → purchase. Max length: 32. | No
source | Segment the traffic by tagging them e.g. from camera, from desktop. Max length: 32. | No
brand | Product brand. Max length: 64. | No
price | Product price. Numeric field, if provided must be >=0 and is a valid number. | No
currency | ISO 3 characters code e.g. “USD”. Will be validated if provided. | No
productUrl| Product URL. Max length: 512 | No
campaign | Advertising campaign. Max length : 64. | No
campaignAdGroup | Ad group name (only relevant for campaign) | No
campaignCreative | Creative name (only relevant for campaign) | No
n1 | Custom numeric parameter. | No
n2 | Custom numeric parameter. | No
n3 | Custom numeric parameter. | No
n4 | Custom numeric parameter. | No
n5 | Custom numeric parameter. | No
s1 | Custom string parameter. Max length: 64. | No
s2 | Custom string parameter. Max length: 64. | No
s3 | Custom string parameter. Max length: 64. | No
s4 | Custom string parameter. Max length: 64. | No
s5 | Custom string parameter. Max length: 64. | No
