# ViSearch Android SDK
[![Build Status](https://api.travis-ci.org/visenze/visearch-sdk-android.svg?branch=master)](https://travis-ci.org/visenze/visearch-sdk-android)

---

##Table of Contents
 1. [Overview](#1-overview)
 2. [Setup](#2-setup)
 	  - 2.1 [Install the SDK](#21-install-the-sdk)
 	  - 2.2 [Add User Permissions](#22-add-user-permissions)
 3. [Initialization](#3-initialization)
 4. [Searching Images](#4-searching-images)
	  - 4.1 [Pre-indexed Search](#41-pre-indexed-search)
	  - 4.2 [Color Search](#42-color-search)
	  - 4.3 [Upload Search](#43-upload-search)
	    - 4.3.1 [Selection Box](#431-selection-box)
	    - 4.3.2 [Resizing Settings](#432-resizing-settings)
 5. [Search Results](#5-search-results)
 6. [Advanced Search Parameters](#6-advanced-search-parameters)
	  - 6.1 [Retrieving Metadata](#61-retrieving-metadata)
	  - 6.2 [Filtering Results](#62-filtering-results)
	  - 6.3 [Result Score](#63-result-score)
 7. [Code Samples](#7-code-samples)

---


##1. Overview
ViSearch is an API that provides accurate, reliable and scalable image search. ViSearch API provides two services ( Data API and Search API) to let the developers prepare image database and perform image searches efficiently. ViSearch API can be easily integrated into your web and mobile applications. For more details, see [ViSearch API Documentation](http://www.visenze.com/docs/overview/introduction).

The ViSearch Androi SDK is an open source software to provide easy integration of ViSearch Search API with your Android mobile applications. It provides three search methods based on the ViSearch Search API - pre-indexed search, color search and upload search. For source code and references, please visit the [Github Repository](https://github.com/visenze/visearch-sdk-android).

>Current stable version: 1.0.5

>Minimum Android SDK Version: API 9, Android 2.3


##2. Setup

###2.1 Install the SDK
You can include the dependency in your project using gradle:

```
compile 'com.visenze:visearch-android:1.0.5'
```

In the `build.gradle` file under your app module, add the packaging options to ensure a successful compilation:

```
android {
	...
	
    packagingOptions {
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
    }
    ...
}
```

If you want to use the packaged Jars directly in your project, please find all the dependencies in the directory `/dependency`

###2.2 Add User Permissions
ViSearch Android SDK needs these user permissions to work. Add the following declarations to the `AndroidManifest.xml` file.  Network permission allows the app to connect to network services. Write/read to external storage permissions allow the app to load and save images on the device.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.visenze.android.visenze_demo_test">

	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	<uses-permission android:name="android.permission.INTERNET"/>
	<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
	<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

	<application>
	...
	</application>
</manifest>
```

##3. Initialization
`ViSearch` must be initialized with an accessKey/secretKey pair before it can be used. In order for it to be notified of the search result, `ViSearch.ResultListener` must be implemented. Call `viSearch.setListener` to set the listener.

```java

public class MyActivity extends Activity implements ViSearch.ResultListener{
    //Please change to your access key / secret Key pair
    private static final String ACCESS_KEY = "your_access_key";
    private static final String SECRET_KEY = "your_secret_key";
	...

	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		ViSearch viSearch = new ViSearch.Builder(ACCESS_KEY, SECRET_KEY).build(this);
		viSearch.setListener(this);
		
		...
	}
	...
}
```

##4. Searching Images

###4.1 Pre-indexed Search
Search similar images based on the your indexed image by its unique identifier (im_name):

```java
IdSearchParams idSearchParams = new IdSearchParams("dress_1");
viSearch.idSearch(idSearchParams);
```

###4.2 Color Search
Color search is to search images with similar color by providing a color code. The color code should be in Hexadecimal and passed to `ColorSearchParams` as a `String`.

```java
ColorSearchParams colorSearchParams = new ColorSearchParams("9b351b");
viSearch.colorSearch(colorSearchParams);
``` 

###4.3 Upload Search
Upload search is used to search similar images by uploading an image or providing an image url. `Image` class is used to perform the image encoding and resizing. You should construct the `Image` object and pass it to `UploadSearchParams` to start a search.

* Using an image from a local file path:
```java
Image image = new Image("/local/path/to/image.jpg");
UploadSearchParams uploadSearchParams.setImage(image);

viSearch.uploadSearch(uploadSearchParams);
```

* Using an image by providing the Uri of the image in photo gallery:
```java
Image image = new Image(context, uri);
UploadSearchParams uploadSearchParams = new UploadSearchParams(image);

viSearcher.uploadSearch(uploadSearchParams);
```

* Construct the `image` from the byte array returned by the camera preview callback:
```java
@Override
public void onPictureTaken(byte[] bytes, Camera camera) {
    Image image = new Image(bytes);
    UploadSearchParams uploadSearchParams = new UploadSearchParams(image);
    
    viSearcher.uploadSearch(uploadSearchParams);
}
```

* Alternatively, you can pass an image url directly to `uploadSearchParams` to start the search :
```java
String url = "http://mydomain.com/sample_image.jpg";
UploadSearchParams uploadSearchParams = new UploadSearchParams(url);
viSearcher.uploadSearch(uploadSearchParams);
```

####4.3.1 Selection Box
If the object you wish to search for takes up only a small portion of your image, or other irrelevant objects exists in the same image, chances are the search result could become inaccurate. Use the Box parameter to refine the search area of the image to improve accuracy. The box coordinated is set with respect to the original size of the uploading image:

```java
Image image = new Image(this, uri);
// create the box to refine the area on the searching image
// Box(x1, y1, x2, y2) where (0,0) is the top-left corner
// of the image, (x1, y1) is the top-left corner of the box,
// and (x2, y2) is the bottom-right corner of the box.
image.setBox(0, 0, 400, 400);
```

####4.3.2 Resizing Settings
When performing upload search, you may notice the increased search latency with increased image file size. This is due to the increased time spent in network transferring your images to the ViSearch server, and the increased time for processing larger image files in ViSearch. 

To reduce upload search latency, by default the uploadSearch method makes a copy of your image file and resizes the copy to 512x512 pixels if both of the original dimensions exceed 512 pixels. This is the optimized size to lower search latency while not sacrificing search accuracy for general use cases:

```java
//default resize setting, set the image size to 512 x 512
Image image = new Image(imagePath, ResizeSettings.STANDARD);
```

If your image contains fine details such as textile patterns and textures, you can use an image with larger size for search to get better search result:

```java
//for images with fine details, use HIGH resize settings 1024 x 1024
Image image = new Image(imagePath, ResizeSettings.HIGH);
```

Or, provide the customized resize settings. To make efficient use the of the memory and network bandwidth of mobile device, the maximum size is set at 1024 x 1024. Any image exceeds the limit will be resized to the limit:

```java
//resize the image to 800 by 800 area using jpeg 80 quality
Image image = new Image(imagePath, new ResizeSettings(800, 800, 80));
```


##5. Search Results
The search results are returned as a list of image names with required additional information. Use `getImageList()` to get the list of images. The basic information returned about the image are image name. Use`viSearch.cancelSearch()` to cancel a search, and handle the result by implementing the `onSearchCanceled()` callback. If error occurs during the search, an error message will be returned and can be handled in `viSearch.onSearchError(String error)` callback method. 

```java
@Override
public void onSearchResult(ResultList resultList) {
	for (ImageResult imageResult : resultList.getImageList()) {
		//Do something with each result image
		...
	}
}

@Override
public void onSearchError(String error) {
    resultView.displayError(error);
}


@Override
public void onSearchCanceled() {

}
```

 You can provide pagination parameters to control the paging of the image search results. by configuring the basic search parameters `BaseSearchParams`. As the result is returned in a format of a list of images page by page, use `setLimit` to set the number of results per page, `setPage` to indicate the page number:

| Name | Type | Description |
| ---- | ---- | ----------- |
| page | Integer | Optional parameter to specify the page of results. The first page of result is 1. Defaults to 1. |
| limit | Integer | Optional parameter to specify the result per page limit. Defaults to 10. |

```java
BaseSearchParams baseSearchParams = new BaseSearchParams();
baseSearchParams.setLimit(20);
baseSearchParams.setPage(1);
ColorSearchParams colorSearchParams = new ColorSearchParams("3322ff");
colorSearchParams.setBaseSearchParams(baseSearchParams);
visearcher.colorSearch(colorSearchParams);
```

##6. Advanced Search Parameters

###6.1 Retrieving Metadata
To retrieve metadata of your search results, provide a list of metadata keys as the `fl` (field list) in the basic search property:

```java
BaseSearchParams baseSearchParams = new BaseSearchParams();
List<String> fl = new ArrayList<>();
fl.add("price");
fl.add("brand");
baseSearchParams.setFl(fl);

UploadSearchParams uploadSearchParams = new UploadSearchParams(new Image("/path/to/image"));
uploadSearchParams.setBaseSearchParams(baseSearchParams);
```

In result callback you can read the metadata:
```java
@Override
public void onSearchResult(ResultList resultList) {
	for (ImageResult imageResult : resultList.getImageList()) {
		Map<String, String> metaData = imageResult.getMetaData();
		//Do something with the metadata
		...
	}
}
```

>Only metadata of type string, int, and float can be retrieved from ViSearch. Metadata of type text is not available for retrieval.

###6.2 Filtering Results
To filter search results based on metadata values, provide a map of metadata key to filter value as the `fq` (filter query) property:

```java
// add fq param to specify the filtering criteria
Map<String, String> fq = new HashMap<String, String>();
// description is metadata type text
fq.put("description", "wingtips");
// price is metadata type float
fq.put("price", "0,199");
baseSearchParams.setFq(fq);
```

Querying syntax for each metadata type is listed in the following table:

Type | FQ
--- | ---
string | Metadata value must be exactly matched with the query value, e.g. "Vintage Wingtips" would not match "vintage wingtips" or "vintage"
text | Metadata value will be indexed using full-text-search engine and supports fuzzy text matching, e.g. "A pair of high quality leather wingtips" would match any word in the phrase
int | Metadata value can be either: <ul><li>exactly matched with the query value</li><li>matched with a ranged query ```minValue,maxValue```, e.g. int value ```1, 99```, and ```199``` would match ranged query ```0,199``` but would not match ranged query ```200,300```</li></ul>
float | Metadata value can be either <ul><li>exactly matched with the query value</li><li>matched with a ranged query ```minValue,maxValue```, e.g. float value ```1.0, 99.99```, and ```199.99``` would match ranged query ```0.0,199.99``` but would not match ranged query ```200.0,300.0```</li></ul>


###6.3 Result Score
ViSearch image search results are ranked in descending order i.e. from the highest scores to the lowest, ranging from 1.0 to 0.0. By default, the score for each result is not returned. You can turn on the score parameter to retrieve the scores for each image result:

```java
// return scores for each image result, default is false
baseSearchParams.setScore(true);

IdSearchParams idSearchParams = new IdSearchParams("dress_1");
idSearchParams.setBaseSearchParams(baseSearchParams);
visearch.idSearch(idSearchParams);

...
@Override
public void onSearchResult(ResultList resultList) {
	for (ImageResult imageResult : resultList.getImageList()) {
		float score = imageResult.getScore();
		//Do something with the score
		...
	}
}
```

If you need to restrict search results from a minimum score to a maximum score, specify the score_min and/or score_max parameters:
```java
//set the threshold value
baseSearchParams.setScoreMin(0.5);
baseSearchParams.setScoreMax(0.8);

// only retrieve search results with scores between 0.5 and 0.8
IdSearchParams idSearchParams = new IdSearchParams("dress_1");
idSearchParams.setBaseSearchParams(baseSearchParams);
visearch.idSearch(idSearchParams);
```


##7. Code Samples
Source code of a demo application can be found [here](https://github.com/visenze/visearch-sdk-android/tree/master/demo).
