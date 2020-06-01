/*
package com.example.googleapp;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

*/
/*
public class AdminReceiver  {
}
*//*

public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onPasswordFailed(android.content.Context context, android.content.Intent intent, android.os.UserHandle userHandle) {
        DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int no = mgr.getCurrentFailedPasswordAttempts();

        if (no >= 3) {
       */
/*     context.startActivity(new Intent(context, Login.class));*//*

            Toast.makeText(context, R.string.password_failed, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
*/

/***
 Copyright (c) 2013 CommonsWare, LLC
 Licensed under the Apache License, Version 2.0 (the "License"); you may not
 use this file except in compliance with the License. You may obtain a copy
 of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 OF ANY KIND, either express or implied. See the License for the specific
 language governing permissions and limitations under the License.

 Covered in detail in the book _The Busy Coder's Guide to Android Development_
 https://commonsware.com/Android
 */

package com.example.googleapp;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminReceiver extends DeviceAdminReceiver{
    private FusedLocationProviderClient fusedLocationClient;//location


    @Override
    public void onDisabled(Context context, Intent intent) {
/*        Toast.makeText(context, "Device Administrator Disabled",
                Toast.LENGTH_SHORT).show();*/
        SharedPreferences prefs = context.getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("lockedState", false);
        editor.putString("checked", "false");
        editor.apply();
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Admin rights are beeing requested to be disabled for the app called: '" + context.getString(R.string.app_name) + "'.";
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
/*        Toast.makeText(context, "Device Administrator Started",
                Toast.LENGTH_SHORT).show();*/
/*        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);*/


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
            }
        }

        SharedPreferences prefs = context.getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("lockedState", true);
        editor.putString("checked", "yes");
        editor.apply();
        Intent i = new Intent(context, device_admin.class);
        context.startActivity(i);

    }

    @Override
    public void onPasswordChanged(Context ctxt, Intent intent) {
        DevicePolicyManager mgr =
                (DevicePolicyManager) ctxt.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int msgId;

        if (mgr.isActivePasswordSufficient()) {
            msgId = R.string.compliant;
        } else {
            msgId = R.string.not_compliant;
        }

        /*Toast.makeText(ctxt, msgId, Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onPasswordFailed(Context ctxt, Intent intent) {
       /* Toast.makeText(ctxt, R.string.password_failed, Toast.LENGTH_LONG)
                .show();*/
        Log.d("Hello", "onPasswordFailed");
        View v;

    /*    hourdifs(ctxt);*/

        if (isConnected(ctxt)) {
            /*String mapLink=fetchLocation(ctxt);*/
            String email = "";
            final String[] mapLink = {""};
            final Context context=ctxt;

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctxt);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    email = profile.getEmail().trim();
                }

                final String finalEmail = email;
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener( new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.

                                if (location != null) {
                                    // Logic to handle location object

                                    // Logic to handle location object
                                    Double latitude = location.getLatitude();
                                    Double longitude = location.getLongitude();
                                    mapLink[0] = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
                                    String body = "An Incorrect Password Attempt has been detected in your Device from this Location,\n";
                                    body=body.concat("\n"+ mapLink[0]+"\n\nRegards, \nFortron ");
                                    String subject = "Location update from fortron";
                                    JavaMailAPI javaMailAPI=new JavaMailAPI(context.getApplicationContext(), finalEmail,subject,body);
                                    javaMailAPI.execute();
                                }
                            }
                        });

            }
        }



    }


    @Override
    public void onPasswordSucceeded(Context ctxt, Intent intent) {
/*        Toast.makeText(ctxt, R.string.password_success, Toast.LENGTH_LONG)
                .show();*/
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

  /*  public String fetchLocation(Context ctxt){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctxt);
        String mapLink="";
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if (location != null) {
                            // Logic to handle location object

                            // Logic to handle location object
                            Double latitude = location.getLatitude();
                            Double longitude = location.getLongitude();
                            mapLink[0] = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;

                        }
                    }
                });

        return mapLink[0];

    }*/

 /*   private void hourdifs(Context ctxt) {

        SharedPreferences prefs = ctxt.getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");


        if (!prefs.getBoolean("firstTime", false)) {
            String date="31-Dec-1998 23:37:50";
            Date finaldate=null;
            try{
                finaldate = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            editor.putString("myDate", sdf.format(finaldate)).apply();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
        Date time1=null;
        try{
            time1= sdf.parse(prefs.getString("myDate", "defaultValue"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date time2 = Calendar.getInstance().getTime();
        long mils = time2.getTime() - time1.getTime();
        int minuteFor = (int) (mils / (1000 * 60) % 60);
        Log.d("TAG","minute difference is "+minuteFor);
    }*/

}
