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

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.visenze.visearch.camerademo.BuildConfig;
import com.visenze.visearch.camerademo.CameraActivity;
import com.visenze.visearch.camerademo.R;
import com.visenze.visearch.camerademo.util.PermissionHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by visenze on 2/12/15.
 *
 * @author yulu
 */
public class MainFragment extends Fragment {
    @InjectView(R.id.version_id)        TextView    versionId;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @OnClick(R.id.main_image_button)
    public void startImageSearch() {
        // Check camera an storage permission has been granted
        if (!(PermissionHelper.checkCameraPermission(getActivity()) && PermissionHelper.checkWriteExternal(getActivity()))) {
            // See if user has denied permission in the past
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show a simple snackbar explaining the request instead
                showPermissionSnackbar();
            } else {
                // Otherwise request permission from user
                PermissionHelper.requestCameraPermission(getActivity());
            }
        } else {
            // Otherwise permission is granted (which is always the case on pre-M devices)
            cameraPermissionGranted();
        }
    }

    private void cameraPermissionGranted() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PermissionHelper.CAMERA_PERMISSION_REQ) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraPermissionGranted();
            } else {
                Toast.makeText(getActivity(), "We need camera permission to take pictures", Toast.LENGTH_SHORT).show();
            }
            // END_INCLUDE(permission_result)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionSnackbar() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Permissions Needed")
                .setMessage("We need permissions for using camera")
                .setPositiveButton(R.string.grant, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        PermissionHelper.requestCameraPermission(getActivity());

                    }
                })
                .setNegativeButton(R.string.dont_grant, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        Toast.makeText(getActivity(), "We need camera permission to take pictures", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_layout, container, false);
        ButterKnife.inject(this, view);
        versionId.setText(BuildConfig.VERSION_NAME);

        return view;
    }
}