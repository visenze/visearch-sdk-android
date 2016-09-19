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
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.TrackParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.camerademo.DetailActivity;
import com.visenze.visearch.camerademo.MainActivity;
import com.visenze.visearch.camerademo.R;
import com.visenze.visearch.camerademo.http.SearchAPI;
import com.visenze.visearch.camerademo.util.DataHelper;
import com.visenze.visearch.camerademo.views.ScrollAwareGridView;
import com.visenze.visearch.camerademo.views.adapter.SquareImageAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by visenze on 28/5/15.
 *
 * @author yulu
 */
public class DetailFragment extends Fragment implements ViSearch.ResultListener {
    //inject ui
    @InjectView(R.id.detail_image_view)     ImageView               detailImageView;
    @InjectView(R.id.result_query_image)    ImageView               queryImageView;
    @InjectView(R.id.detail_grid_view)      ScrollAwareGridView     similarListView;
    @InjectView(R.id.sliding_detail_layout) SlidingUpPanelLayout    slidingUpPanelLayout;
    @InjectView(R.id.edit_query_view)       TextView                editQueryView;
    @InjectView(R.id.result_loading)        ImageView               loadingImage;
    @InjectView(R.id.detail_im_name_view)   TextView                imNameView;

    //ViSearch and Search parameters
    private ViSearch viSearch;
    private String   url;

    public static DetailFragment newInstance() {
       return new DetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_layout, container, false);
        ButterKnife.inject(this, view);

        //get viSearch instance
        try {
            viSearch = SearchAPI.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String imName = ((DetailActivity)getActivity()).getImName();
        url = ((DetailActivity)getActivity()).getUrl();

        viSearch.setListener(this);

        updateUI(url, imName);
        editQueryView.setText("");

        startSearch(imName);

        return view;
    }

    @OnClick(R.id.detail_back_button)
    public void clickBack() {
        viSearch.cancelSearch();
        getActivity().finish();
    }

    @OnClick(R.id.detail_home_button)
    public void returnHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.detail_image_view)
    public void expandImageOnImage() {
        ImageFragment imageFragment = ImageFragment.newInstance(url);
        imageFragment.show(getFragmentManager().beginTransaction(), "Dialog");
    }

    @OnClick(R.id.result_query_image)
    public void clickQuery() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onSearchResult(final ResultList resultList) {
        if (isAdded()) {
            loadingImage.setVisibility(View.GONE);
            similarListView.setVisibility(View.VISIBLE);
            similarListView.setAdapter(new SquareImageAdapter(getActivity(), resultList.getImageList()));
            similarListView.invalidate();
            similarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageResult imageResult = resultList.getImageList().get(position);
                    updateUI(imageResult.getImageUrl(), imageResult.getImageName());
                    startSearch(imageResult.getImageName());
                    viSearch.track(new TrackParams().setAction("click").setReqid(resultList.getTransId()).setImName(imageResult.getImageName()));
                }
            });
        }
    }

    @Override
    public void onSearchError(String errorMessage) {
        loadingImage.setVisibility(View.GONE);
    }

    @Override
    public void onSearchCanceled() {

    }

    private void startSearch(String imName) {
        loadingImage.setVisibility(View.VISIBLE);
        similarListView.setVisibility(View.GONE);
        //start scan
        AnimationDrawable anim = (AnimationDrawable) loadingImage.getDrawable();
        anim.start();

        //start id search
        IdSearchParams params = new IdSearchParams(imName);
        DataHelper.setIdSearchParams(params.getBaseSearchParams());
        viSearch.idSearch(params);
    }

    private void updateUI(String url, String imName) {
        Picasso.with(getActivity())
                .load(url)
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .tag(getActivity())
                .into(detailImageView);

        Picasso.with(getActivity())
                .load(url)
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .tag(getActivity())
                .into(queryImageView);
        imNameView.setText(imName);

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        this.url = url;
    }
}