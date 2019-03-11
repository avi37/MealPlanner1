package com.example.user.mealmanager;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class RegisterCookActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    private RegisterCookAPI registerCookAPI;

    SessionManager sessionManager;

    EditText editText_cook_name, editText_cook_number;
    Spinner spinner_date, spinner_month, spinner_year;
    Button button_register;
    Spinner spinner_cookType;
    ProgressBar progressBar;

    boolean isDOBselected = false;

    String u_id, name, number, date, month, year, dob, cookType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cook);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        registerCookAPI = getAPIService(BASE_URL);
        sessionManager = new SessionManager(this);

        setDateSpinner();
        setMonthSpinner();
        setYearSpinner();
        spinner_cookType = findViewById(R.id.reg_cook_spinner_cook_type);

        editText_cook_name = findViewById(R.id.reg_cook_et_name);
        editText_cook_number = findViewById(R.id.reg_cook_et_number);
        button_register = findViewById(R.id.reg_cook_btn_register);
        progressBar = findViewById(R.id.add_cook_progressbar);


        button_register.setOnClickListener(this);

        spinner_date.setOnItemSelectedListener(this);
        spinner_month.setOnItemSelectedListener(this);
        spinner_year.setOnItemSelectedListener(this);

        spinner_cookType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cookType = spinner_cookType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setDateSpinner() {
        spinner_date = findViewById(R.id.reg_cook_spinnerDOB_date);
        List<String> date = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            date.add(String.valueOf(i));
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, date);

        // Drop down layout style - list view with radio button
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_date.setAdapter(dateAdapter);
    }

    private void setMonthSpinner() {
        spinner_month = findViewById(R.id.reg_cook_spinnerDOB_month);
        List<String> month = new ArrayList<>();
        month.add("January");
        month.add("February");
        month.add("March");
        month.add("April");
        month.add("May");
        month.add("June");
        month.add("July");
        month.add("August");
        month.add("September");
        month.add("October");
        month.add("November");
        month.add("December");

        // Creating adapter for spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, month);

        // Drop down layout style - list view with radio button
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_month.setAdapter(monthAdapter);
    }

    private void setYearSpinner() {
        spinner_year = findViewById(R.id.reg_cook_spinnerDOB_year);
        List<String> year = new ArrayList<>();
        for (int i = 1985; i <= 2016; i++) {
            year.add(String.valueOf(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, year);

        // Drop down layout style - list view with radio button
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_year.setAdapter(yearAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_cook_btn_register:
                sendCookRegistrationRequest();
                break;

        }
    }

    private void sendCookRegistrationRequest() {
        name = editText_cook_name.getText().toString();
        number = editText_cook_number.getText().toString();

        if (name.equals("")) {
            editText_cook_name.setError("Name is required");
            editText_cook_name.requestFocus();
        } else if (number.equals("")) {
            editText_cook_number.setError("Number is required");
            editText_cook_number.requestFocus();
        } else if (!isValidNumber(number)) {
            editText_cook_number.setError("Not a valid number");
            editText_cook_number.requestFocus();
        } else {
            dob = date + "-" + month + "-" + year;
            u_id = sessionManager.getUserid();

            showConfirmDialogBox();
        }

    }

    private void showConfirmDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Check the details");
        alert.setCancelable(false);

        alert.setNegativeButton("Refill", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callRegApi();
            }
        });
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.show();

    }

    private void callRegApi() {

        progressBar.setVisibility(View.VISIBLE);

        String _month = getDateNumber();

        String DoB = spinner_date.getSelectedItem().toString() + "-" + _month + "-" + spinner_year.getSelectedItem().toString();

        //Toast.makeText(this, "DoB: " + DoB, Toast.LENGTH_LONG).show();

        registerCookAPI.addCook(u_id, name, number, dob, number, cookType).enqueue(new Callback<List<MyRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<MyRes>> call, @NonNull Response<List<MyRes>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().get(0).getMsg().equalsIgnoreCase("true")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Cook registration request sent", Toast.LENGTH_LONG).show();
                        finish();
                        Toast.makeText(getApplicationContext(), "You will be notified when your cook will join", Toast.LENGTH_LONG).show();

                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Some error occured while sending request \n Please try after sometime", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Error in getting response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MyRes>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDateNumber() {
        String d = null;
        switch (spinner_month.getSelectedItem().toString()) {
            case "January":
                d = "01";
                break;

            case "February":
                d = "02";
                break;

            case "March":
                d = "03";
                break;

            case "April":
                d = "04";
                break;

            case "May":
                d = "05";
                break;

            case "June":
                d = "06";
                break;

            case "July":
                d = "07";
                break;

            case "August":
                d = "08";
                break;

            case "September":
                d = "09";
                break;

            case "October":
                d = "10";
                break;

            case "November":
                d = "11";
                break;

            case "December":
                d = "12";
                break;
        }
        return d;
    }

    private boolean isValidNumber(String number) {
        Pattern p;
        Matcher m;
        String NUMBER_STRING = "^[6-9]\\d{9}$";
        p = Pattern.compile(NUMBER_STRING);
        m = p.matcher(number);
        return m.matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.reg_cook_spinnerDOB_date:
                date = spinner_date.getSelectedItem().toString();
                break;

            case R.id.reg_cook_spinnerDOB_month:
                month = spinner_month.getSelectedItem().toString();
                break;

            case R.id.reg_cook_spinnerDOB_year:
                year = spinner_year.getSelectedItem().toString();
                break;
        }

        isDOBselected = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        isDOBselected = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


// --------------------- APIs -------------------------------------------------- //


    RegisterCookAPI getAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(RegisterCookAPI.class);
    }


    interface RegisterCookAPI {
        @POST("cook/add_cook_api/")
        @FormUrlEncoded
        Call<List<MyRes>> addCook(@Field("u_id") String u_id,
                                  @Field("name") String name,
                                  @Field("mobile") String mobile,
                                  @Field("dob") String dob,
                                  @Field("password") String pwd,
                                  @Field("cook_type") String cook_type
        );
    }

}
