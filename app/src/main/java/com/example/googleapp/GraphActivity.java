 package com.example.googleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;

 public class GraphActivity extends AppCompatActivity {

    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        final String[] Inches= new String[43];
        final int[] Men= new int[44];
        final float[] Meters= new float[44];
        final int[] Women= new int[44];
        final LineChart[] mpLineChart = new LineChart[1];
        final int[] size = {0};
        final int[] UserWeight= new int[2];
        final float[] UserHeight= new float[2];

        Intent intent = getIntent();
        final float HeightValue = Float.parseFloat(intent.getStringExtra("Height"));
        final int WeightValue = Integer.parseInt(intent.getStringExtra("Weight"));
        final String SelectedGender = intent.getStringExtra ("Gender");
        Men[0]=0;
        Meters[0]=0;
        Women[0]=0;
        UserWeight[0]=0;
        UserHeight[0]=0;
        UserWeight[1]=WeightValue;
        UserHeight[1]=HeightValue;




        for(int j=0;j<=42;j++){
        reff= FirebaseDatabase.getInstance().getReference().child(Integer.toString(j));
     /*       final int finalI = i;*/
            final int finalJ = j;

            reff.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i=0;

               /* Inches[finalI]=dataSnapshot.child("Inches").getValue().toString();
                String h="hai";*/

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    switch(i) {

                        case 1:
                            Inches[finalJ]=snapshot.getValue().toString();
                            size[0] = size[0] +1;
                            break;
                        case 2:
                            Men[finalJ+1]=Integer.parseInt(snapshot.getValue().toString());
                            break;
                        case 3:
                            Meters[finalJ+1]=Float.parseFloat(snapshot.getValue().toString());
                            break;
                        case 4:
                            Women[finalJ+1]=Integer.parseInt(snapshot.getValue().toString());
                            break;
                    }

                   /* Inches[0]=snapshot.getValue().toString();*/
                }
                if (size[0]==43){
                    mpLineChart[0] =  (LineChart)findViewById(R.id.lineChart);
                    LineDataSet lineDataSet1= new LineDataSet(dataValues1(Men,Meters),"Men Weight(in Kg) v/s Height(in m)");
                    LineDataSet lineDataSet2= new LineDataSet(dataValues2(Women,Meters),"Women Weight(in Kg) v/s Height(in m)");
                    LineDataSet lineDataSet3= new LineDataSet(dataValues3(UserWeight,UserHeight),"Your Weight(in Kg) v/s Height(in m)");
                    lineDataSet1.setDrawValues(false);
                    lineDataSet1.setColor(Color.BLACK);
                    lineDataSet1.setLineWidth(3);
                    lineDataSet1.setCircleColor(Color.BLACK);

                    lineDataSet3.setColor(Color.RED);
                    lineDataSet3.setLineWidth(3);
                    lineDataSet3.setCircleColor(Color.RED);

                    lineDataSet2.setDrawValues(false);
                    lineDataSet2.setColor(Color.BLUE);
                    lineDataSet2.setCircleColor(Color.BLUE);
                    lineDataSet2.setLineWidth(3);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet1);
                    dataSets.add(lineDataSet2);
                    dataSets.add(lineDataSet3);



                    mpLineChart[0].setDrawGridBackground(true);
                    mpLineChart[0].setDrawBorders(true);
                    mpLineChart[0].setBorderColor(Color.BLACK);
                    mpLineChart[0].setBorderWidth(2);
                    mpLineChart[0].getLegend().setWordWrapEnabled(true);
                    mpLineChart[0].getDescription().setText("IDEAL BODY CHART");
                    mpLineChart[0].getDescription().setTextSize(16f);
                    LineData data = new LineData(dataSets);
                    mpLineChart[0].setData(data);
                    mpLineChart[0].invalidate();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       }





    }

    private ArrayList<Entry> dataValues1(int[] men, float[] meters)
    {
        ArrayList<Entry> dataValsMen = new ArrayList<Entry>();

        for (int i=0;i<=43;i++){
            dataValsMen.add(new Entry( men[i], meters[i] ));

        }

        return dataValsMen;
    }
     private ArrayList<Entry> dataValues2(int[] women, float[] meters)
     {
         ArrayList<Entry> dataValsWomen = new ArrayList<Entry>();

         for (int i=0;i<=43;i++){
             dataValsWomen.add(new Entry( women[i], meters[i] ));

         }
         return dataValsWomen;
     }

     private ArrayList<Entry> dataValues3(int[] userweight, float[] userheight)
     {
         ArrayList<Entry> dataValsWomen = new ArrayList<Entry>();

         for (int i=0;i<=1;i++){
             dataValsWomen.add(new Entry( userweight[i], userheight[i] ));

         }
         return dataValsWomen;
     }


}
