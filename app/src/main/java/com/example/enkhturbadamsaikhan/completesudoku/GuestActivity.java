package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuestActivity extends AppCompatActivity {

    private Button easy;
    private Button medium;
    private Button hard;
    private Button extreme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        easy = (Button) findViewById(R.id.easyButton_guest);
        medium = (Button) findViewById(R.id.mediumButton_guest);
        hard = (Button) findViewById(R.id.hardButton_guest);
        extreme = (Button) findViewById(R.id.extremeButton_guest);

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

    }
}
