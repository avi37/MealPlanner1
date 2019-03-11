package com.example.user.mealmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyRecipesListAdapter extends ArrayAdapter<String> {

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    private GetMealItemAPI getMealItemAPI;

    private Context mContext;

    private OpenMyRecipeListener openMyRecipeListener;

    private String[] arr_recipeId;
    private String[] arr_recipeName;
    private String[] arr_foodType;
    private String[] arr_mealItems;


    public MyRecipesListAdapter(Context context, int resource, String[] arr_recipeId, String[] arr_recipeName, String[] arr_foodType, String[] arr_mealItems) {
        super(context, R.layout.my_recipes_list_item);

        this.mContext = context;
        this.arr_recipeId = arr_recipeId;
        this.arr_recipeName = arr_recipeName;
        this.arr_foodType = arr_foodType;
        this.arr_mealItems = arr_mealItems;

        getMealItemAPI = getGetMealItemAPIService(BASE_URL);

        openMyRecipeListener = (OpenMyRecipeListener) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return arr_foodType.length;
    }

    private View getCustomView(final int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.my_recipes_list_item, parent, false);

        TextView recipe_name = row.findViewById(R.id.my_recipes_list_item_recipeName);
        recipe_name.setText(arr_recipeName[position]);

        TextView food_type = row.findViewById(R.id.my_recipes_list_item_foodType);
        food_type.setText(arr_foodType[position]);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openMyRecipeListener.openMyRecipeFragment(arr_mealItems[position]);
                Toast.makeText(mContext, arr_recipeName[position] + " selected", Toast.LENGTH_LONG).show();
            }
        });

        return row;
    }


    public interface OpenMyRecipeListener {
        void openMyRecipeFragment(String meal_items);
    }


//---------------------------------------- APIs -----------------------------------------------------//


    GetMealItemAPI getGetMealItemAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetMealItemAPI.class);
    }

    interface GetMealItemAPI {
        @POST("meal/get_meal_item_api/")
        @FormUrlEncoded
        Call<List<MyRecipesRes>> getMealItemDetails(@Field("meal_items") String meal_items);
    }


}

