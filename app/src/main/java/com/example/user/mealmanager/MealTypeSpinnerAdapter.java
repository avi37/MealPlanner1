package com.example.user.mealmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MealTypeSpinnerAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private String[] arr_mealType;

    public MealTypeSpinnerAdapter(@NonNull Context context, int resource, String[] arr_mealType) {
        super(context, resource, arr_mealType);

        this.ctx = context;
        this.arr_mealType = arr_mealType;
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
        View row = inflater.inflate(R.layout.meal_type_spinner_item, parent, false);

        TextView textView = row.findViewById(R.id.mealType_spinner_item_tv_name);
        textView.setText(arr_mealType[position]);

        return row;
    }


}
