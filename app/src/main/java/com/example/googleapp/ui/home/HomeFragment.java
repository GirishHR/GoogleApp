package com.example.googleapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.FragmentManager;

import com.example.googleapp.GraphActivity;
import com.example.googleapp.R;
import com.example.googleapp.Register;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private HomeViewModel homeViewModel;
    EditText height,weight;
    Button SbmtBtn;
    Spinner Genderdropdown;
    String SelectedGender;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



/*        Fragment fragment = new HomeFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();*/
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        height = root.findViewById(R.id.heighttext);
        weight = root.findViewById(R.id.WeightText);
        SbmtBtn = root.findViewById(R.id.SubmitButton);


        height.setFilters(new InputFilter[]{new PercentageInputFilter((float) 1.00, (float) 2.50)});
        weight.setFilters(new InputFilter[]{new InputFilterMinMax("1", "200")});
        //get the spinner from the xml.
        final Spinner dropdown = root.findViewById(R.id.Genderdropdown);
        //create a list of items for the spinner.
        String[] items = new String[]{"Select Your Gender","MALE", "FEMALE"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

      /*  final TextView textView = root.findViewById(R.id.graphButton);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        SbmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String heightValue = height.getText().toString();
                String WeightValue=weight.getText().toString();
                if ((heightValue.equals("")) || (WeightValue.equals("")) || (SelectedGender.equals("Select Your Gender")) ) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Kindly fill all the information", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Toast.makeText(v.getContext(), "Kindly fill all the information. ", Toast.LENGTH_LONG).show();

                    return;
                }

                Intent intent = new Intent(getActivity().getApplicationContext(), GraphActivity.class);
                intent.putExtra("Height",heightValue);
                intent.putExtra("Weight",WeightValue);
                intent.putExtra("Gender",SelectedGender);

                startActivity(intent);


            }
        });
        return root;


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SelectedGender = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*    @Override
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
private class PercentageInputFilter implements InputFilter {
    private float min;
    private float max;

    PercentageInputFilter(float min, float max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            // Get input
            String stringToMatch = dest.toString() + source.toString();
            float input = Float.parseFloat(stringToMatch);

            // Check if the input is in range.
            if (isInRange(min, max, input)) {
                // return null to accept the original replacement in case the format matches and text is in range.
                return null;
            }
        } catch (NumberFormatException nfe) {
        }

        Snackbar.make(getActivity().findViewById(android.R.id.content), "Height in meters is not between the range of World Record.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        Toast.makeText(getActivity(), "Height in meters is not in between the range of World Record ,can be calculated between 1 to 2.5 meter range.", Toast.LENGTH_LONG).show();
        return "";
    }

    private boolean isInRange(float min, float max, float input) {
        return input >= min && input <= max;
    }
}

    public class InputFilterMinMax implements InputFilter {
        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }

            Snackbar.make(getActivity().findViewById(android.R.id.content), "Entered Weight should be between 20 to 200", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Toast.makeText(getActivity(), "Weight can be calculated between 20Kg to 200Kg", Toast.LENGTH_LONG).show();
            return "";
        }

        private boolean isInRange(int a, int b, int c) {

            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

}
