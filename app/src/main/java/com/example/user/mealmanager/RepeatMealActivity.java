package com.example.user.mealmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RepeatMealActivity extends AppCompatActivity implements View.OnClickListener {

    SessionManager sessionManager;

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    GetRecipeNameAPI getRecipeNameAPI;
    GetLastMealDetailsAPI getLastMealDetailsAPI;
    RepeatMealAPI repeatMealAPI;

    View view;
    LinearLayout ll_dateInfo;
    TextView textView_date, textView_day, textView_mealType, textView_selected_time;
    TextView textView_noty_racipeName, textView_noty_date;
    Spinner spinner_meal;
    EditText editText_NoP;
    Button button_done;
    ProgressBar progressBar;

    SimpleDateFormat simpledateformat;
    Date today_date;

    List<String> list_recipe_ids = new ArrayList<>();
    List<String> list_recipe_mealTypes = new ArrayList<>();
    List<String> list_recipe_mealTimes = new ArrayList<>();

    String condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat_meal);

        condition = getIntent().getStringExtra("condition");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);

        getRecipeNameAPI = getRecipeNameAPIService(BASE_URL);
        getLastMealDetailsAPI = getGetLastMealDetailsAPIService(BASE_URL);
        repeatMealAPI = getRepeatMealAPIService(BASE_URL);


        view = findViewById(R.id.activity_repeat_meal);
        ll_dateInfo = findViewById(R.id.rep_meal_ll_dateInfo);
        textView_date = findViewById(R.id.rep_meal_tv_todayDate);
        textView_day = findViewById(R.id.rep_meal_tv_todayDay);
        textView_noty_racipeName = findViewById(R.id.rep_meal_tv_noty_recipeName);
        textView_noty_date = findViewById(R.id.rep_meal_tv_noty_date);
        textView_mealType = findViewById(R.id.rep_meal_tv_mealType);
        textView_selected_time = findViewById(R.id.rep_meal_tv_mealTime);
        spinner_meal = findViewById(R.id.rep_meal_spinner_meal);
        editText_NoP = findViewById(R.id.rep_meal_et_nop);
        button_done = findViewById(R.id.rep_meal_btn_done);
        progressBar = findViewById(R.id.rep_meal_progressbar);

        setDateConndition(condition);

        setSpinner_Meal();

        editText_NoP.setText("1");
        editText_NoP.setSelection(editText_NoP.getText().length());

        ll_dateInfo.setOnClickListener(this);
        textView_selected_time.setOnClickListener(this);
        button_done.setOnClickListener(this);

        spinner_meal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                setLastMealDetails();
                setTypeOfMeal();
                setTimeOfMeal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setDateConndition(String condition) {
        if (condition.equals("week")) {
            setTomorrowData();

        } else {
            simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
            today_date = new Date();
            textView_date.setText(simpledateformat.format(today_date));

            simpledateformat = new SimpleDateFormat("EEEE");
            String dayOfWeek = simpledateformat.format(today_date);
            textView_day.setText(dayOfWeek);

            ll_dateInfo.setClickable(false);
        }
    }

    private void setLastMealDetails() {
        progressBar.setVisibility(View.VISIBLE);

        getLastMealDetailsAPI.getLastMealDetailsAPI(sessionManager.getUserid(), spinner_meal.getSelectedItem().toString()).enqueue(new Callback<List<MyRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<MyRes>> call, @NonNull Response<List<MyRes>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String last_dateOfRecipe = response.body().get(0).getDate();

                        textView_noty_racipeName.setText(spinner_meal.getSelectedItem().toString());
                        textView_noty_date.setText(last_dateOfRecipe);

                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MyRes>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "OnFailure", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setSpinner_Meal() {
        progressBar.setVisibility(View.VISIBLE);

        final List<String> list_recipe_names = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list_recipe_names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getRecipeNameAPI.getRecipeNames(sessionManager.getUserid()).enqueue(new Callback<List<MyRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<MyRes>> call, @NonNull Response<List<MyRes>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        List<MyRes> recipe_data = response.body();

                        if (recipe_data.size() != 0) {

                            for (int i = 0; i < recipe_data.size(); i++) {
                                list_recipe_ids.add(recipe_data.get(i).getM_id());
                                list_recipe_names.add(recipe_data.get(i).getRecipe_name());
                                list_recipe_mealTypes.add(recipe_data.get(i).getMeal_type());
                                list_recipe_mealTimes.add(recipe_data.get(i).getTime());
                            }

                            spinner_meal.setAdapter(adapter);

                            setTypeOfMeal();
                            setTimeOfMeal();

                            editText_NoP.setFocusable(true);
                            button_done.setClickable(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            editText_NoP.setText("");
                            editText_NoP.setFocusable(false);
                            button_done.setClickable(false);
                            Snackbar snackbar = Snackbar.make(view, "You have not saved any recipe yet! Please save one.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MyRes>> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "OnFailure", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void setTomorrowData() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String reportDate = df.format(tomorrow);

        textView_date.setText(reportDate);

        simpledateformat = new SimpleDateFormat("EEEE");
        String dayOfWeek = simpledateformat.format(tomorrow);
        textView_day.setText(dayOfWeek);
    }

    private void setTypeOfMeal() {
        textView_mealType.setText("Meal " + list_recipe_mealTypes.get(spinner_meal.getSelectedItemPosition()));
    }

    private void setTimeOfMeal() {
        textView_selected_time.setText(list_recipe_mealTimes.get(spinner_meal.getSelectedItemPosition()));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
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

            case R.id.rep_meal_ll_dateInfo:
                selectDate();
                break;

            case R.id.rep_meal_btn_done:
                addMealAPIRequest();
                break;

            case R.id.rep_meal_tv_mealTime:
                showTimePicker();
                break;
        }
    }

    private void selectDate() {
        if (condition.equals("week")) {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH + 1);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    final String sDate = String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(year);
                    textView_date.setText(sDate);

                    SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                    Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                    String dayOfWeek = simpledateformat.format(date);

                    textView_day.setText(dayOfWeek);
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }

    }

    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                textView_selected_time.setText("Time: " + selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    private void addMealAPIRequest() {

        if (editText_NoP.getText().toString().equals("")) {
            editText_NoP.setError("Required");
            editText_NoP.requestFocus();
        } else if (Integer.parseInt(editText_NoP.getText().toString()) > 15) {
            editText_NoP.setError("Check the value");
            editText_NoP.requestFocus();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            final String m_id, date, nOp;

            m_id = list_recipe_ids.get(spinner_meal.getSelectedItemPosition());
            date = textView_date.getText().toString();
            nOp = editText_NoP.getText().toString();

            repeatMealAPI.repeatMeal(m_id, date, nOp).enqueue(new Callback<MyRes>() {
                @Override
                public void onResponse(@NonNull Call<MyRes> call, @NonNull Response<MyRes> response) {

                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getMsg().equals("true")) {
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();
                            Toast.makeText(getApplicationContext(), "Your Meal details sent to your cook", Toast.LENGTH_SHORT).show();
                        } else if (response.body().getMsg().equals("xxx")) {
                            progressBar.setVisibility(View.INVISIBLE);

                            view = findViewById(R.id.activity_repeat_meal);
                            Snackbar snackbar = Snackbar.make(view, "This meal is already created on selected date.", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Msg != true", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MyRes> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


// -------------------------- APIs ---------------------------------------------//


    GetRecipeNameAPI getRecipeNameAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetRecipeNameAPI.class);
    }

    interface GetRecipeNameAPI {
        @POST("meal/get_recipe_name_api/")
        @FormUrlEncoded
        Call<List<MyRes>> getRecipeNames(@Field("u_id") String u_id);
    }


    RepeatMealAPI getRepeatMealAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(RepeatMealAPI.class);
    }

    interface RepeatMealAPI {
        @POST("meal/repeat_meal_api/")
        @FormUrlEncoded
        Call<MyRes> repeatMeal(@Field("m_id") String m_id,
                               @Field("date") String date,
                               @Field("nop") String nop
        );
    }


    GetLastMealDetailsAPI getGetLastMealDetailsAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetLastMealDetailsAPI.class);
    }

    interface GetLastMealDetailsAPI {
        @POST("meal/get_last_meal_api/")
        @FormUrlEncoded
        Call<List<MyRes>> getLastMealDetailsAPI(@Field("u_id") String u_id,
                                                @Field("recipeName") String recipeName
        );
    }

}
