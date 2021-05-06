package com.example.smarttravelguideapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginRegisterActivity extends AppCompatActivity {

    private EditText user_email, user_password;
    private ImageView link_Login;
    private TextView status;
    private Button btn_user_signup, btn_user_login;

    private ProgressDialog dialog;
    private DatabaseReference CustomerDatabaseRef;
    private String onlineUserID;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mauth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);

        link_Login = findViewById(R.id.link_login);
        status = findViewById(R.id.heading_title);

        user_email = findViewById(R.id.etuserEmail);
        user_password = findViewById(R.id.etuserPassword);

        btn_user_login = findViewById(R.id.btnlogin);
        btn_user_signup = findViewById(R.id.btnregister);

//        btn_user_signup.setVisibility(View.VISIBLE);
        btn_user_login.setVisibility(View.INVISIBLE);
        btn_user_login.setEnabled(false);

        btn_user_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RegisterUser();
            }
        });

        btn_user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserLogin();
            }
        });


        link_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlogin();
            }
        });
    }

    private void openlogin() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);

        status.setText("Welcome \nSmart Travel \nGuide?");

        btn_user_signup.setVisibility(View.INVISIBLE);
        btn_user_signup.setEnabled(false);

        btn_user_login.setVisibility(View.VISIBLE);
        btn_user_login.setEnabled(true);
    }

    private void RegisterUser() {
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter Email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter Password...", Toast.LENGTH_SHORT).show();
        } else {
            mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginRegisterActivity.this, "Authentication failed." + task.getException(),
                                Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(LoginRegisterActivity.this, "Successfully Register User. Please enter your email and password for login", Toast.LENGTH_SHORT).show();
                        user_email.getText().clear();
                        user_password.getText().clear();
                        openlogin();

                    }
                }
            });

//            mauth.createUserWithEmailAndPassword(cemail, cpassword)
//                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                onlineUserID = mauth.getCurrentUser().getUid();
//                                CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference()
//                                        .child("Users").child("Guide").child(onlineUserID);
//                                CustomerDatabaseRef.setValue(true);
//                                Intent driverIntent = new Intent(LoginRegisterActivity.this, MainActivity.class);
//                                startActivity(driverIntent);
//
//                                Toast.makeText(LoginRegisterActivity.this, "Successfully Register Customer Information...", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            } else {
//                                Toast.makeText(LoginRegisterActivity.this, "Failed to Register Details. Please Try Again.", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//
//                        }
//                    });
        }


    }

    private void UserLogin() {
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter Email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter Password...", Toast.LENGTH_SHORT).show();
        } else {
            dialog.setIndeterminate(true);
            dialog.setTitle("User Login");
            dialog.setMessage("Loading...");
            dialog.show();

            mauth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginRegisterActivity.this, MainActivity.class));
                                Toast.makeText(LoginRegisterActivity.this, "Successfully Login...", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            } else {
                                Toast.makeText(LoginRegisterActivity.this, "Failed to Login. Please Try Again.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }
                    });
        }

    }
}