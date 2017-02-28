package com.example.enkhturbadamsaikhan.completesudoku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

//    private static final int RC_SIGN_IN = 0;
    private EditText email;
    private EditText password;

    private Button login;
    private TextView register;

//    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;

    private DatabaseReference mDatabaseUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mAuth = FirebaseAuth.getInstance();
//
//        if(mAuth.getCurrentUser() != null) {
//            //user already signed in
//            Log.d("AUTH", mAuth.getCurrentUser().getEmail());
//        } else {
//
//            startActivityForResult(AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setProviders(
//                            AuthUI.FACEBOOK_PROVIDER,
//                            AuthUI.GOOGLE_PROVIDER,
//                            AuthUI.EMAIL_PROVIDER)
//                    .build(), RC_SIGN_IN);
//        }


//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
//        mDatabaseUsers.keepSynced(true);

//        mProgress = new ProgressDialog(this);

//        email = (EditText) findViewById(R.id.et_username_login);
//        password = (EditText) findViewById(R.id.et_password_login);
//
//        login = (Button) findViewById(R.id.b_login);
//
//        register = (TextView) findViewById(R.id.tv_redirect_register);
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkLogin();
//            }
//        });
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
//                registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(registerIntent);
//            }
//        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            if (resultCode == RESULT_OK) {
//                Log.d("AUTH", mAuth.getCurrentUser().getEmail());
//
////                Intent intent = new Intent(this, MainActivity.class);
////                startActivity(intent);
////                finish();
//
//            } else {
//                Log.d("AUTH", "NOT AUTHENTICATED");
//            }
//        }
//    }

//    private void checkLogin() {
//
//        String pass = password.getText().toString();
//        String email1 = email.getText().toString();
//
//        if(!TextUtils.isEmpty(email1) && !TextUtils.isEmpty(pass)) {
//
//            mProgress.setMessage("Checking Login...");
//            mProgress.show();
//
//            mAuth.signInWithEmailAndPassword(email1, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//
//                        mProgress.dismiss();
//
//                        checkUserExist();
//
//                    } else {
//                        mProgress.dismiss();
//
//                        Toast.makeText(LoginActivity.this, "Error login", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
//        }
//    }

//    private void checkUserExist() {
//        final String user_id = mAuth.getCurrentUser().getUid();
//
//        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(user_id)) {
//                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(mainIntent);
//                } else {
//                    Toast.makeText(LoginActivity.this, "You need to set up your account", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
