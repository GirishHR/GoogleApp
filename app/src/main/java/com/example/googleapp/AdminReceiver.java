package com.example.googleapp;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/*
public class AdminReceiver  {
}
*/
public class AdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onPasswordFailed(android.content.Context context, android.content.Intent intent, android.os.UserHandle userHandle) {
        DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int no = mgr.getCurrentFailedPasswordAttempts();

        if (no >= 3) {
       /*     context.startActivity(new Intent(context, Login.class));*/
            Toast.makeText(context, R.string.password_failed, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
