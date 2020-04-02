package com.example.googleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,mForgotBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    private long backPressedTime;
    private Toast backToast;
    String alertmessage="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        //Code to start timer and take action after the timer ends

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.LoginButton);
        mCreateBtn = findViewById(R.id.RegisterText);
        mForgotBtn=findViewById(R.id.forgotbtn);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected(Login.this))
                {buildDialog(Login.this).show();}
                else {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                 /*   mEmail.setError("Email is Required.");*/
                    Snackbar.make(findViewById(android.R.id.content), "Email is Required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    /*mPassword.setError("Password is Required.");*/
                    Snackbar.make(findViewById(android.R.id.content), "Password is Required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
/*
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }*/

                progressBar.setVisibility(View.VISIBLE);

                //authenticate the User

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(Login.this, "Invalid Password !! Please Try Again.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        mForgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected(Login.this)) {
                    buildDialog(Login.this).show();
                }
                else{
                final EditText resetMail = new EditText(v.getContext());
                resetMail.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                resetMail.setGravity(Gravity.CENTER);
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive The Reset Link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //send Reset Link
                        String mail = resetMail.getText().toString();
                        if (TextUtils.isEmpty(mail)) {
                            resetMail.setError("Email is Required.");
                            alertmessage="forgotemailerror";
                            buildDialog(Login.this).show();
                            return;
                        }
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(Login.this, "Password Reset Request Sent To Your Email.", Toast.LENGTH_SHORT).show();
                                alertmessage="successforgot";
                                buildDialog(Login.this).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                alertmessage="forgoterror";
                                buildDialog(Login.this).show();
                               // Toast.makeText(Login.this, "Email is Not Correct or The User Is Not Registered", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Closing Dialog
                    }
                });

                passwordResetDialog.create().show();
            }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            finishAffinity();
            finish();
 /*           super.onBackPressed();
            return;*/
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
        return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        if(alertmessage.equals("forgotemailerror")){
            builder.setTitle("Incorrect Email");
            builder.setMessage("Kindly Verify Your Email.");
            alertmessage="";

        }
        else if(alertmessage.equals("forgoterror")){
            builder.setTitle("Incorrect Email");
            builder.setMessage("Incorrect Email (OR) User Not Registered.");
            alertmessage="";
        }
        else if(alertmessage.equals("successforgot")){
            builder.setTitle("Forgot Request");
            builder.setMessage("Password Reset Request Sent To Your Email.");
            alertmessage="";
        }
        else {
            builder.setTitle("No Internet Connection");
            builder.setMessage("Kindly Verify Your Internet Connection.");
        }
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
               /* startActivity(new Intent(getApplicationContext(),Login.class));
                *//*finish();*/
            }
        });

        return builder;
    }
}
