package com.example.googleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText mFullName,mEmail,mPassword,newPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.email);
        newPassword = findViewById(R.id.newPassword);
        mPassword   = findViewById(R.id.confirmPassword);
        mRegisterBtn= findViewById(R.id.RegisterButton);
        mLoginBtn   = findViewById(R.id.loginText);

        fAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar3);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String confirmedpassword = mPassword.getText().toString().trim();
                String newPasswd= newPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                }

                if(TextUtils.isEmpty(newPasswd)){
                    newPassword.setError("Password is Required.");
                    return;
                }

                if(TextUtils.isEmpty(confirmedpassword)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(confirmedpassword.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                if(newPasswd.compareTo(confirmedpassword) != 0 ){
                    newPassword.setError("New Password and Confirmed Password must match.");
                    mPassword.setError("New Password and Confirmed Password must match.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in Firebase

                fAuth.createUserWithEmailAndPassword(email,confirmedpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user= fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(Register.this,"Verification Email has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","Email Not Sent "+e.getMessage());
                                }
                            });

                            final Toast toast = Toast.makeText(Register.this, "Registration Successful. Please Verify The Link Sent To Registered Email and Login", Toast.LENGTH_LONG);
                            toast.show();
                            new android.os.Handler().postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            toast.cancel();
                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }, 4500);

                        }
                        else{
                                Toast.makeText(Register.this,"Error!"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);


                        }
                    }
                });


            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}
