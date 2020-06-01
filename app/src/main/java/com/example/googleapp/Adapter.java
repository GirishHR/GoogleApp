package com.example.googleapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<String> foodTitle,foodWater,foodEnergy,foodProtein,foodCarbs,foodCalcium,foodIron;
    private LayoutInflater inflater;

    public Adapter(Context context,List<String> foodTitle,List<String> foodWater,List<String> foodEnergy,List<String>  foodProtein,List<String> foodCarbs,List<String> foodCalcium,List<String> foodIron){

        this.foodTitle = foodTitle;
        this.foodWater = foodWater;
        this.foodEnergy=foodEnergy;
        this.foodProtein=foodProtein;
        this.foodCarbs=foodCarbs;
        this.foodCalcium=foodCalcium;
        this.foodIron=foodIron;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = foodTitle.get(position);
        String water = foodWater.get(position);
        String energy=foodEnergy.get(position);
        String protein= foodProtein.get(position);
        String carbs=foodCarbs.get(position);
        String calcium=foodCalcium.get(position);
        String iron=foodIron.get(position);

/*        Picasso.get().load(images.get(position)).placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                //.resize(75,75)
                .fit()
                .centerCrop()
                .into(holder.thumbnail);*/
        holder.item.setText(item);
        holder.water.setText("Water:"+water+"g");
        holder.energy.setText("Energy:"+energy+"kcal");
        holder.protein.setText("Protein:"+protein+"g");
        holder.carbs.setText("Carbs:"+carbs+"g");
        holder.calcium.setText("Calcium:"+calcium+"mg");
        holder.iron.setText("Iron:"+iron+"mg");

    }

    @Override
    public int getItemCount() {
        return foodTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item,water,energy,protein,carbs,calcium,iron;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.foodtitle);
            water = itemView.findViewById(R.id.description);
            energy=itemView.findViewById(R.id.energy);
            protein=itemView.findViewById(R.id.protein);
            carbs=itemView.findViewById(R.id.carbs);
            calcium=itemView.findViewById(R.id.calcium);
            iron=itemView.findViewById(R.id.iron);

        }
    }
}
