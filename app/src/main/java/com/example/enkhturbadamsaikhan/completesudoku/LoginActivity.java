package com.example.enkhturbadamsaikhan.completesudoku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText email;
    private EditText password;

    private Button login;
    private TextView register;
    private TextView guest;

    private ProgressDialog mProgress;

    private DatabaseReference mDatabaseUsers;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgress = new ProgressDialog(this);

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
                Log.d(TAG, "guest: onclick");
                Intent i = new Intent(LoginActivity.this, GuestActivity.class);
                startActivity(i);
            }
        });

    }


    private void goToMainScreen() {
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
                    if (task.isSuccessful()) {

                        mProgress.dismiss();
                        finish();
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        //Now we can start MainActivity
                          checkUserExist();

                    } else {
                        mProgress.dismiss();

                        Toast.makeText(LoginActivity.this, "Error login", Toast.LENGTH_LONG).show();
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

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
