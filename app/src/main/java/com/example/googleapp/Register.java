package com.example.googleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText mFullName,mEmail,mPassword,newPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private long backPressedTime;
    private Toast backToast;
    String alertmessage="";

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

/*        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnected(Register.this)) {
                    buildDialog(Register.this).show();
                }
                else
                    {
                final String email = mEmail.getText().toString().trim();
                String confirmedpassword = mPassword.getText().toString().trim();
                String newPasswd = newPassword.getText().toString().trim();
                final String fullName= mFullName.getText().toString().trim();


                if (TextUtils.isEmpty(fullName)) {

                    /* mEmail.setError("Email is Required.");*/
                    Snackbar.make(findViewById(android.R.id.content), "Please Enter Your Name", Snackbar.LENGTH_LONG)
                                  .setAction("Action", null).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                   /* mEmail.setError("Email is Required.");*/
                    Snackbar.make(findViewById(android.R.id.content), "Email is Required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (TextUtils.isEmpty(newPasswd)) {
                    Snackbar.make(findViewById(android.R.id.content), "Password is Required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmedpassword)) {
                   /* mPassword.setError("Password is Required.");*/
                    Snackbar.make(findViewById(android.R.id.content), "Confirmation Password is Required", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (confirmedpassword.length() < 6) {
                   /* mPassword.setError("Password Must be >= 6 Characters");*/
                    Snackbar.make(findViewById(android.R.id.content), "Password Must be atleast 6 Characters", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                if (newPasswd.compareTo(confirmedpassword) != 0) {
                    Snackbar.make(findViewById(android.R.id.content), "Both the Passwords must Match", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in Firebase

                fAuth.createUserWithEmailAndPassword(email, confirmedpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(Register.this,"Verification Email has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Email Not Sent " + e.getMessage());
                                }
                            });

                            /*final Toast toast = Toast.makeText(Register.this, "Registration Successful. Please Verify The Link Sent To Registered Email and Login", Toast.LENGTH_LONG);
                            toast.show();*/
                            alertmessage="verificationsuccess";
                            buildDialog(Register.this).show();
                            progressBar.setVisibility(View.GONE);

                        } else {
                            Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);


                        }
                    }
                });


            }
        }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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
        if(alertmessage.equals("verificationsuccess")){
            builder.setTitle("Thank You For Registering");
            builder.setMessage("A verification email has been sent to your registered email. Please verify.");
        }
        else{
        builder.setTitle("No Internet Connection");
        builder.setMessage("Kindly Verify Your Internet Connection.");

        }

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* startActivity(new Intent(getApplicationContext(),Login.class));
                 *//*finish();*/if(alertmessage.equals("verificationsuccess")){
                    logout();

                }
                else{
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }


            }
        });

        return builder;
    }
    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
      /*  finish()*/;

        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}
