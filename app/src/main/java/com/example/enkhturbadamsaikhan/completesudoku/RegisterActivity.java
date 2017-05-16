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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText username;
    private Button cancel;
    private Button register;
    private EditText password;
    private EditText reenter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username.getText().toString()).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        }
                    });
                }
            }
        };

        mProgress = new ProgressDialog(this);

        username = (EditText) findViewById(R.id.et_username_register);
        email = (EditText) findViewById(R.id.et_email_register);
        password = (EditText) findViewById(R.id.et_password_register);
        reenter = (EditText) findViewById(R.id.et_password_reenter);

        cancel = (Button) findViewById(R.id.b_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


        register = (Button) findViewById(R.id.b_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startRegister();

            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void startRegister() {
        // Retrieve all the field
        final String userName = username.getText().toString();
        String pass = password.getText().toString();
        String email1 = email.getText().toString();
        String reen = reenter.getText().toString();

        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(email1) && !TextUtils.isEmpty(pass)
                && !TextUtils.isEmpty(reen) && reen.equals(pass)) {

            mProgress.setMessage("Signing up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email1, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //user successfully registered and signed in and will direct to MainActivity

                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();

                        mProgress.dismiss();

                    } else {
                        mProgress.dismiss();
                        if (task.getException() != null) {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Firebase registration error", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "Couldn't register, please try again...", Toast.LENGTH_LONG).show();
        }

    }
}
