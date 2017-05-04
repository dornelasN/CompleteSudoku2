package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class UploadActivity extends AppCompatActivity {


    private TextView cameraUpload;
    private TextView galleryUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        cameraUpload = (TextView) findViewById(R.id.tv_upload_from_camera);
        galleryUpload = (TextView) findViewById(R.id.tv_upload_from_gallery);

        cameraUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open camera
                Toast.makeText(UploadActivity.this, "Open Camera", Toast.LENGTH_LONG).show();
            }
        });

        galleryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                //Toast.makeText(UploadActivity.this, "Open Gallery", Toast.LENGTH_LONG).show();

                gotoGallery();




            }
        });

    }

    private void gotoGallery() {

        Intent intent = new Intent(this, MainActivityGallery.class);

        startActivity(intent);

    }

}
