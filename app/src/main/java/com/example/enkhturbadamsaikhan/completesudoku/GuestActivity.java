package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.david.completesudoku.SudokuGame;
import com.david.completesudoku.SudokuGenerator;
import com.example.david.testsudoku.DataResult;
import com.example.david.testsudoku.GameActivity;
import com.example.david.testsudoku.TestModel;

import java.io.IOException;
import java.io.InputStreamReader;

public class GuestActivity extends AppCompatActivity {

    private static final String TAG = "SinglePlayerActivity";
    private TextView easy;
    private TextView medium;
    private TextView hard;
    private TextView extreme;
    private TextView upload;

    private SudokuGenerator sudokuGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        easy = (TextView) findViewById(R.id.easyButton_guest);
        medium = (TextView) findViewById(R.id.mediumButton_guest);
        hard = (TextView) findViewById(R.id.hardButton_guest);
        extreme = (TextView) findViewById(R.id.extremeButton_guest);
        upload = (TextView) findViewById(R.id.tv_upload);

        try {
            sudokuGenerator = new SudokuGenerator(new InputStreamReader(getAssets().open("puzzler.txt")));
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(SudokuGenerator.EASY);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(SudokuGenerator.MEDIUM);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(SudokuGenerator.HARD);
            }
        });

        extreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(SudokuGenerator.DIABOLICAL);
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

    private void startGame(String difficulty) {
        DataResult.getInstance().setSudokuGame(new SudokuGame(sudokuGenerator.getSudoku(difficulty)));
        DataResult.getInstance().setSudokuModel(new TestModel());
        Intent intent = new Intent(GuestActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
