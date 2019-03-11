package com.example.user.mealmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ShowTodayMenuCat extends Fragment implements View.OnClickListener {

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    GetTodayMenuAPI getTodayMenuAPI;
    SetCounterAPI setCounterAPI;

    SessionManager sessionManager;

    View view;

    TextView textView_mealType;
    TextView textView_recipeName, textView_time, textView_foodType, textView_nop, textView_cookName;
    TextView textView_proteins, textView_fats, textView_carbs, textView_calories;
    FloatingActionButton fab_editMeal;
    ProgressBar progressBar;
    LinearLayout ll_cook_details, ll_nutrition_info;
    ListView listView_mealItems;

    String category, catID, mealType;
    Double proteins = 0.0, carbs = 0.0, fats = 0.0, calories = 0.0;

    ShowMealItemsAdapter showMealItemsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_show_today_menu_cat, container, false);

        sessionManager = new SessionManager(getActivity());
        getTodayMenuAPI = getTodayMenuAPIService(BASE_URL);
        setCounterAPI = getSetCounterAPIService(BASE_URL);

        textView_mealType = view.findViewById(R.id.today_menu_cat_tv_mealType);
        textView_recipeName = view.findViewById(R.id.today_menu_cat_tv_recipeName);
        textView_time = view.findViewById(R.id.today_menu_cat_tv_time);
        textView_foodType = view.findViewById(R.id.today_menu_cat_tv_foodType);
        textView_nop = view.findViewById(R.id.today_menu_cat_tv_nop);
        textView_cookName = view.findViewById(R.id.today_menu_cat_tv_cookName);
        fab_editMeal = view.findViewById(R.id.today_menu_cat_fab_editMeal);
        progressBar = view.findViewById(R.id.today_menu_cat_progressBar);
        ll_cook_details = view.findViewById(R.id.today_menu_cat_ll_cook_details);
        listView_mealItems = view.findViewById(R.id.today_menu_cat_listView_items);

        ll_nutrition_info = view.findViewById(R.id.today_menu_cat_ll_nutrition_info);
        textView_proteins = view.findViewById(R.id.today_menu_cat_tv_total_proteins);
        textView_fats = view.findViewById(R.id.today_menu_cat_tv_total_fats);
        textView_carbs = view.findViewById(R.id.today_menu_cat_tv_total_carbs);
        textView_calories = view.findViewById(R.id.today_menu_cat_tv_total_calories);

        category = getArguments().getString("category");
        mealType = category.replace("Meal ", "");

        catID = category.substring(category.length() - 1);

        progressBar.setVisibility(View.VISIBLE);

        textView_mealType.setText(category);


        getTodayMenuAPI.getTodayMenu(sessionManager.getUserid(), sessionManager.getCId(), catID).enqueue(new Callback<List<GetMealTypeRes>>() {

            @Override
            public void onResponse(@NonNull Call<List<GetMealTypeRes>> call, @NonNull Response<List<GetMealTypeRes>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!(response.body().get(0).getTime().equals(""))) {

                            GetMealTypeRes res = response.body().get(0);

                            String recipe_name, time, foodType, nop, cookName;
                            List<MealItemsRes> mealItems;

                            recipe_name = res.getRecipe_name();
                            time = res.getTime();
                            foodType = res.getFood_type();
                            nop = res.getNop();
                            cookName = res.getName();
                            mealItems = res.getMealItems();

                            String[] item_name = new String[mealItems.size()];
                            String[] item_thumbImg = new String[mealItems.size()];
                            String[] item_realImg = new String[mealItems.size()];

                            for (int i = 0; i < mealItems.size(); i++) {
                                item_name[i] = mealItems.get(i).getName();
                                item_thumbImg[i] = mealItems.get(i).getThumbUrl();
                                item_realImg[i] = mealItems.get(i).getImgUrl();

                                proteins += Double.parseDouble(mealItems.get(i).getProteins());
                                fats += Double.parseDouble(mealItems.get(i).getFats());
                                carbs += Double.parseDouble(mealItems.get(i).getCarbs());
                                calories += Double.parseDouble(mealItems.get(i).getCalories());
                            }

                            textView_recipeName.setText(recipe_name);
                            textView_time.setText(time);
                            textView_foodType.setText(foodType);
                            textView_nop.setText(nop);
                            if (sessionManager.getUserid().equals("xx")) {
                                ll_cook_details.setVisibility(View.GONE);
                                ll_nutrition_info.setVisibility(View.GONE);
                                fab_editMeal.setVisibility(View.GONE);
                            } else {
                                textView_cookName.setText(cookName);
                                textView_proteins.setText(String.valueOf(proteins));
                                textView_fats.setText(String.valueOf(fats));
                                textView_carbs.setText(String.valueOf(carbs));
                                textView_calories.setText(String.valueOf(calories));
                            }


                            showMealItemsAdapter = new ShowMealItemsAdapter(getActivity(), R.layout.show_meal_items_item, item_name, item_thumbImg, item_realImg);
                            listView_mealItems.setAdapter(showMealItemsAdapter);

                            showMealItemsAdapter.notifyDataSetChanged();

                            if (sessionManager.getUserid().equals("xx")) {
                                setNotification();
                            }

                            progressBar.setVisibility(View.INVISIBLE);

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(getActivity(), "getting some other response", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<GetMealTypeRes>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getActivity(), "onFailure while showing meal", Toast.LENGTH_SHORT).show();
            }
        });


        fab_editMeal.setOnClickListener(this);

        return view;
    }

    private void setNotification() {

        setCounterAPI.setMealCounter(sessionManager.getCId(), mealType).enqueue(new Callback<MyRes>() {
            @Override
            public void onResponse(@NonNull Call<MyRes> call, @NonNull Response<MyRes> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String res = response.body().getMsg();

                        switch (res) {
                            case "true":
                                Toast.makeText(getActivity(), "Counter for Meal " + mealType + " set", Toast.LENGTH_SHORT).show();
                                break;

                            case "seen":
                                Toast.makeText(getActivity(), "You already saw this meal", Toast.LENGTH_SHORT).show();
                                break;

                            case "msg":
                                Toast.makeText(getActivity(), "MSG got", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(getActivity(), "Unknown reply", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(getActivity(), "response body null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyRes> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Set Counter onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today_menu_cat_fab_editMeal:
                editCurrentMeal();
                break;
        }
    }

    private void editCurrentMeal() {
        Toast.makeText(getActivity(), "Edit Current Meal Action", Toast.LENGTH_SHORT).show();
    }



//------------------------------ APIs -----------------------------------------------------------------//


    GetTodayMenuAPI getTodayMenuAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetTodayMenuAPI.class);
    }

    interface GetTodayMenuAPI {
        @POST("meal/get_today_menu_api/")
        @FormUrlEncoded
        Call<List<GetMealTypeRes>>
        getTodayMenu(@Field("u_id") String u_id,
                     @Field("c_id") String c_id,
                     @Field("mealType") String mealType
        );
    }


    SetCounterAPI getSetCounterAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SetCounterAPI.class);
    }

    interface SetCounterAPI {
        @POST("meal/set_counter_api/")
        @FormUrlEncoded
        Call<MyRes>
        setMealCounter(@Field("c_id") String c_id,
                       @Field("mealType") String mealType
        );
    }

}
