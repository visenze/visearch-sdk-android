/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ViSenze Pte. Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.visenze.visearch.camerademo.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.Box;
import com.visenze.visearch.android.model.Image;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.android.model.ProductType;
import com.visenze.visearch.camerademo.CameraActivity;
import com.visenze.visearch.camerademo.DetailActivity;
import com.visenze.visearch.camerademo.EditPhotoActivity;
import com.visenze.visearch.camerademo.MainActivity;
import com.visenze.visearch.camerademo.R;
import com.visenze.visearch.camerademo.Views.ScrollAwareGridView;
import com.visenze.visearch.camerademo.Views.adapter.HorizontalProductTypeArrayAdapter;
import com.visenze.visearch.camerademo.Views.adapter.SquareImageAdapter;
import com.visenze.visearch.camerademo.Views.adapter.StrechImageAdapter;
import com.visenze.visearch.camerademo.http.SearchAPI;
import com.visenze.visearch.camerademo.util.Config;
import com.visenze.visearch.camerademo.util.DataHelper;
import com.visenze.visearch.camerademo.util.ImageHelper;
import com.visenze.visearch.camerademo.util.IntentHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.HListView;
import me.littlecheesecake.croplayout.EditPhotoView;
import me.littlecheesecake.croplayout.EditableImage;
import me.littlecheesecake.croplayout.handler.OnBoxChangedListener;
import me.littlecheesecake.croplayout.model.ScalableBox;
import me.littlecheesecake.waterfalllayoutview.MultiColumnListView;
import me.littlecheesecake.waterfalllayoutview.WFAdapterView;

/**
 * Created by yulu on 2/17/15.
 * 
 * PhotoEditFragment 
 */
public class PhotoEditFragment extends Fragment implements ViSearch.ResultListener{
    private static final String PHOTO_EDIT_ACTIVITY = "PhotoEditActivity";

    private enum VIEW_LAYOUT {
        GRID, WATERFALL
    };

    //UI element
    @InjectView(R.id.result_loading)            ImageView               loadingImage;
    @InjectView(R.id.result_query_image)        ImageView               queryImage;
    @InjectViews({R.id.result_grid_view})       List<View>              photoUIs;
    @InjectView(R.id.photoedit_image_view)      EditPhotoView           editPhotoView;
    @InjectView(R.id.photoedit_rotate_button)   TextView                rotateButton;
    @InjectView(R.id.result_grid_view)          FrameLayout             resultGridView;
    @InjectView(R.id.category_list_view)        HListView               categoryListView;
    @InjectView(R.id.sliding_layout)            SlidingUpPanelLayout    slidingUpPanelLayout;
    @InjectView(R.id.result_switch_button)      ImageView               switchButtonView;

    //Dynamic UI elements
    private HorizontalProductTypeArrayAdapter   horizontalAdapter;
    private MultiColumnListView                 waterfallViewLayout;
    private ScrollAwareGridView                 gridViewLayout;
    private VIEW_LAYOUT                         currentLayout;

    //parameters passed in from camera activity
    private String                      imagePath;
    private String                      selectedType;
    private Bitmap                      bitmap;
    private List<String>                productList;
    private ResultList                  resultList;

    //ViSearch and Search parameters
    private ViSearch                    viSearch;
    private EditableImage               editableImage;

    /**
     * Constructor: get new instance of PhotoEditFragment
     *  
     * @return new instance of PhotoEditFragment
     */
    public static PhotoEditFragment newInstance() {
        return new PhotoEditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.result_edit_main, container, false);
        ButterKnife.inject(this, view);

        //get viSearch instance
        try {
            viSearch = SearchAPI.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        viSearch.setListener(this);

        //set up data
        imagePath = ((EditPhotoActivity)getActivity()).getImagePath();
        resultList = ((EditPhotoActivity)getActivity()).getResultList();

        List<ProductType> cachedProductList = DataHelper.copyProductTypeList(resultList.getProductTypes());
        selectedType = DataHelper.getSelectedProductType(cachedProductList).getType();
        productList = DataHelper.getSupportedTypeList(resultList.getSupportedProductTypeList(), selectedType);
        Box box = cachedProductList.get(0).getBox();

        //set up UI
        horizontalAdapter = new HorizontalProductTypeArrayAdapter(getActivity(), productList);
        horizontalAdapter.setSelected(productList.indexOf(selectedType));
        categoryListView.scrollTo(productList.indexOf(selectedType), 0);

        categoryListView.setAdapter(horizontalAdapter);
        categoryListView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> adapterView, View view, int i, long l) {
                horizontalAdapter.setSelected(i);

                Image image = new Image(imagePath, Config.IMAGE_QUALITY);
                ScalableBox b = editableImage.getBox();
                image.setBox(b.getX1(), b.getY1(), b.getX2(), b.getY2());

                //set search parameters
                UploadSearchParams uploadSearchParams = new UploadSearchParams(image);

                //set detection
                selectedType = productList.get(i);
                DataHelper.setSearchParams(uploadSearchParams, selectedType);

                viSearch.cancelSearch();
                viSearch.uploadSearch(uploadSearchParams);
                changeUploadUI();
            }
        });

        editableImage = new EditableImage(imagePath);
        editableImage.setBox(getDetectionBox(box));
        editPhotoView.initView(getActivity(), editableImage);
        editPhotoView.setOnBoxChangedListener(new OnBoxChangedListener() {
            @Override
            public void onChanged(int x1, int y1, int x2, int y2) {
                Image image = new Image(imagePath, Config.IMAGE_QUALITY);
                image.setBox(x1, y1, x2, y2);

                //set search parameters
                UploadSearchParams uploadSearchParams = new UploadSearchParams(image);
                //set detection
                DataHelper.setSearchParams(uploadSearchParams, selectedType);

                viSearch.cancelSearch();
                viSearch.uploadSearch(uploadSearchParams);
                changeUploadUI();
            }
        });

        //set up result view
        switchView();

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        return view;
    }

    @Override
    public void onSearchResult(ResultList resultList) {
        Log.d(PHOTO_EDIT_ACTIVITY, "upload ok");
        this.resultList = resultList;

        changeUploadUIBack();
        if (currentLayout == VIEW_LAYOUT.GRID) {
            gridViewLayout.setAdapter(new SquareImageAdapter(getActivity(), resultList.getImageList()));
            gridViewLayout.invalidate();
        } else if (currentLayout == VIEW_LAYOUT.WATERFALL) {
            waterfallViewLayout.setAdapter(new StrechImageAdapter(getActivity(), resultList.getImageList()));
            waterfallViewLayout.invalidate();
        }

        horizontalAdapter.setSelected(productList.indexOf(selectedType));
        categoryListView.scrollTo(productList.indexOf(selectedType), 0);
    }

    @Override
    public void onSearchError(String errorMessage) {
        Log.d(PHOTO_EDIT_ACTIVITY, "upload error: " + errorMessage);
        if (isAdded()) {
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            changeUploadUIBack();
        }
    }

    @Override
    public void onSearchCanceled() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //get data from intent
        imagePath = ((EditPhotoActivity)getActivity()).getImagePath();
        String thumbnailPath = ((EditPhotoActivity) getActivity()).getThumbnailPath();

        if (thumbnailPath != null) {
            bitmap = ImageHelper.getBitmapFromPath(thumbnailPath);
            queryImage.setImageBitmap(bitmap);
        }
        viSearch.setListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bitmap != null)
            bitmap.recycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);

        if (editableImage.getOriginalImage() != null)
            editableImage.getOriginalImage().recycle();
    }

    @OnClick(R.id.photoedit_rotate_button)
    public void rotateImage() {
        editPhotoView.rotateImageView();
        editableImage.saveEditedImage(imagePath);
        changeUploadUI();

        Image image = new Image(imagePath, Config.IMAGE_QUALITY);

        //set search parameters
        UploadSearchParams uploadSearchParams = new UploadSearchParams(image);
        //set detection
        DataHelper.setSearchParams(uploadSearchParams, selectedType);

        viSearch.cancelSearch();
        viSearch.uploadSearch(uploadSearchParams);
    }

    @OnClick(R.id.result_home_button)
    public void returnHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.result_back_button)
    public void closeClicked() {
        viSearch.cancelSearch();
        getActivity().finish();
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.result_query_image)
    public void showCrop() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @OnClick(R.id.result_switch_button)
    public void switchView() {
        if (currentLayout == VIEW_LAYOUT.GRID) {
            currentLayout = VIEW_LAYOUT.WATERFALL;
            switchButtonView.setSelected(true);

            if (waterfallViewLayout == null) {
                waterfallViewLayout = new MultiColumnListView(getActivity());
            }

            waterfallViewLayout.setAdapter(new StrechImageAdapter(getActivity(), resultList.getImageList()));
            waterfallViewLayout.setOnItemClickListener(new WFAdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(WFAdapterView<?> parent, View view, int position, long id) {
                    startDetailActivity(resultList.getImageList().get(position));
                }
            });
            waterfallViewLayout.invalidate();
            resultGridView.removeAllViews();
            resultGridView.addView(waterfallViewLayout);
        } else {
            currentLayout = VIEW_LAYOUT.GRID;
            switchButtonView.setSelected(false);

            if (gridViewLayout == null) {
                gridViewLayout = new ScrollAwareGridView(getActivity());
                gridViewLayout.setNumColumns(3);
            }

            gridViewLayout.setAdapter(new SquareImageAdapter(getActivity(), resultList.getImageList()));
            gridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startDetailActivity(resultList.getImageList().get(position));
                }
            });
            gridViewLayout.invalidate();
            resultGridView.removeAllViews();
            resultGridView.addView(gridViewLayout);
        }
    }

    final static ButterKnife.Action<View> SHOW = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    final static ButterKnife.Action<View> HIDE = new ButterKnife.Action<View>() {
        @Override
        public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    private void startDetailActivity(ImageResult imageResult) {
        IntentHelper.addObjectForKey(imageResult.getImageName(), IntentHelper.SEARCH_RESULT_EXTRA);
        IntentHelper.addObjectForKey(imageResult.getImageUrl(), IntentHelper.SEARCH_IMAGE_PATH_EXTRA);

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        startActivity(intent);
    }

    //change UI when uploading starts
    private void changeUploadUI() {
        loadingImage.setVisibility(View.VISIBLE);
        ButterKnife.apply(photoUIs, HIDE);
        rotateButton.setClickable(false);

        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        anim.start();
    }

    //When cancel button is clicked, bring UI back
    private void changeUploadUIBack() {
        ButterKnife.apply(photoUIs, SHOW);
        loadingImage.setVisibility(View.GONE);
        rotateButton.setClickable(true);
    }

    private ScalableBox getDetectionBox(Box box) {
        ScalableBox searchBox = new ScalableBox();
        if (box != null) {
            searchBox.setX1(box.getX1());
            searchBox.setX2(box.getX2());
            searchBox.setY1(box.getY1());
            searchBox.setY2(box.getY2());
        }

        return searchBox;
    }
}