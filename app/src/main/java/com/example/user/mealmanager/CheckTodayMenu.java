package com.example.user.mealmanager;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class CheckTodayMenu extends AppCompatActivity implements View.OnClickListener {

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    private GetCreatedMealAPI getCreatedMealAPI;
    SessionManager sessionManager;

    ShowTodayMenuCat fragment;
    FragmentManager manager;
    FragmentTransaction transaction;

    FrameLayout main_frame, fake_frame;
    TextView textView_date;
    CardView cardView_meal_1, cardView_meal_2, cardView_meal_3, cardView_meal_4, cardView_meal_5, cardView_meal_6, cardView_meal_7;
    TextView textView_meal1_time, textView_meal2_time, textView_meal3_time, textView_meal4_time, textView_meal5_time, textView_meal6_time, textView_meal7_time;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        getCreatedMealAPI = getCreatedMealAPIService(BASE_URL);

        textView_date = findViewById(R.id.todayMenu_tv_date);

        main_frame = findViewById(R.id.frame_show_today_menu_cat);
        fake_frame = findViewById(R.id.frame_show_today_menu_cat_noMeals);
        cardView_meal_1 = findViewById(R.id.todayMenu_card_meal1);
        cardView_meal_2 = findViewById(R.id.todayMenu_card_meal2);
        cardView_meal_3 = findViewById(R.id.todayMenu_card_meal3);
        cardView_meal_4 = findViewById(R.id.todayMenu_card_meal4);
        cardView_meal_5 = findViewById(R.id.todayMenu_card_meal5);
        cardView_meal_6 = findViewById(R.id.todayMenu_card_meal6);
        cardView_meal_7 = findViewById(R.id.todayMenu_card_meal7);

        textView_meal1_time = findViewById(R.id.todayMenu_card1_tv_time);
        textView_meal2_time = findViewById(R.id.todayMenu_card2_tv_time);
        textView_meal3_time = findViewById(R.id.todayMenu_card3_tv_time);
        textView_meal4_time = findViewById(R.id.todayMenu_card4_tv_time);
        textView_meal5_time = findViewById(R.id.todayMenu_card5_tv_time);
        textView_meal6_time = findViewById(R.id.todayMenu_card6_tv_time);
        textView_meal7_time = findViewById(R.id.todayMenu_card7_tv_time);

        progressBar = findViewById(R.id.today_menu_prgressBar);

        setTodayDate();
        highlightCreatedMeals();

        cardView_meal_1.setOnClickListener(this);
        cardView_meal_2.setOnClickListener(this);
        cardView_meal_3.setOnClickListener(this);
        cardView_meal_4.setOnClickListener(this);
        cardView_meal_5.setOnClickListener(this);
        cardView_meal_6.setOnClickListener(this);
        cardView_meal_7.setOnClickListener(this);

    }


    private void highlightCreatedMeals() {
        progressBar.setVisibility(View.VISIBLE);

        getCreatedMealAPI.getCreatedMeal(sessionManager.getUserid(), sessionManager.getCId()).enqueue(new Callback<List<MyRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<MyRes>> call, @NonNull Response<List<MyRes>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<MyRes> res = response.body();

                        int n, m;
                        String[] a = new String[]{"1", "2", "3", "4", "5", "6", "7"};
                        n = a.length;
                        String[] arr_createdMeals = new String[res.size()];
                        String[] arr_timeOfMeals = new String[res.size()];

                        for (int i = 0; i < res.size(); i++) {
                            arr_createdMeals[i] = res.get(i).getMeal_type();
                            arr_timeOfMeals[i] = res.get(i).getTime();
                        }
                        m = arr_createdMeals.length;

                        for (int i = 0; i < n; i++) {
                            int j;

                            for (j = 0; j < m; j++) {
                                if (a[i].equals(arr_createdMeals[j])) {
                                    break;
                                }
                            }

                            if (j == m) {
                                setCardSelectionBG(a[i]);
                            }
                        }

                        setMealTimes(arr_createdMeals, arr_timeOfMeals);

                        if (arr_createdMeals.length != 0) {
                            main_frame.setVisibility(View.VISIBLE);
                            fake_frame.setVisibility(View.INVISIBLE);
                        }
                        progressBar.setVisibility(View.INVISIBLE);

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MyRes>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Response on failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setTodayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        textView_date.setText(formatter.format(date));
    }

    private void setCardSelectionBG(String notSelectedMeal) {
        switch (notSelectedMeal) {
            case "1":
                cardView_meal_1.setVisibility(View.GONE);
                break;

            case "2":
                cardView_meal_2.setVisibility(View.GONE);
                break;

            case "3":
                cardView_meal_3.setVisibility(View.GONE);
                break;

            case "4":
                cardView_meal_4.setVisibility(View.GONE);
                break;

            case "5":
                cardView_meal_5.setVisibility(View.GONE);
                break;

            case "6":
                cardView_meal_6.setVisibility(View.GONE);
                break;

            case "7":
                cardView_meal_7.setVisibility(View.GONE);
                break;

        }

    }

    private void setMealTimes(String[] arr_createdMeals, String[] arr_timeOfMeals) {

        for (int i = 0; i < arr_createdMeals.length; i++) {
            switch (arr_createdMeals[i]) {
                case "1":
                    textView_meal1_time.setText("Time: " + arr_timeOfMeals[i]);
                    break;

                case "2":
                    textView_meal2_time.setText("Time: " + arr_timeOfMeals[i]);
                    break;

                case "3":
                    textView_meal3_time.setText("Time: " + arr_timeOfMeals[i]);
                    break;

                case "4":
                    textView_meal4_time.setText("Time: " + arr_timeOfMeals[i]);
                    break;

                case "5":
                    textView_meal5_time.setText("Time: " + arr_timeOfMeals[i]);
                    break;

                case "6":
                    textView_meal6_time.setText("Time: " + arr_timeOfMeals[i]);
                    break;

                case "7":
                    textView_meal7_time.setText("Time: " + arr_timeOfMeals[i]);
                    break;

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.todayMenu_card_meal1:
                showCategoryMenu("Meal 1");
                break;


            case R.id.todayMenu_card_meal2:
                showCategoryMenu("Meal 2");
                break;


            case R.id.todayMenu_card_meal3:
                showCategoryMenu("Meal 3");
                break;


            case R.id.todayMenu_card_meal4:
                showCategoryMenu("Meal 4");
                break;


            case R.id.todayMenu_card_meal5:
                showCategoryMenu("Meal 5");
                break;


            case R.id.todayMenu_card_meal6:
                showCategoryMenu("Meal 6");
                break;


            case R.id.todayMenu_card_meal7:
                showCategoryMenu("Meal 7");
                break;

        }

    }


    void showCategoryMenu(String selectedCategory) {
        Bundle bundle = new Bundle();
        bundle.putString("category", selectedCategory);

        fragment = new ShowTodayMenuCat();
        fragment.setArguments(bundle);

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.frame_show_today_menu_cat, fragment);
        transaction.addToBackStack("main_act_frame");

        disableAllCards();
        transaction.commit();
    }

    private void disableAllCards() {
        cardView_meal_1.setClickable(false);
        cardView_meal_2.setClickable(false);
        cardView_meal_3.setClickable(false);
        cardView_meal_4.setClickable(false);
        cardView_meal_5.setClickable(false);
        cardView_meal_6.setClickable(false);
        cardView_meal_7.setClickable(false);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        cardView_meal_1.setClickable(true);
        cardView_meal_2.setClickable(true);
        cardView_meal_3.setClickable(true);
        cardView_meal_4.setClickable(true);
        cardView_meal_5.setClickable(true);
        cardView_meal_6.setClickable(true);
        cardView_meal_7.setClickable(true);

        /*overridePendingTransition(
                0,
                R.anim.exit_to_left
        );*/
    }


    //------------------------------------- APIs ----------------------------------------------//


    GetCreatedMealAPI getCreatedMealAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetCreatedMealAPI.class);
    }

    interface GetCreatedMealAPI {
        @POST("meal/get_meal_type_api2/")
        @FormUrlEncoded
        Call<List<MyRes>> getCreatedMeal(@Field("u_id") String u_id,
                                         @Field("c_id") String c_id
        );
    }


}
