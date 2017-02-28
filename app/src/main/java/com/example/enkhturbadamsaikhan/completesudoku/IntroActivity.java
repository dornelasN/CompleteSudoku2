package com.example.enkhturbadamsaikhan.completesudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button loginButton;
    Button registerButton;
    Button guest;
    LoginButton loginButtonFB;
    CallbackManager callbackManager;

    String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);



//        mAuth = FirebaseAuth.getInstance();
//
//        startActivityForResult(AuthUI.getInstance()
//        .createSignInIntentBuilder()
//        .setProviders(
//                AuthUI.FACEBOOK_PROVIDER,
//                AuthUI.GOOGLE_PROVIDER,
//                AuthUI.EMAIL_PROVIDER)
//        .build(), 1);
//        Intent intent;
//
//        if( mAuth.getCurrentUser() != null){
//            intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }


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


//            callbackManager = CallbackManager.Factory.create();
//            loginButtonFB = (LoginButton) findViewById(R.id.login_button);
//
//            // Callback registration
//            loginButtonFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//                @Override
//                public void onSuccess(LoginResult loginResult) {
//                    // App code
//                    Log.d(TAG, "onSuccess");
//                    Intent i = new Intent(IntroActivity.this, MainActivity.class);
//                    startActivity(i);
//                }
//
//                @Override
//                public void onCancel() {
//                    // App code
//                    Log.d(TAG, "onCancel");
//                }
//
//                @Override
//                public void onError(FacebookException exception) {
//                    // App code
//                    Log.d(TAG, "onError");
//                }
//            });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }
}

