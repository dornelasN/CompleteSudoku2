package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuestActivity extends AppCompatActivity {

    private TextView easy;
    private TextView medium;
    private TextView hard;
    private TextView extreme;
    private TextView upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        easy = (TextView) findViewById(R.id.easyButton_guest);
        medium = (TextView) findViewById(R.id.mediumButton_guest);
        hard = (TextView) findViewById(R.id.hardButton_guest);
        extreme = (TextView) findViewById(R.id.extremeButton_guest);
        upload = (TextView) findViewById(R.id.tv_upload);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuestActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuestActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuestActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        extreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuestActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuestActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });


    }
}
