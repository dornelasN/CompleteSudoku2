package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class UploadActivity extends AppCompatActivity {


    private TextView cameraUpload;
    private TextView galleryUpload;
    private int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "UploadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        cameraUpload = (TextView) findViewById(R.id.tv_upload_from_camera);
        galleryUpload = (TextView) findViewById(R.id.tv_upload_from_gallery);

        cameraUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCamera();
            }
        });

        galleryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGallery();
            }
        });

    }

    private void gotoCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void gotoGallery() {
        Intent intent = new Intent(this, MainActivityGallery.class);
        startActivity(intent);
    }

}
