ViSearch Android SDK User Guide
----

[![Build Status](https://api.travis-ci.org/jasonpeng/visearch-sdk-android.svg?branch=develop)](https://travis-ci.org/jasonpeng/visearch-sdk-android)

##1. Overview
ViSearch is an API that provides accurate, reliable and scalable image search. ViSearch API provides two services ( Data API and Search API) to let the developers prepare image database and perform image searches efficiently. ViSearch API can be easily integrated into your web and mobile applications. More details about ViSearch API can be found in the [documentation](http://www.visenze.com/docs/overview/introduction).

The ViSearch Androi SDK is an open source software for easy integartion of ViSearch Search API with your Android mobile applications. It provides three search methods based on the ViSearch Search API - pre-indexed search, color search and upload search. For source code and references, visit the github [repository](https://github.com/visenze/visearch-sdk-android).

Current stable version: 1.0.1

##2. Setup
You can get the soruce code of the SDK and demos from the [github repo](https://github.com/visenze/visearch-sdk-android). You can import it as a new project using Android Studio (Tested environment: Android Studio 1.0.2)

###Install the SDK
For a quick start, you may include the SDK into your own project using gradle:

```
compile 'com.visenze:visearch-android:1.0.1'
```

In the build.gradle file under your app module, add the packaging options to ensure a successful compilation:

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



###Add user permissions
ViSearch Android SDK needs these user permissions to work. Add the following declarations to the `AndroidManifest.xml` file.  Network permission allows the app to connect to network services, while write/read to external storage permissions allow the app to load and save images on the device.

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
`ViSearch` must be initialized with an accessKey/secretKey pair before it can be used. In order for it to be notified of the search result, `ViSearch.ResultListener` must be implemented and set using `setListener`.

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

###Pre-indexed Search
Pre-index search is to search similar images based on the your indexed image by its unique identifier (im_name). It  should be a valid ID that is used to index your images in the database.

```java
IdSearchParams idSearchParams = new IdSearchParams("dress_1");
viSearch.idSearch(idSearchParams);
```

###Color Search
Color search is to search images with similar color by providing a color code. The color code should be in Hexadecimal and passed to `ColorSearchParams` as a `String`.

```java
ColorSearchParams colorSearchParams = new ColorSearchParams("9b351b");
viSearch.colorSearch(colorSearchParams);
``` 

###Upload Search
Upload search is used to search similar images by uploading an image or providing an image url. `Image` class is used to perform the image encoding and resizing. You should construct the `Image` object and pass it to `UploadSearchParams` to start a search.

Using an image from a local file path:
```java
Image image = new Image("/local/path/to/image.jpg");
UploadSearchParams uploadSearchParams.setImage(image);

viSearch.uploadSearch(uploadSearchParams);
```

Or using an image by providing the Uri of the image in photo gallery:
```java
Image image = new Image(context, uri);
UploadSearchParams uploadSearchParams = new UploadSearchParams(image);

viSearcher.uploadSearch(uploadSearchParams);
```

Or construct the `image` from the byte array return by the camera preview callback:
```java
@Override
public void onPictureTaken(byte[] bytes, Camera camera) {
    Image image = new Image(bytes);
    UploadSearchParams uploadSearchParams = new UploadSearchParams(image);
    
    viSearcher.uploadSearch(uploadSearchParams);
}
```

Alternatively, you can pass an image url directly to `uploadSearchParams` to start the search. :
```java
String url = "http://mydomain.com/sample_image.jpg";
UploadSearchParams uploadSearchParams = new UploadSearchParams(url);
viSearcher.uploadSearch(uploadSearchParams);
```

####Selection Box
If the object you wish to search for takes up only a small portion of your image, or other irrelevant objects exists in the same image, chances are the search result could become inaccurate. Use the Box parameter to refine the search area of the image to improve accuracy. Noted that the box coordinated is set with respect to the original size of the image passed:

```java
Image image = new Image(this, uri);
// create the box to refine the area on the searching image
// Box(x1, y1, x2, y2) where (0,0) is the top-left corner
// of the image, (x1, y1) is the top-left corner of the box,
// and (x2, y2) is the bottom-right corner of the box.
image.setBox(0, 0, 400, 400);
```
####Resizing Settings
When performing upload search, you may notice the increased search latency with increased image file size. This is due to the increased time spent in network transferring your images to the ViSearch server, and the increased time for processing larger image files in ViSearch. 

To reduce upload search latency, by default the uploadSearch method makes a copy of your image file and resizes the copy to 512x512 pixels if both of the original dimensions exceed 512 pixels. This is the optimized size to lower search latency while not sacrificing search accuracy for general use cases:
```java
//default resize setting, set the image size to 512 x 512 with jpeg 85 quality
Image image = new Image(imagePath, ResizeSettings.STANDARD);
```

If your image contains fine details such as textile patterns and textures, you can use a higer quality image for search to get better search result:
```java
//for images with fine details, use HIGH resize settings 1024 x 1024 and jpeg 90 quality
Image image = new Image(imagePath, ResizeSettings.HIGH);
```

Or, provide the customized resize settings. To make efficient use the of the memory and network bandwidth of mobile device, the maximum size is set at 1024 x 1024. Any image exceeds the limit will be resized to the limit:
```java
//resize the image to 800 by 800 area using jpeg 80 quality
Image image = new Image(imagePath, new ResizeSettings(800, 800, 80));
```


##5. Search Results
The search result is returned as a list of image names with required additional information. Use `getImageList()` to get the list of images. The basic information returned about the image are image name(use `getImageName()` to obtain). Use`viSearch.cancelSearch()` to cancel a search, and handle the result by implementing the `onSearchCanceled()` callback. If error occurs during the search, an error message will be returned in `viSearch.onSearchError(String error)`. 

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

You can tune the search result by configuring the basic search parameters `BaseSearchParams`. As the result is returned in a format of a list of images page by page, use `setLimit` to set the number of results per page, `setPage` to indicate the page number:



```java
BaseSearchParams baseSearchParams = new BaseSearchParams();
baseSearchParams.setLimit(20);
baseSearchParams.setPage(1);
ColorSearchParams colorSearchParams = new ColorSearchParams("3322ff");
colorSearchParams.setBaseSearchParams(baseSearchParams);
visearcher.colorSearch(colorSearchParams);
```

##6. Advanced Search Parameters

###Configure FL to get the metadata of the data feed
To retrieve metadata of your search results, provide a list of metadata keys as the `fl` (field list) in the basic search parameters:

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

Only metadata of type string, int, and float can be retrieved from ViSearch:
| Type | FL|
-|-
|String | yes|
|text|no|
|int|yes|
|float|yes|

###Configure FQ to filter the search result based on metadata of the data feed
To filter search results based on metadata values, provide a map of metadata key to filter value as the `fq` (filter query) parameter:

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
|Type|FQ|
|-|-|
|String|Metadata value must be exactly matched with the query value, e.g. "Vintage Wingtips" would not match "vintage wingtips" or "vintage"|
|text|Metadata value will be indexed using full-text-search engine and supports fuzzy text matching, e.g. "A pair of high quality leather wingtips" would match any word in the phrase|
|int|Metadata value can be either: 1) exactly matched with the query value 2) matched with a ranged query minValue,maxValue, e.g. int value 1, 99, and 199 would match ranged query 0,199 but would not match ranged query 200,300|
|float|Metadata value can be either 1) exactly matched with the query value 2) matched with a ranged query minValue,maxValue, e.g. float value 1.0, 99.99, and 199.99 would match ranged query 0.0,199.99 but would not match ranged query 200.0,300.0|

###Use score parameter as threshold to filter search result
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
Source code of the SDK and Demo samples can be found in this [Github Repo](https://github.com/visenze/visearch-sdk-android)
