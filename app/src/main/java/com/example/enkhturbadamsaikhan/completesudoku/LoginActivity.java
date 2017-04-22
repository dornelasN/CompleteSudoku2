package com.example.enkhturbadamsaikhan.completesudoku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import org.opencv.android.OpenCVLoader;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText email;
    private EditText password;

    private Button login;
    private TextView register;
    private TextView guest;

    private ProgressDialog mProgress;

    private DatabaseReference mDatabaseUsers;

    private static final String TAG = "LoginActivity";

    public native String stringFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");

        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgress = new ProgressDialog(this);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.lgnButton);

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        // Example of a call to a native method
        String text = "";
        text += stringFromJNI();
        Log.d(TAG, text);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

                Profile profile = Profile.getCurrentProfile();
                //nextActivity(profile);

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Error occured", Toast.LENGTH_SHORT).show();
            }
        });


        //email login
        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "FirebaseUser user = "+firebaseAuth.getCurrentUser().getDisplayName());
                    goToMainScreen();
                }

            }
        };

        email = (EditText) findViewById(R.id.et_username_login);
        password = (EditText) findViewById(R.id.et_password_login);

        login = (Button) findViewById(R.id.b_login) ;

        register = (TextView) findViewById(R.id.tv_redirect_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "login: onclick");
                checkLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "register: onclick");
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(registerIntent);
            }
        });

        guest = (TextView) findViewById(R.id.tv_guest_login);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing as guest", Toast.LENGTH_LONG).show();
                Log.d(TAG, "guest: onclick");
                Intent i = new Intent(LoginActivity.this, GuestActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        mProgress.setMessage("Connecting to Facebook...");
        mProgress.show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            if (task.getException() != null) {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Firebase login error", Toast.LENGTH_LONG).show();
                            }
                        }
                        mProgress.dismiss();
                    }
                });
    }

    private void goToMainScreen() {
        Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_LONG).show();
        Log.d(TAG, "goToMainScreen()");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }


    private void checkLogin() {

        Log.d(TAG, "checkLogin()");
        String pass = password.getText().toString();
        String email1 = email.getText().toString();

        if(!TextUtils.isEmpty(email1) && !TextUtils.isEmpty(pass)) {

            Log.d(TAG, "(!TextUtils.isEmpty("+email1+") && !TextUtils.isEmpty("+pass+"))");
            mProgress.setMessage("Checking Login...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email1, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "signInWithEmailAndPassword: OnComplete");
                    mProgress.dismiss();
                    if (task.isSuccessful()) {
                        finish();
                        //Now we can start MainActivity
                        checkUserExist();

                    } else {
                        if (task.getException() != null) {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Firbase login error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
//        else if (TextUtils.isEmpty(email1)) {
//            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
//        } else if (TextUtils.isEmpty(pass)) {
//            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
//        }
    }

    private void checkUserExist() {
        Log.d(TAG, "checkUserExist()");
        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange(DataSnapshot "+dataSnapshot.toString()+")");
                if(dataSnapshot.hasChild(user_id)) {
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "dataSnapshot.hasChild("+user_id+")");
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            Log.d(TAG, "onCancelled("+databaseError.getMessage()+")");
            }
        });
    }
}
