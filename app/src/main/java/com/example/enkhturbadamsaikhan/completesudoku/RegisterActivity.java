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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText username;
    private Button cancel;
    private Button register;
    private EditText password;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        mProgress = new ProgressDialog(this);

        username = (EditText) findViewById(R.id.et_username_register);
        email = (EditText) findViewById(R.id.et_email_register);
        password = (EditText) findViewById(R.id.et_password_register);

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


    private void startRegister() {
        // Retrieve all the field
        final String user = username.getText().toString();
        String pass = password.getText().toString();
        final String email1 = email.getText().toString();

        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(email1) && !TextUtils.isEmpty(pass)) {

            mProgress.setMessage("Signing up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email1, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //user successfully registered and signed in and will direct to MainActivity

                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();

                        //current user's id
                        String user_id = mAuth.getCurrentUser().getUid();

                        //child created with user id
                        DatabaseReference current_user_db = mDatabaseUsers.child(user_id);

                        current_user_db.child("Name").setValue(user);
                        current_user_db.child("Email").setValue(email1);


                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    }
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this, "Couldn't register, please try again...", Toast.LENGTH_LONG).show();
        }

    }
}
