package com.example.user.mealmanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    SessionManager sessionManager;
    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    private LoginAPI loginAPI;

    View view;
    EditText editText_number, editText_password;
    Button button_login;
    Spinner spinner_role;
    TextView textView_forgot_pwd;
    ProgressBar progressBar;
    FrameLayout frameLayout_login_content;

    String selected_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginAPI = getAPIService(BASE_URL);
        sessionManager = new SessionManager(this);

        if (sessionManager.checkLogin()) {
            finish();

        } else {
            spinner_role = findViewById(R.id.login_spinner_role);
            editText_number = findViewById(R.id.login_et_number);
            editText_password = findViewById(R.id.login_et_password);
            button_login = findViewById(R.id.login_btn_login);
            //textView_skip = findViewById(R.id.login_tv_skip);
            textView_forgot_pwd = findViewById(R.id.login_tv_forgot_pwd);
            progressBar = findViewById(R.id.login_progressbar);
            frameLayout_login_content = findViewById(R.id.frame_login_container);


            spinner_role.setOnItemSelectedListener(this);
            button_login.setOnClickListener(this);
            textView_forgot_pwd.setOnClickListener(this);

            spinner_role.requestFocus();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                loginMethod();
                break;

            case R.id.login_tv_forgot_pwd:
                forgotPasswordMethod();
                break;
        }

    }

    private void forgotPasswordMethod() {
        Toast.makeText(this, "User forgot the password!", Toast.LENGTH_SHORT).show();
    }

    private void loginMethod() {
        String number, password;
        number = editText_number.getText().toString();
        password = editText_password.getText().toString();

        if (number.equals("")) {
            editText_number.setError("Number is required");
            editText_number.requestFocus();
        } else if (password.equals("")) {
            editText_password.setError("Password is required");
            editText_password.requestFocus();
        } else if (!isValidNumber(number)) {
            editText_number.setError("Not a valid number");
            editText_number.requestFocus();
        } else if (password.length() < 8) {
            editText_password.setError("Length of password must be >=8 characters");
            editText_password.requestFocus();
            Toast.makeText(this, "Length of password must be >=8 characters", Toast.LENGTH_LONG).show();
        } else {

            progressBar.setVisibility(View.VISIBLE);
            frameLayout_login_content.setClickable(false);

            loginAPI.login(number, password, selected_role).enqueue(new Callback<MyRes>() {
                @Override
                public void onResponse(@NonNull Call<MyRes> call, @NonNull Response<MyRes> response) {

                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().getMsg().equalsIgnoreCase("true")) {

                            if (response.body().getStatus().equals("1")) {
                                if (response.body().getRole().equals("2")) {
                                    sessionManager.createLoginSession(response.body().getId(), "xx", response.body().getName(), response.body().getDob(), response.body().getMobile(), response.body().getEmail(), response.body().getRole());
                                } else {
                                    sessionManager.createLoginSession("xx", response.body().getId(), response.body().getName(), response.body().getDob(), response.body().getMobile(), response.body().getEmail(), response.body().getRole());
                                }
                                progressBar.setVisibility(View.GONE);

                                Intent intent = new Intent(getApplicationContext(), MainNavigation.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                frameLayout_login_content.setClickable(true);
                                Toast.makeText(getApplicationContext(), "Your Account is de-activated\nPlease contact to Administrator", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            frameLayout_login_content.setClickable(true);
                            Toast.makeText(getApplicationContext(), "Invalid Credential Or \nCheck your role", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        frameLayout_login_content.setClickable(true);
                        Toast.makeText(getApplicationContext(), "Connection error1" + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MyRes> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    frameLayout_login_content.setClickable(true);
                    Toast.makeText(getApplicationContext(), "Connection error2", Toast.LENGTH_SHORT).show();
                }
            });

        }

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
        if (spinner_role.getSelectedItem().toString().equals("Officer")) {
            selected_role = "1";
        } else {
            selected_role = "2";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "You selected nothing", Toast.LENGTH_SHORT).show();
    }


//------------------------------ APIs ------------------------------------------------//


    LoginAPI getAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(LoginAPI.class);
    }

    interface LoginAPI {
        @POST("home/login_api/")
        @FormUrlEncoded
        Call<MyRes> login(@Field("user") String number,
                          @Field("pass") String pwd,
                          @Field("role") String role
        );
    }

}
