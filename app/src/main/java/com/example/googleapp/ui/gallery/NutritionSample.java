package com.example.googleapp.ui.gallery;

class NutritionSample {

    String fooItems;
    String Water_g;
    String Energ_kcal;
    String Protein_g;
    String Carbs_g;
    String Calc_mg;
    String Iron_mg;

    public String getFooItems() {
        return fooItems;
    }

    public void setFooItems(String fooItems) {
        this.fooItems = fooItems;
    }

    public String getWater_g() {
        return Water_g;
    }

    public void setWater_g(String water_g) {
        this.Water_g = water_g;
    }

    public String getEnerg_kcal() {
        return Energ_kcal;
    }

    public void setEnerg_kcal(String energ_kcal) {
        this.Energ_kcal = energ_kcal;
    }

    public String getProtein_g() {
        return Protein_g;
    }

    public void setProtein_g(String protein_g) {
        this.Protein_g = protein_g;
    }

    public String getCarbs_g() {
        return Carbs_g;
    }

    public void setCarbs_g(String carbs_g) {
        this.Carbs_g = carbs_g;
    }

    public String getCalc_mg() {
        return Calc_mg;
    }

    public void setCalc_mg(String calc_mg) {
        Calc_mg = calc_mg;
    }

    public String getIron_mg() {
        return Iron_mg;
    }

    public void setIron_mg(String iron_mg) {
        this.Iron_mg = iron_mg;
    }

    @Override
    public String toString() {
       /* return "NutritionSample{" +
                "fooItems='" + fooItems + '\'' +
                ", Water_g='" + Water_g + '\'' +
                ", Energ_kcal='" + Energ_kcal + '\'' +
                ", Protein_g='" + Protein_g + '\'' +
                ", Carbs_g='" + Carbs_g + '\'' +
                ", Calc_mg='" + Calc_mg + '\'' +
                ", Iron_mg='" + Iron_mg + '\'' +
                '}';*/
       return fooItems+","+Water_g+","+ Energ_kcal+","+ Protein_g+","+ Carbs_g+","+ Calc_mg+","+ Iron_mg;
    }
}
