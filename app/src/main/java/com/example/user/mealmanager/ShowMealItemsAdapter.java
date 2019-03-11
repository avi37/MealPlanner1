package com.example.user.mealmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ShowMealItemsAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private String[] contentArray;
    private String[] thumbImageArray;
    private String[] realImageArray;

    private String BASE_REAL_IMG_URL = "http://www.code-fuel.in/mealplanner/menu/";
    private String BASE_THUMB_IMG_URL = "http://www.code-fuel.in/mealplanner/menu/thumb/";

    public ShowMealItemsAdapter(Context context, int resource, String[] objects, String[] _thumbImageArray, String[] _realImageArray) {
        super(context, R.layout.food_name_spinner_item_layout, R.id.sub_cat_spinner_tv_name, objects);

        this.ctx = context;
        this.contentArray = objects;
        this.thumbImageArray = _thumbImageArray;
        this.realImageArray = _realImageArray;
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

    private View getCustomView(final int position, final View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.show_meal_items_item, parent, false);

        TextView textView = row.findViewById(R.id.show_mealItems_item_tv_name);
        textView.setText(contentArray[position]);

        ImageView imageView = row.findViewById(R.id.show_mealItems_item_iv_thumbImg);
        String img_uri = BASE_THUMB_IMG_URL + (thumbImageArray[position]);
        Glide.with(ctx).load(img_uri).into(imageView);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRealItemImage(BASE_REAL_IMG_URL + realImageArray[position]);
            }
        });

        return row;
    }


    private void openRealItemImage(String url_realImage) {

        AlertDialog.Builder popupDialogBuilder = new AlertDialog.Builder(ctx);

        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.show_item_image, null);

        ImageView popupImv = view.findViewById(R.id.show_item_realImage);
        popupDialogBuilder.setView(view);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        Glide.with(ctx).setDefaultRequestOptions(requestOptions).load(url_realImage).into(popupImv);

        AlertDialog alertDialog = popupDialogBuilder.create();
        alertDialog.show();
    }

}
