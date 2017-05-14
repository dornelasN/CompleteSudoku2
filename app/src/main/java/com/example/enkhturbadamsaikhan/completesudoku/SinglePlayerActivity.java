package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.david.completesudoku.Sudoku;
import com.david.completesudoku.SudokuGame;
import com.example.david.testsudoku.DataResult;
import com.example.david.testsudoku.GameActivity;
import com.example.david.testsudoku.TestModel;

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
                int[][] model = new int[9][9] ;

                // Create the initial situation

                model[0][0] = 9 ;
                model[0][4] = 2 ;
                model[0][6] = 7 ;
                model[0][7] = 5 ;

                model[1][0] = 6 ;
                model[1][4] = 5 ;
                model[1][7] = 4 ;

                model[2][1] = 2 ;
                model[2][3] = 4 ;
                model[2][7] = 1 ;

                model[3][0] = 2 ;
                model[3][2] = 8 ;

                model[4][1] = 7 ;
                model[4][3] = 5 ;
                model[4][5] = 9 ;
                model[4][7] = 6 ;

                model[5][6] = 4 ;
                model[5][8] = 1 ;

                model[6][1] = 1 ;
                model[6][5] = 5 ;
                model[6][7] = 8 ;

                model[7][1] = 9 ;
                model[7][4] = 7 ;
                model[7][8] = 4 ;

                model[8][1] = 8 ;
                model[8][2] = 2 ;
                model[8][4] = 4 ;
                model[8][8] = 6 ;

                DataResult.getInstance().setSudokuModel(new TestModel());
                DataResult.getInstance().setSudokuGame(new SudokuGame(new Sudoku(model)));
                Intent intent = new Intent(SinglePlayerActivity.this, GameActivity.class);
                startActivity(intent);
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
                Intent i = new Intent(SinglePlayerActivity.this, SavedGamesActivity.class);
                startActivity(i);
                //Toast.makeText(SinglePlayerActivity.this, "Create Loading Puzzle Activity", Toast.LENGTH_LONG).show();

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
