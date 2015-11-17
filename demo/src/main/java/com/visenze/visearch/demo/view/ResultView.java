package com.visenze.visearch.demo.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.demo.R;

import java.util.List;

/**
 * display result in grid view
 * Created by visenze on 12/9/14.
 */
public class ResultView extends LinearLayout {
    private GridView     gridView;
    private TextView     resultText;
    private ProgressBar  progressBar;

    public ResultView(Context context) {
        super(context);
        initView(context);
    }

    public ResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ResultView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.result_layout, this, true);

        gridView = (GridView) findViewById(R.id.result_grid_view);
        resultText = (TextView) findViewById(R.id.result_text_view);
        progressBar = (ProgressBar) findViewById(R.id.result_progress_bar);

    }

    public void startSearchResult() {
        progressBar.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);

        resultText.setText(getContext().getResources().getString(R.string.searching));
    }

    public void displayError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        String errorDisplay = getContext().getResources().getString(R.string.error);
        resultText.setText(errorDisplay + errorMessage);
    }

    public void updateResult(Activity context, List<ImageResult> resultList) {
        progressBar.setVisibility(View.GONE);
        resultText.setText(getContext().getResources().getString(R.string.search_result));
        gridView.setVisibility(View.VISIBLE);

        ImageAdapter imageAdapter = new ImageAdapter(context, resultList);
        gridView.setAdapter(imageAdapter);
        invalidate();
    }

    /**
     * Image adapter for gridview
     */
    private class ImageAdapter extends ArrayAdapter<ImageResult> {
        private Activity mContext;
        private List<ImageResult> imageList;

        public ImageAdapter(Activity c, List<ImageResult> imageList) {
            super(c, R.layout.result_layout, imageList);
            this.mContext = c;
            this.imageList = imageList;
        }

        // create a new imageLayout for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View imageLayout = convertView;
            ViewHolder viewHolder;

            if (convertView == null) {
                LayoutInflater inflater = mContext.getLayoutInflater();

                imageLayout = inflater.inflate(R.layout.image_item_layout, null);
                viewHolder = new ViewHolder();

                viewHolder.imageNameView = (TextView)imageLayout.findViewById(R.id.image_name_view);
                viewHolder.imageView = (ImageView)imageLayout.findViewById(R.id.result_image_view);
                viewHolder.scoreView = (TextView) imageLayout.findViewById(R.id.result_score_view);

                imageLayout.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder)imageLayout.getTag();
            }

            //load image
            Picasso.with(mContext)
                .load(imageList.get(position).getImageUrl())
                .placeholder(R.drawable.empty_image)
                .error(R.drawable.empty_image)
                .resize(200, 200)
                .tag(mContext)
                .into(viewHolder.imageView);

            if (imageList.get(position).getScore() != null)
                viewHolder.scoreView.setText(imageList.get(position).getScore().toString());

            viewHolder.imageNameView.setText(imageList.get(position).getImageName());

            return imageLayout;
        }

        class ViewHolder {
            public TextView imageNameView;
            public TextView scoreView;
            public ImageView imageView;
        }
    }
}
