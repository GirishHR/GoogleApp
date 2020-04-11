package com.example.googleapp;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

public class device_admin extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1 ;
    ToggleButton tbutton;

    SharedPreferences.Editor prefEditor;
    SharedPreferences prefs;

    private FusedLocationProviderClient fusedLocationClient;//location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_admin);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        prefEditor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        tbutton = (ToggleButton)findViewById(R.id.toggleButton);

        tbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(tbutton.isChecked())
                {
                    fetchlocation();
 /*                   fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        // Logic to handle location object
                                    }
                                }
                            });*/
                   /* Toast.makeText(device_admin.this, "Safe Locator is on", Toast.LENGTH_LONG).show();*/
/*                    prefEditor.putString("checked","yes");
                    prefEditor.apply();*/

                    if(prefs.getString("granted","false").equals("yes"))
                    {
                            turnon();
                    }
                }
                else {
                    Toast.makeText(device_admin.this, "Safe Locator is Off", Toast.LENGTH_LONG).show();
                 /*   prefEditor.putString("checked","false");
                    prefEditor.apply();*/
                    turnon();
                }
            }
        });

        SharedPreferences prefs = getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
        boolean bool= prefs.getBoolean("lockedState", false);

        if (prefs.getString("checked","no").equals("yes")){

            tbutton.setChecked(true);
            Toast.makeText(device_admin.this, "Safe Locator is on", Toast.LENGTH_LONG).show();

        }else {

            tbutton.setChecked(false);
        }




    }

/*    @Override
    protected void onStart() {

        super.onStart();
        Toast.makeText(this, "call onstart method", Toast.LENGTH_LONG).show();

    }*/

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        Toast.makeText(this, "call onwindow focus change method", Toast.LENGTH_LONG).show();
        statuschange();
    }

    public void statuschange(){
        ComponentName cn = new ComponentName(this, AdminReceiver.class);
        DevicePolicyManager mgr =
                (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        if (mgr.isAdminActive(cn)) {
            tbutton.setChecked(true);
        }
        else{
            tbutton.setChecked(false);
        }


    }

    public void turnon() {
        ComponentName cn = new ComponentName(this, AdminReceiver.class);
        DevicePolicyManager mgr =
                (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        if (tbutton.isChecked()) {
            boolean  hai= tbutton.isChecked();
            tbutton.setChecked(false);
/*            prefEditor.putString("checked","yes");
            prefEditor.apply();*/
            if (mgr.isAdminActive(cn)) {
                int msgId;
                tbutton.setChecked(true);
                if (mgr.isActivePasswordSufficient()) {
                    int i = 2;
                    msgId = R.string.compliant;
                } else {
                    msgId = R.string.not_compliant;
                }

                Toast.makeText(this, msgId, Toast.LENGTH_LONG).show();
            } else {
                Intent intent =
                        new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        getString(R.string.device_admin_explanation));
                startActivity(intent);
            }

        } else {
            mgr.removeActiveAdmin(cn);
            prefEditor.putString("checked","false");
            prefEditor.apply();
            tbutton.setChecked(false);
        }
    }

    public void fetchlocation(){

        final ComponentName cn = new ComponentName(this, AdminReceiver.class);
        final DevicePolicyManager mgr =
                (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                prefEditor.putString("granted","no");
                prefEditor.apply();
                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess this feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(device_admin.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                mgr.removeActiveAdmin(cn);
                                prefEditor.putString("checked","false");
                                prefEditor.apply();

                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(device_admin.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                prefEditor.putString("granted","no");
                prefEditor.apply();

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            prefEditor.putString("granted","yes");
            prefEditor.apply();
 /*           fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(device_admin.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.

                            if (location != null) {
                                // Logic to handle location object

                                // Logic to handle location object
                                Double latitude = location.getLatitude();
                                Double longitude = location.getLongitude();
                                String mapLink="https://www.google.com/maps/search/?api=1&query="+latitude+","+longitude;
                                prefEditor.putString("locationAddress",mapLink);
                                prefEditor.apply();
                               *//* new AlertDialog.Builder(device_admin.this)
                                        .setTitle("LAtitude and longitude")
                                        .setMessage("https://www.google.com/maps/search/?api=1&query="+latitude+","+longitude)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ActivityCompat.requestPermissions(device_admin.this,
                                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                                        MY_PERMISSIONS_REQUEST_READ_LOCATION);
                                            }
                                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                        .create()
                                        .show();*//*

                            }
                        }
                    });*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_READ_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //abc
                Snackbar.make(findViewById(android.R.id.content), "Kindly, Turn ON Safe Locator.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                prefEditor.putString("granted","yes");
                prefEditor.apply();

            }else{
                prefEditor.putString("granted","no");
                prefEditor.apply();
                final ComponentName cn = new ComponentName(this, AdminReceiver.class);
                final DevicePolicyManager mgr =
                        (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                mgr.removeActiveAdmin(cn);
                prefEditor.putString("checked","false");
                prefEditor.apply();
            }
        }

    }
}
