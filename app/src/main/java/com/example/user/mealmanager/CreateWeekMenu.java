package com.example.user.mealmanager;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class CreateWeekMenu extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    SessionManager sessionManager;
    AddMealPref addMealPref;

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    private SelectMenuActivity.GetCreatedMealAPI getCreatedMealAPI;
    private SelectMenuActivity.GetCookNamesAPI getCookNames;
    //private SelectMenuActivity.AddMealAPI addMealAPI;
    private SelectMenuActivity.CategoryAPI categoryAPI;
    private SelectMenuActivity.SubCategoryAPI subCategoryAPI;

    Spinner spinner_mealType, spinner_foodType, spinner_cook, spinner_mealCategory, spinner_foodName;
    TextView textView_selectedTime, textView_todayDate, textView_todayDay;
    Button button_done;
    ImageButton add_button;
    ProgressBar progressBar;

    SimpleDateFormat simpledateformat;
    Date today_date;

    String food_type;
    List<String> list_cook_ids = new ArrayList<>();
    List<String> list_addedItemIds = new ArrayList<>();
    String[] foodName_ids;

    ListView added_list;
    MealTypeSpinnerAdapter adapter_mealType;
    ArrayAdapter<String> adapter_mealCategory;
    ArrayList<AddedItemModel> listItems;
    FoodNameSpinnerAdapter adapter_foodName;
    AddItemAdapter adapter_addItem;

    int selectedCategory;
    String[] food_category;
    String[] food_name = null, food_img = null, food_proteins = null, food_fats = null, food_carbs = null, food_calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_week_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        addMealPref = new AddMealPref(this);

        getCreatedMealAPI = getCreatedMealAPIService(BASE_URL);
        getCookNames = getCookNamesAPIService(BASE_URL);

        textView_todayDate = findViewById(R.id.create_weekly_menu_tv_todayDate);
        textView_todayDay = findViewById(R.id.create_weekly_menu_tv_todayDay);
        textView_selectedTime = findViewById(R.id.create_weekly_menu_tv_seletedTime);

        setSpinner_MealType();

        spinner_foodType = findViewById(R.id.create_weekly_menu_spinnerFoodType);
        spinner_cook = findViewById(R.id.create_weekly_menu_spinnerCookName);

        setTodayData();
        progressBar = findViewById(R.id.create_weekly_menu_progressBar);

        categoryAPI = getCategoryAPIService(BASE_URL);
        subCategoryAPI = getSubCategoryAPIService(BASE_URL);
        //addMealAPI = getAddMealAPIService(BASE_URL);

        spinner_mealCategory = findViewById(R.id.create_weekly_menu_spinnerMealCategory);
        spinner_foodName = findViewById(R.id.create_weekly_menu_spinnerFoodName);
        button_done = findViewById(R.id.create_weekly_menu_btn_done);
        added_list = findViewById(R.id.create_weekly_menu_listView_addedMeal);
        add_button = findViewById(R.id.create_weekly_menu_spinner_AddButton);

        initFoodCategorySpinner();

        spinner_mealType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTimeOfMeal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        final List<String> list_cook_names = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_cook_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_foodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                food_type = spinner_foodType.getSelectedItem().toString();

                progressBar.setVisibility(View.VISIBLE);

                list_cook_names.clear();
                list_cook_ids.clear();

                getCookNames.getNames(sessionManager.getUserid(), food_type).enqueue(new Callback<CookNamesRes>() {
                    @Override
                    public void onResponse(@NonNull Call<CookNamesRes> call, @NonNull Response<CookNamesRes> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                try {
                                    List<CookNamesResInn> cook_data = response.body().getData();
                                    for (int i = 0; i < cook_data.size(); i++) {
                                        list_cook_names.add(cook_data.get(i).getName());
                                        list_cook_ids.add(cook_data.get(i).getId());
                                    }

                                    spinner_cook.setAdapter(adapter);

                                    progressBar.setVisibility(View.INVISIBLE);

                                } catch (Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Json Exception\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Error in getting msg=true", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<CookNamesRes> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "API Request Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_cook.setOnItemSelectedListener(this);

        spinner_mealCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                progressBar.setVisibility(View.VISIBLE);

                selectedCategory = spinner_mealCategory.getSelectedItemPosition() + 1;

                String qqq = spinner_foodType.getSelectedItem().toString();

                subCategoryAPI.getSubCategory(String.valueOf(selectedCategory), qqq).enqueue(new Callback<List<SubCategoryRes>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<SubCategoryRes>> call, @NonNull Response<List<SubCategoryRes>> response) {

                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                try {
                                    List<SubCategoryRes> res = response.body();
                                    foodName_ids = new String[res.size()];
                                    food_name = new String[res.size()];
                                    food_proteins = new String[res.size()];
                                    food_fats = new String[res.size()];
                                    food_carbs = new String[res.size()];
                                    food_calories = new String[res.size()];
                                    food_img = new String[res.size()];

                                    for (int i = 0; i < res.size(); i++) {
                                        foodName_ids[i] = res.get(i).getId();
                                        food_name[i] = res.get(i).getName();
                                        food_proteins[i] = res.get(i).getProteins();
                                        food_fats[i] = res.get(i).getFats();
                                        food_carbs[i] = res.get(i).getCarbs();
                                        food_calories[i] = res.get(i).getCalories();
                                        food_img[i] = res.get(i).getThumb_url();
                                    }

                                    String _foodId = "", _foodName = "", _foodThumbImages = "";

                                    for (String s : foodName_ids) {

                                        _foodId += s + ",";
                                    }
                                    for (String s : food_name) {
                                        _foodName += s + ",";
                                    }
                                    for (String s : food_img) {
                                        _foodThumbImages += s + ",";
                                    }

                                    Log.d("Your String arr 1: ", _foodName);
                                    Log.d("Your String arr 2: ", _foodThumbImages);

                                    adapter_foodName = new FoodNameSpinnerAdapter(getApplicationContext(), R.layout.food_name_spinner_item_layout, food_name, food_img, food_proteins, food_fats, food_carbs, food_calories);
                                    spinner_foodName.setAdapter(adapter_foodName);

                                    progressBar.setVisibility(View.INVISIBLE);

                                    adapter_foodName.notifyDataSetChanged();

                                    add_button.setVisibility(View.VISIBLE);

                                } catch (Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Error in getting --> msg=true", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SubCategoryRes>> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "No items available ", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_foodName.setOnItemSelectedListener(this);

        listItems = new ArrayList<>();
        adapter_addItem = new AddItemAdapter(this, R.layout.added_meal_item, listItems);
        added_list.setAdapter(adapter_addItem);


        button_done.setOnClickListener(this);
        add_button.setOnClickListener(this);

        textView_selectedTime.setOnClickListener(this);


    }


    private void setSpinner_MealType() {
        spinner_mealType = findViewById(R.id.create_weekly_menu_spinnerMealType);

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
                        String[] arr_notCreatedMeals = new String[a.length - arr_createdMeals.length];


                        for (int i = 0; i < res.size(); i++) {
                            arr_createdMeals[i] = res.get(i).getMeal_type();
                        }
                        m = arr_createdMeals.length;
                        int k = 0;

                        for (int i = 0; i < n; i++) {
                            int j;

                            for (j = 0; j < m; j++) {
                                if (a[i].equals(arr_createdMeals[j])) {
                                    break;
                                }
                            }

                            if (j == m) {
                                arr_notCreatedMeals[k] = a[i];
                                k++;
                            }

                        }

                        for (int i = 0; i < arr_notCreatedMeals.length; i++) {
                            arr_notCreatedMeals[i] = "Meal " + arr_notCreatedMeals[i];
                        }

                        adapter_mealType = new MealTypeSpinnerAdapter(getApplicationContext(), R.layout.meal_type_spinner_item, arr_notCreatedMeals);
                        spinner_mealType.setAdapter(adapter_mealType);

                    } else {
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<MyRes>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Response on failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setTodayData() {
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        today_date = new Date();
        textView_todayDate.setText(simpledateformat.format(today_date));

        simpledateformat = new SimpleDateFormat("EEEE");
        String dayOfWeek = simpledateformat.format(today_date);
        textView_todayDay.setText(dayOfWeek);
    }

    private void initFoodCategorySpinner() {

        progressBar.setVisibility(View.VISIBLE);

        categoryAPI.getCategory().enqueue(new Callback<List<CategoryRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryRes>> call, @NonNull Response<List<CategoryRes>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            List<CategoryRes> res = response.body();
                            food_category = new String[res.size()];

                            for (int i = 0; i < res.size(); i++) {
                                food_category[i] = res.get(i).getName();
                            }

                            adapter_mealCategory = new ArrayAdapter<>(CreateWeekMenu.this, android.R.layout.simple_spinner_item, food_category);
                            adapter_mealCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_mealCategory.setAdapter(adapter_mealCategory);

                            adapter_mealCategory.notifyDataSetChanged();

                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Error in getting msg=true", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryRes>> call, @NonNull Throwable t) {

            }
        });


    }

    private void addToList() {
        listItems.add(new AddedItemModel(spinner_foodName.getSelectedItem().toString()));
        list_addedItemIds.add(foodName_ids[spinner_foodName.getSelectedItemPosition()]);
        adapter_addItem.notifyDataSetChanged();
    }

    private void showConfirmDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.submit_meal_dialog, null);
        final EditText etNoP = alertLayout.findViewById(R.id.submit_meal_et_nop);
        etNoP.setText("1");
        etNoP.setSelection(etNoP.getText().length());
        final EditText etMealName = alertLayout.findViewById(R.id.submit_meal_recipe_name);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm your meal");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Confirmation canceled", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nOp = etNoP.getText().toString(), recipe_name = etMealName.getText().toString();

                if (!nOp.equals("")) {

                    if (recipe_name.equals("")) {
                        recipe_name = "#";
                    }

                    addDataToPref(nOp, recipe_name);

                    Log.d("arrayyyyy", Arrays.toString(adapter_addItem.getAllMealItems()));

                } else {
                    etNoP.setError("Required");
                    etNoP.requestFocus();
                }
            }
        });
        alert.setCancelable(false);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void createMealApiResquest() {
        String _u_id, _date, _time, _mealType, _foodType, _cookID, _nop, _recipe_name;
        //ArrayList<String> _mealItems = new ArrayList<>();

        _u_id = addMealPref.getKeyUId();
        _date = addMealPref.getKeyDate();
        _time = addMealPref.getKeyTime();
        _mealType = addMealPref.getKeyMealType();
        _foodType = addMealPref.getKeyFoodType();
        _cookID = addMealPref.getKeyId();
        _nop = addMealPref.getKeyNop();
        _recipe_name = addMealPref.getKeyRecipeName();

        //_mealItems = adapter_addItem.copyMealItems();
        String listString = "";

        /*for (String s : _mealItems) {
            listString += s + ",";
        }*/

        for (String s : list_addedItemIds) {
            listString += s + ",";
        }

        //Log.d("iiihhaaaa", listString);

        /*addMealAPI.addMeal("_new", _u_id, _date, _time, _mealType, _foodType, _cookID, _nop, _recipe_name, listString).enqueue(new Callback<MyRes>() {
            @Override
            public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().getMsg().equalsIgnoreCase("true")) {
                            Toast.makeText(getApplicationContext(), "Your meal is successfully created", Toast.LENGTH_SHORT).show();
                            finish();

                        } else if (response.body().getMsg().equals("xxx")) {
                            int m = Integer.parseInt(addMealPref.getKeyMealType()) + 1;
                            Toast.makeText(getApplicationContext(), "Meal-" + addMealPref.getKeyMealType() + " is already created for today \nPlease create other meal", Toast.LENGTH_SHORT).show();

                        } else if (response.body().getMsg().equals("yyy")) {
                            Toast.makeText(getApplicationContext(), "This recipe is already saved. \nPlease add it from Repeat Meal option", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid response", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyRes> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "API request failed\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    private void addDataToPref(String _nop, String _recipe_name) {
        String _u_id, _date, _time, _mealType, _foodType, _cookID;

        _u_id = sessionManager.getUserid();
        _date = textView_todayDate.getText().toString();
        _time = textView_selectedTime.getText().toString();
        _mealType = spinner_mealType.getSelectedItem().toString().replace("Meal ", "");//String.valueOf(spinner_mealType.getSelectedItemPosition() + 1);
        _foodType = spinner_foodType.getSelectedItem().toString();
        _cookID = list_cook_ids.get(spinner_cook.getSelectedItemPosition());

        addMealPref.addVP1Data(_u_id, _date, _time, _mealType, _foodType, _cookID, spinner_cook.getSelectedItem().toString());
        addMealPref.addVP2Data(_nop, _recipe_name);

        createMealApiResquest();
    }

    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView_selectedTime.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    void setTimeOfMeal() {
        switch (spinner_mealType.getSelectedItem().toString()) {
            case "Meal 1":
                textView_selectedTime.setText("6:30");
                break;

            case "Meal 2":
                textView_selectedTime.setText("9:00");
                break;

            case "Meal 3":
                textView_selectedTime.setText("12:00");
                break;

            case "Meal 4":
                textView_selectedTime.setText("15:00");
                break;

            case "Meal 5":
                textView_selectedTime.setText("18:00");
                break;

            case "Meal 6":
                textView_selectedTime.setText("21:00");
                break;

            case "Meal 7":
                textView_selectedTime.setText("23:00");
                break;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_menu_tv_seletedTime:
                showTimePicker();
                break;

            case R.id.create_menu_btn_done:
                showConfirmDialog();
                break;

            case R.id.create_menu_buttonAddItem:
                addToList();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        addMealPref.deleteAddMealPref();
    }

// ------------------------------ APIs ------------------------------------------------------------//


    SelectMenuActivity.GetCookNamesAPI getCookNamesAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SelectMenuActivity.GetCookNamesAPI.class);
    }

    interface GetCookNamesAPI {
        @POST("cook/get_cook_api/")
        @FormUrlEncoded
        Call<CookNamesRes> getNames(@Field("u_id") String u_id,
                                    @Field("cook_type") String cook_type
        );
    }


    SelectMenuActivity.GetCreatedMealAPI getCreatedMealAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SelectMenuActivity.GetCreatedMealAPI.class);
    }

    interface GetCreatedMealAPI {
        @POST("meal/get_meal_type_api2/")
        @FormUrlEncoded
        Call<List<MyRes>> getCreatedMeal(@Field("u_id") String u_id,
                                         @Field("c_id") String c_id);
    }


    /*SelectMenuActivity.AddMealAPI getAddMealAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SelectMenuActivity.AddMealAPI.class);
    }

    interface AddMealAPI {
        @POST("meal/add_meal_api/")
        @FormUrlEncoded
        Call<MyRes> addMeal(@Field("content") String content,
                            @Field("u_id") String u_id,
                            @Field("date") String date,
                            @Field("time") String time,
                            @Field("mealType") String mealType,
                            @Field("foodType") String foodType,
                            @Field("id") String id,
                            @Field("noOfPersons") String noOfPersons,
                            @Field("recipeName") String recipeName,
                            @Field("mealItems") String mealItems
        );
    }*/

    SelectMenuActivity.CategoryAPI getCategoryAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SelectMenuActivity.CategoryAPI.class);
    }

    interface CategoryAPI {
        @POST("meal/category_api/")
        Call<List<CategoryRes>> getCategory();
    }


    SelectMenuActivity.SubCategoryAPI getSubCategoryAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(SelectMenuActivity.SubCategoryAPI.class);
    }

    interface SubCategoryAPI {
        @POST("meal/subcategory_api/")
        @FormUrlEncoded
        Call<List<SubCategoryRes>> getSubCategory(@Field("catId") String catId,
                                                  @Field("foodType") String foodType
        );
    }

}
