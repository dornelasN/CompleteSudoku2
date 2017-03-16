package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0;

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;

    private Button easy;
    private Button medium;
    private Button hard;
    private Button extreme;

    private Button upload;
    private Button saved;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        uidTextView = (TextView) findViewById(R.id.uidTextView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null ) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();

            nameTextView.setText(name);
            emailTextView.setText(email);
            uidTextView.setText(uid);

        } else {
            goToLoginScreen();
        }

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        easy = (Button) findViewById(R.id.easyButton);
        medium = (Button) findViewById(R.id.mediumButton);
        hard = (Button) findViewById(R.id.hardButton);
        extreme = (Button) findViewById(R.id.extremeButton);

        upload =  (Button) findViewById(R.id.uploadButton);
        saved = (Button) findViewById(R.id.savedSudokuButton);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        extreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PlayGameActivity.class);
                startActivity(i);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(i);
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SavedGamesActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

//        MenuItem searchItem = menu.findItem(R.id.);
//        SearchView searchView =
//                (SearchView) MenuItemCompat.getActionView(searchItem);
//
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.logout_user:
                logout();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goToLoginScreen();
    }


}
