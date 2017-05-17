package com.example.enkhturbadamsaikhan.completesudoku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.completesudoku.SudokuGame;
import com.example.david.testsudoku.DataResult;
import com.example.david.testsudoku.GameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SavedGamesActivity extends AppCompatActivity {

    private ArrayList<String> results;
    private ArrayAdapter<String> adapter;
    private TextView downloadResults;
    private Map<String, SudokuGame> sudokuGames;
    private Map<String, String> uids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_games);

        TextView cancel = (TextView) findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView listView = (ListView) findViewById(R.id.sudoku_list);
        results = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d("LOAD", name);
                SudokuGame game = sudokuGames.get(name);
                DataResult.getInstance().setSudokuGame(game);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DataResult.getInstance().setSudokuModel(
                            new FirebaseModel(FirebaseDatabase.getInstance().getReference("/Users/"+user.getUid()).child(uids.get(name))));
                    Intent intent = new Intent(SavedGamesActivity.this, GameActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(SavedGamesActivity.this, R.string.not_logged_in, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        sudokuGames = new ConcurrentHashMap<>();
        uids = new ConcurrentHashMap<>();

        final ProgressDialog progressDialog;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            progressDialog = ProgressDialog.show(SavedGamesActivity.this,
                    "Complete Sudoku",
                    "Loading saved games");
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("/Users/"+user.getUid()).child("sudokus");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Log.e("Count " ,""+snapshot.getChildrenCount());
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        String key = postSnapshot.getKey();
                        SaveGame saveGame = postSnapshot.getValue(SaveGame.class);
                        SudokuGame game = FirebaseModel.createSudokuGame(saveGame);

                        String name = String.format("%s (%s)\nDifficulty: %s\nStatus: %s \nScore: %d", game.getName(),
                                game.getElapsedFormatted(), game.getDifficulty(), game.getStatus(), game.getScore());
                        results.add(name);
                        sudokuGames.put(name, game);
                        uids.put(name, key);
                        adapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast toast = Toast.makeText(SavedGamesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast toast = Toast.makeText(this, R.string.not_logged_in, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
