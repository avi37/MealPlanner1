package com.example.user.mealmanager;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyRecipesActivity extends AppCompatActivity implements MyRecipesListAdapter.OpenMyRecipeListener {

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    SessionManager sessionManager;
    GetAllRecipesAPI getAllRecipesAPI;

    ListView listView_recipeList;
    ProgressBar progressBar;

    ArrayList<String> list_AllRecipes;
    MyRecipesListAdapter list_adapter;

    String u_id;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipes);

        context = MyRecipesActivity.this;

        sessionManager = new SessionManager(this);
        getAllRecipesAPI = getGetAllRecipesAPIService(BASE_URL);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listView_recipeList = findViewById(R.id.myRecipes_listView);
        progressBar = findViewById(R.id.myRecipes_progressBar);

        u_id = sessionManager.getUserid();

        list_AllRecipes = new ArrayList<>();

        getAllRecipes();

    }

    private void getAllRecipes() {
        progressBar.setVisibility(View.VISIBLE);

        getAllRecipesAPI.getAllRecipes(u_id).enqueue(new Callback<List<MyRecipesRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<MyRecipesRes>> call, @NonNull Response<List<MyRecipesRes>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        String[] recipe_id = new String[response.body().size()];
                        String[] recipe_name = new String[response.body().size()];
                        String[] foodType = new String[response.body().size()];
                        String[] mealItems = new String[response.body().size()];

                        for (int i = 0; i < response.body().size(); i++) {
                            recipe_id[i] = response.body().get(i).getId();
                            recipe_name[i] = response.body().get(i).getRecipe_name();
                            foodType[i] = response.body().get(i).getFood_type();
                            mealItems[i] = response.body().get(i).getMeal_items();
                        }

                        list_adapter = new MyRecipesListAdapter(MyRecipesActivity.this, R.layout.my_recipes_list_item, recipe_id, recipe_name, foodType, mealItems);
                        listView_recipeList.setAdapter(list_adapter);

                        progressBar.setVisibility(View.GONE);

                        list_adapter.notifyDataSetChanged();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MyRecipesRes>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Response onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @Override
    public void openMyRecipeFragment(String meal_items) {
        Bundle bundle = new Bundle();
        bundle.putString("str_mealItems", meal_items);

        MyRecipeItemFragment fragment = new MyRecipeItemFragment();
        fragment.setArguments(bundle);

        FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.add(R.id.fragment_my_recipe_item, fragment);
        transaction.addToBackStack("My_recipeList_activity_frame");
        transaction.commit();
    }
//----------------------------------------- APIs ----------------------------------------------//


    GetAllRecipesAPI getGetAllRecipesAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetAllRecipesAPI.class);
    }

    interface GetAllRecipesAPI {
        @POST("meal/get_all_meal_api/")
        @FormUrlEncoded
        Call<List<MyRecipesRes>> getAllRecipes(@Field("u_id") String u_id);
    }

}
