package com.example.googleapp.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleapp.Adapter;
import com.example.googleapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    RecyclerView recyclerView;
    Adapter adapter;
    List<String> foodTitle,foodWater,foodEnergy,foodProtein,foodCarbs,foodCalcium,foodIron;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        foodTitle = new ArrayList<>();
        foodWater = new ArrayList<>();
        foodEnergy= new ArrayList<>();
        foodProtein=new ArrayList<>();
        foodCarbs = new ArrayList<>();
        foodCalcium=new ArrayList<>();
        foodIron=new ArrayList<>();
        recyclerView= root.findViewById(R.id.listOfData);



       readNutritionData();

        for(int i = 0; i < nutritionSamples.size(); i++) {
            Log.d("MyActivity","Printing Samples"+(nutritionSamples.get(i)).toString());
        }
        return root;
    }

    private List<NutritionSample>  nutritionSamples= new ArrayList<>();

    private void readNutritionData() {

            InputStream is= getResources().openRawResource(R.raw.nutrition);
        BufferedReader reader= new BufferedReader(new InputStreamReader( is, Charset.forName("UTF-8")));

        String line="" ;
        try{
            while ( (line =reader.readLine()) != null){

                Log.d("MyActivity","Line: "+line);
                String[] tokens=line.split(",");


                NutritionSample sample= new NutritionSample();
                sample.setFooItems(tokens[0]);
                foodTitle.add(tokens[0]);

                sample.setWater_g(tokens[1]);
                foodWater.add(tokens[1]);

                sample.setEnerg_kcal(tokens[2]);
                foodEnergy.add(tokens[2]);

                sample.setProtein_g(tokens[3]);
                foodProtein.add(tokens[3]);

                sample.setCarbs_g(tokens[4]);
                foodCarbs.add(tokens[4]);


                sample.setCalc_mg(tokens[5]);
                foodCalcium.add(tokens[5]);

                sample.setIron_mg(tokens[6]);
                foodIron.add(tokens[6]);

                nutritionSamples.add(sample);



            }
            showData();
        }catch(IOException e){
            Log.wtf("MyActivity","Error Reading the file on line"+ line,e);
            e.printStackTrace();
        }



    }

    private void showData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter(getActivity(),foodTitle,foodWater,foodEnergy,foodProtein,foodCarbs,foodCalcium,foodIron);
        recyclerView.setAdapter(adapter);
    }


}
