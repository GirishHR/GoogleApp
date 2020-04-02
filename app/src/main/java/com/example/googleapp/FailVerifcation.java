package com.example.googleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FailVerifcation extends AppCompatActivity {

    FirebaseAuth fAuth;
    Button mVerifyBtn;
    TextView mFailMsg,mCreateBtn;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_verifcation);

        fAuth= FirebaseAuth.getInstance();

        final FirebaseUser user= fAuth.getCurrentUser();


        mVerifyBtn=findViewById(R.id.button);
        mCreateBtn=findViewById(R.id.RegisterText);

        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(v.getContext(),"Verification Email has been Sent.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","Email Not Sent "+e.getMessage());
                    }
                });

                Toast.makeText(v.getContext(), "Kindly Verify The URL Sent To Registered Email", Toast.LENGTH_LONG).show();

            }
        }) ;
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
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
    public void logout() {
        FirebaseAuth.getInstance().signOut();//logout
        /*  finish()*/;

        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}
