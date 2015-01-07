package com.visenze.visearch.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.visenze.visearch.demo.R;

import java.io.IOException;

/**
 * search view UI
 * Created by visenze on 12/9/14.
 */
public class SearchView extends LinearLayout{
    private static final String SEARCH_VIEW = "search view";

    //buttons
    private Button uploadSearchButton;
    private Button idSearchButton;
    private Button colorSearchButton;

    //edit text
    private EditText colorText;
    private EditText indexText;
    private EditText pageText;
    private EditText limitText;

    //switch
    private Switch   scoreSwitch;
    private Switch   urlSwitch;

    //color view
    private View    colorPreview;

    //image view
    private ImageView imageView;
    private Bitmap    bitmap;
    private Uri       uri;

    public SearchView(Context context) {
        super(context);
        initView(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.search_layout, this, true);

        //EditText
        colorText = (EditText)findViewById(R.id.color_edit_text);
        indexText = (EditText)findViewById(R.id.id_edit_text);
        pageText = (EditText) findViewById(R.id.page_edit_text);
        limitText = (EditText) findViewById(R.id.limit_edit_text);

        //switch
        scoreSwitch = (Switch) findViewById(R.id.score_switch);
        urlSwitch = (Switch) findViewById(R.id.url_switch);

        //button
        idSearchButton = (Button)findViewById(R.id.idx_search_button);
        colorSearchButton = (Button)findViewById(R.id.color_search_button);
        uploadSearchButton = (Button)findViewById(R.id.upload_search_button);

        //Color
        colorPreview = (View) findViewById(R.id.color_preview);

        //Image
        imageView = (ImageView)findViewById(R.id.image_view);

    }

    public Button getUploadSearchButton() {
        return uploadSearchButton;
    }

    public Button getColorSearchButton() {
        return colorSearchButton;
    }

    public Button getIdSearchButton() {
        return idSearchButton;
    }

    public Uri getUri() {
        if (uri == null) {
            Log.e(SEARCH_VIEW, "empty image path");
            Toast.makeText(getContext(), "empty image path", Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    public void loadAndDisplayImage(Uri uri, Context context) {
        this.uri = uri;
        try {
            if (bitmap != null)
                bitmap.recycle();

            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getColorText() {
        String text = null;
        if (colorText.getText() != null && colorText.getText().toString().length() > 0) {
            text = colorText.getText().toString();
            if (text.charAt(0) == '#')
                text = text.substring(1, text.length());

            if (!text.matches("^[0-9a-fA-F]{6}$")) {
                Toast.makeText(getContext(), "invalid color code", Toast.LENGTH_SHORT).show();
                Log.e(SEARCH_VIEW, "invalid color code");
                text = null;
            } else{
                colorPreview.setBackgroundColor(Color.parseColor("#"+text));
            }
        } else{
            Log.e(SEARCH_VIEW, "empty color code input");
            Toast.makeText(getContext(), "empty text input", Toast.LENGTH_SHORT).show();
        }

        hideKeyboard(colorText);

        return text;
    }

    public String getIndexText() {
        String text = null;
        if (indexText.getText() != null && indexText.getText().toString().length() > 0)
            text = indexText.getText().toString();
        else {
            Log.e(SEARCH_VIEW, "empty image name input");
            Toast.makeText(getContext(), "empty image name input", Toast.LENGTH_SHORT).show();
        }
        hideKeyboard(indexText);

        return text;
    }

    public String getPageNumber() {
        String text = null;
        if (pageText.getText() != null && pageText.getText().toString().length() > 0)
            text = pageText.getText().toString();

        hideKeyboard(pageText);

        return text;
    }

    public String getLimit() {
        String text = null;
        if (limitText.getText() != null && limitText.getText().toString().length() > 0)
            text = limitText.getText().toString();

        hideKeyboard(limitText);

        return text;
    }

    public boolean getScoreChecked() {
        return scoreSwitch.isChecked();
    }

    public boolean getUrlChecked() {
        return urlSwitch.isChecked();
    }

    public ImageView getImageView() {
        return imageView;
    }

    private void hideKeyboard(EditText v) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
