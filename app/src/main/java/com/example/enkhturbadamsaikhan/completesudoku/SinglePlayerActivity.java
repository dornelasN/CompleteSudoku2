package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePlayerActivity extends AppCompatActivity {

    private TextView easy;
    private TextView medium;
    private TextView hard;
    private TextView extreme;
    private TextView loadPuzzle;
    private TextView uploadPuzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        easy = (TextView) findViewById(R.id.tv_easy_mode);
        medium = (TextView) findViewById(R.id.tv_medium_mode);
        hard = (TextView) findViewById(R.id.tv_hard_mode);
        extreme = (TextView) findViewById(R.id.tv_extreme_mode);
        loadPuzzle = (TextView) findViewById(R.id.tv_load_puzzle);
        uploadPuzzle = (TextView) findViewById(R.id.tv_upload);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinglePlayerActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinglePlayerActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinglePlayerActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        extreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinglePlayerActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        loadPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(SinglePlayerActivity.this, LoadGameActivity.class);
                //startActivity(i);
                Toast.makeText(SinglePlayerActivity.this, "Create Loading Puzzle Activity", Toast.LENGTH_LONG).show();

            }
        });

        uploadPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinglePlayerActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });
    }
}
