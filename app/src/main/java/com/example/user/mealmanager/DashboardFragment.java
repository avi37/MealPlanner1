package com.example.user.mealmanager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class DashboardFragment extends android.app.Fragment implements View.OnClickListener {

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    SessionManager sessionManager;
    GetCounterAPI getCounterAPI;
    CheckCookAddedAPI checkCookAddedAPI;

    Context context;

    View view;
    TextView textView_notificationBar;
    CardView cardView_selectMenu, cardView_todaysMenu, cardView_viewHistory, cardView_notifyCook, cardView_birthdayList, cardView_addCook;

    String[] arr_mealType, arr_counter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sessionManager = new SessionManager(getActivity());
        getCounterAPI = getGetCounterAPIService(BASE_URL);
        checkCookAddedAPI = getCheckCookAddedAPIService(BASE_URL);

        textView_notificationBar = view.findViewById(R.id.mainDash_tv_notificationBar);
        cardView_selectMenu = view.findViewById(R.id.dash_card_create_today_menu);
        cardView_todaysMenu = view.findViewById(R.id.dash_card_todays_menu);
        cardView_viewHistory = view.findViewById(R.id.dash_card_create_weekly_menu);
        cardView_notifyCook = view.findViewById(R.id.dash_card_notify_cook);
        cardView_birthdayList = view.findViewById(R.id.dash_card_check_weekly_menu);
        cardView_addCook = view.findViewById(R.id.dash_card_add_cook);

        cardView_selectMenu.setOnClickListener(this);
        cardView_todaysMenu.setOnClickListener(this);
        cardView_viewHistory.setOnClickListener(this);
        cardView_notifyCook.setOnClickListener(this);
        cardView_birthdayList.setOnClickListener(this);
        cardView_addCook.setOnClickListener(this);


        return view;
    }


    private void checkForMealNotifications() {

        getCounterAPI.getMealCounter(sessionManager.getUserid()).enqueue(new Callback<List<GetMealTypeRes>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetMealTypeRes>> call, @NonNull Response<List<GetMealTypeRes>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        arr_mealType = new String[response.body().size()];
                        arr_counter = new String[response.body().size()];

                        for (int i = 0; i < response.body().size(); i++) {
                            arr_mealType[i] = response.body().get(i).getMeal_type();
                            arr_counter[i] = response.body().get(i).getCount();
                        }

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showNotifications(arr_mealType, arr_counter);
                            }
                        }, 500);


                    } else {
                        Toast.makeText(getActivity(), "response body null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GetMealTypeRes>> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "Socket Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "get Counter onFailure", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void showNotifications(String[] arr_mealType, String[] arr_counter) {
        for (int i = 0; i < arr_mealType.length; i++) {

            if (arr_counter[i].equals("1")) {
                //textView_notificationBar.setVisibility(View.VISIBLE);
                textView_notificationBar.setBackgroundColor(getResources().getColor(R.color.notificationGreen));
                textView_notificationBar.setText("Meal " + arr_mealType[i] + " is seen by your cook");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //textView_notificationBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkForMealNotifications();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dash_card_create_today_menu:
                createTodayMenu();
                break;

            case R.id.dash_card_todays_menu:
                checkTodayMenu();
                break;

            case R.id.dash_card_create_weekly_menu:
                createWeeklyMenu();
                break;

            case R.id.dash_card_notify_cook:
                notifyCook();
                break;

            case R.id.dash_card_check_weekly_menu:
                checkWeeklyMenu();
                break;

            case R.id.dash_card_add_cook:
                addCook();
                break;
        }
    }

    private void addCook() {
        Intent i = new Intent(context, RegisterCookActivity.class);
        startActivity(i);
    }

    private void checkWeeklyMenu() {
        Toast.makeText(context, "Showing your weekly plans", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, ViewWeekPlan.class);
        startActivity(i);
    }

    private void notifyCook() {
        Toast.makeText(context, "Notified to cook", Toast.LENGTH_SHORT).show();

        /*final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);

        String u_id = sessionManager.getUserid();

        checkCookAddedAPI.isCookAdded(u_id).enqueue(new Callback<MyRes>() {
            @Override
            public void onResponse(@NonNull Call<MyRes> call, @NonNull Response<MyRes> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getMsg().equals("false")) {
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar.make(getView(), "You have not added any cook currently. \nPlease add your Cook first", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else {
                            dialog.dismiss();

                            startActivity(new Intent(getActivity(), WeeklyMenuActivity.class));
                        }
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyRes> call, @NonNull Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_LONG).show();
            }
        });*/

    }

    private void createWeeklyMenu() {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);

        String u_id = sessionManager.getUserid();

        checkCookAddedAPI.isCookAdded(u_id).enqueue(new Callback<MyRes>() {
            @Override
            public void onResponse(@NonNull Call<MyRes> call, @NonNull Response<MyRes> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getMsg().equals("false")) {
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar.make(getView(), "You have not added any cook currently. \nPlease add your Cook first", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else {
                            dialog.dismiss();

                            AddMealCustomDialog addMealCustomDialog = new AddMealCustomDialog();
                            Bundle b = new Bundle();
                            b.putString("condition", "week");
                            addMealCustomDialog.setArguments(b);
                            addMealCustomDialog.show(getFragmentManager(), "");
                        }
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyRes> call, @NonNull Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkTodayMenu() {
        Intent i = new Intent(context, CheckTodayMenu.class);
        startActivity(i);
    }

    private void createTodayMenu() {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading. Please wait...", true);

        String u_id = sessionManager.getUserid();

        checkCookAddedAPI.isCookAdded(u_id).enqueue(new Callback<MyRes>() {
            @Override
            public void onResponse(@NonNull Call<MyRes> call, @NonNull Response<MyRes> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.body().getMsg().equals("false")) {
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar.make(getView(), "You have not added any cook currently. \nPlease add your Cook first", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else {
                            dialog.dismiss();
                            AddMealCustomDialog addMealCustomDialog = new AddMealCustomDialog();
                            Bundle b = new Bundle();
                            b.putString("condition", "today");
                            addMealCustomDialog.setArguments(b);
                            addMealCustomDialog.show(getFragmentManager(), "");
                        }
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Response not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyRes> call, @NonNull Throwable t) {
                dialog.dismiss();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "Socket Time out. Please try again.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


//----------------------------------------------------- APIs -------------------------------------------------------------//


    CheckCookAddedAPI getCheckCookAddedAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(CheckCookAddedAPI.class);
    }

    interface CheckCookAddedAPI {
        @POST("cook/check_cook_api/")
        @FormUrlEncoded
        Call<MyRes> isCookAdded(@Field("u_id") String u_id);
    }


    GetCounterAPI getGetCounterAPIService(String baseUrl) {
        return RetrofitClient.getClient(baseUrl).create(GetCounterAPI.class);
    }

    interface GetCounterAPI {
        @POST("meal/get_counter_api/")
        @FormUrlEncoded
        Call<List<GetMealTypeRes>> getMealCounter(@Field("u_id") String u_id);
    }


}
