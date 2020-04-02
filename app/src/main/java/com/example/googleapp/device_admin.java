package com.example.googleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class device_admin extends AppCompatActivity {
    ToggleButton tbutton;

    SharedPreferences.Editor prefEditor;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_admin);

        prefEditor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        tbutton = (ToggleButton)findViewById(R.id.toggleButton);

        tbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(tbutton.isChecked())
                {
                    Toast.makeText(device_admin.this, "Safe Locator is on", Toast.LENGTH_LONG).show();
                    prefEditor.putString("checked","yes");
                    prefEditor.apply();
                }
                else {
                    Toast.makeText(device_admin.this, "Safe Locator is Off", Toast.LENGTH_LONG).show();
                    prefEditor.putString("checked","false");
                    prefEditor.apply();
                }
            }
        });

        if (prefs.getString("checked","no").equals("yes")){

            tbutton.setChecked(true);

        }else {

            tbutton.setChecked(false);
        }



    }
}
