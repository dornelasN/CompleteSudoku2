package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    Button loginButton;
    Button registerButton;
    Button guest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loginButton = (Button) findViewById(R.id.b_login_intro);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        registerButton = (Button) findViewById(R.id.b_register_intro);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        guest = (Button) findViewById(R.id.b_guest);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroActivity.this, GuestActivity.class);
                startActivity(i);
            }
        });

    }
}

