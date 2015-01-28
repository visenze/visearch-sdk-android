package com.visenze.visearch.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.visenze.visearch.android.BaseSearchParams;
import com.visenze.visearch.android.ColorSearchParams;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.Image;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.demo.view.ResultView;
import com.visenze.visearch.demo.view.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity implements ViSearch.ResultListener{
    //Activity Code
    private static final int RESULT_LOAD_IMAGE = 0x00;

    //Please change to your access key / secret Key pair
    private static final String ACCESS_KEY = "your_access_key";
    private static final String SECRET_KEY = "your_secret_key";

    //ViSearcher instance
    private ViSearch viSearch;

    //setup UI
    private SearchView searchView;
    private ResultView resultView;
    private DrawerLayout drawerLayout;

    //Search Parameters
    private BaseSearchParams baseSearchParams;
    private IdSearchParams idSearchParams;
    private ColorSearchParams colorSearchParams;
    private UploadSearchParams uploadSearchParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //get ViSearcher instance
        viSearch = new ViSearch.Builder(ACCESS_KEY, SECRET_KEY).build(this);
        viSearch.setListener(this);

        //init search params
        idSearchParams = new IdSearchParams();
        colorSearchParams = new ColorSearchParams();
        uploadSearchParams = new UploadSearchParams();

        //setup UI and implement click method
        setUpUI();
    }

    private void setUpUI() {
        searchView = (SearchView)findViewById(R.id.query_view);
        resultView = (ResultView)findViewById(R.id.result_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //implement id search button
        searchView.getIdSearchButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.getIndexText() != null) {

                    //start result ui
                    drawerLayout.openDrawer(resultView);
                    resultView.startSearchResult();

                    //start id search
                    startIdSearch(searchView.getIndexText());
                }
            }
        });

        //implement color search button
        searchView.getColorSearchButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.getColorText() != null) {

                    //start result ui
                    drawerLayout.openDrawer(resultView);
                    resultView.startSearchResult();

                    //start color search
                    startColorSearch(searchView.getColorText());
                }
            }
        });

        //implement upload search image loader and search button
        searchView.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGallery();
            }
        });

        searchView.getUploadSearchButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.getUri() != null) {

                    //start result ui
                    resultView.startSearchResult();
                    drawerLayout.openDrawer(resultView);

                    //start upload search
                    startUploadSearch(searchView.getUri());
                }
            }
        });
    }

    //set parameters and start id search
    private void startIdSearch(String imageName) {
        //get base params
        getBaseSearchParams();
        idSearchParams.setBaseSearchParams(baseSearchParams);

        //Start index search with index search parameters
        idSearchParams.setImageName(imageName);

        //call search method
        viSearch.idSearch(idSearchParams);
    }

    //set parameters and start color search
    private void startColorSearch(String colorCode) {
        //get base params
        getBaseSearchParams();
        colorSearchParams.setBaseSearchParams(baseSearchParams);

        //start color search with color search parameters
        colorSearchParams.setColor(colorCode);

        //call search method
        viSearch.colorSearch(colorSearchParams);
    }

    //set parameters and start upload search
    private void startUploadSearch(Uri uri) {
        //get base params
        getBaseSearchParams();
        uploadSearchParams.setBaseSearchParams(baseSearchParams);

        //init image
        Image image = new Image(this, uri, Image.ResizeSettings.STANDARD);
        uploadSearchParams.setImage(image);

        //call search method
        viSearch.uploadSearch(uploadSearchParams);
    }

    //Get basic search parameters from user input and setup default parameters
    private void getBaseSearchParams() {
        //reset base search params
        if (baseSearchParams != null) {
            baseSearchParams = null;
        }
        baseSearchParams = new BaseSearchParams();

        //set page number if there is an input
        if(searchView.getPageNumber() != null) {
            baseSearchParams.setPage(Integer.parseInt(searchView.getPageNumber()));
        }

        //set limit if there is an input
        if(searchView.getLimit() != null){
            baseSearchParams.setLimit(Integer.parseInt(searchView.getLimit()));
        }

        //set score request if request
        baseSearchParams.setScore(searchView.getScoreChecked());

        //set "im_url" in field list, so that the value of "im_url" field will be return as search result for each image
        if (searchView.getUrlChecked()) {
            List<String> fl = new ArrayList<String>();
            fl.add("im_url");
            baseSearchParams.setFl(fl);
        }
    }

    //open photo gallery for result
    private void startGallery() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        startActivityForResult(openAlbumIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri uri = data.getData();
            searchView.loadAndDisplayImage(uri, this);
        }
    }

    @Override
    public void onSearchResult(ResultList resultList) {

        if(resultList != null && resultList.getImageList().size() > 0) {
            resultView.updateResult(this, resultList.getImageList());

        } else {
            resultView.displayError(getResources().getString(R.string.empty_list));
        }
    }

    @Override
    public void onSearchError(String error) {
        resultView.displayError(error);
    }

    @Override
    public void onSearchCanceled() {

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(resultView))
            drawerLayout.closeDrawer(resultView);
        else
            super.onBackPressed();
    }
}
