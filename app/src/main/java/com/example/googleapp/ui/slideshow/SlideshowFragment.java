package com.example.googleapp.ui.slideshow;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.googleapp.AdminReceiver;
import com.example.googleapp.Login;
import com.example.googleapp.R;

import static android.content.Context.DEVICE_POLICY_SERVICE;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    Button deletebutton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
/*        Fragment fragment = new SlideshowFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();*/
        final ComponentName cn = new ComponentName(getActivity(), AdminReceiver.class);
        final DevicePolicyManager mgr =
                (DevicePolicyManager) getActivity().getSystemService(DEVICE_POLICY_SERVICE);
        Context context=getContext();
        SharedPreferences prefs = context.getSharedPreferences("sharedPrefName", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();

        deletebutton = root.findViewById(R.id.deletebutton);

        deletebutton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mgr.removeActiveAdmin(cn);
                                                editor.putString("checked","false");
                                                editor.apply();
                                                buildDialog(getActivity()).show();
                                            }
                                        }

        );

        return root;
    }
/*
    @Override
    public void onResume() {
        super.onResume();

        // Remove comment  when you focus on edittext and then pressed back button

*//*        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    editText.clearFocus();
                }
                return false;
            }
        });*//*

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

    }*/
public AlertDialog.Builder buildDialog(Context c) {

    AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Deleted");
        builder.setMessage("Kindly make sure Safe Locator is off after this.\n"+
                "Then try to Uninstall.");

    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            return;
        }
    });

    return builder;
}

}
