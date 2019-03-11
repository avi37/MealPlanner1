package com.example.user.mealmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FoodNameSpinnerAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private String[] contentArray;
    private String[] imageArray;
    private String[] proteins;
    private String[] fats;
    private String[] carbs;
    private String[] calories;

    private String BASE_IMG_URL = "http://www.code-fuel.in/mealplanner/menu/thumb/";

    public FoodNameSpinnerAdapter(Context context, int resource, String[] objects, String[] imageArray, String[] proteins, String[] fats, String[] carbs, String[] calories) {
        super(context, R.layout.food_name_spinner_item_layout, R.id.sub_cat_spinner_tv_name, objects);

        this.ctx = context;
        this.contentArray = objects;
        this.imageArray = imageArray;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
        this.calories = calories;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.food_name_spinner_item_layout, parent, false);

        TextView tv_foodName = row.findViewById(R.id.sub_cat_spinner_tv_name);
        tv_foodName.setText(contentArray[position]);

        TextView tv_proteins = row.findViewById(R.id.sub_cat_spinner_tv_proteins);
        tv_proteins.setText("proteins: " + proteins[position]);

        TextView tv_fats = row.findViewById(R.id.sub_cat_spinner_tv_fats);
        tv_fats.setText("fats: " + fats[position]);

        TextView tv_carbs = row.findViewById(R.id.sub_cat_spinner_tv_carbs);
        tv_carbs.setText("carbs: " + carbs[position]);

        TextView tv_calories = row.findViewById(R.id.sub_cat_spinner_tv_calories);
        tv_calories.setText("calories: " + calories[position]);

        ImageView imageView = row.findViewById(R.id.sub_cat_spinner_iv_img);
        String img_uri = BASE_IMG_URL + (imageArray[position]);
        Glide.with(ctx).load(img_uri).into(imageView);


        return row;
    }


}