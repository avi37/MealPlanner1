package com.example.user.mealmanager;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class CookDashboardFragment extends android.app.Fragment implements View.OnClickListener {

    View view;

    CardView cardView_todayMenu, cardView_weekMenu, cardView_notifications;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_cook_dashboard, container, false);

        cardView_todayMenu = view.findViewById(R.id.cookDash_card_todays_menu);
        cardView_weekMenu = view.findViewById(R.id.cookDash_card_weekly_menu);
        cardView_notifications = view.findViewById(R.id.cookDash_card_notifications);

        cardView_todayMenu.setOnClickListener(this);
        cardView_weekMenu.setOnClickListener(this);
        cardView_notifications.setOnClickListener(this);

        checkTodayMenu();

        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.cookDash_card_todays_menu:
                checkTodayMenu();
                break;

            case R.id.cookDash_card_weekly_menu:
                checkWeekMenu();
                break;

            case R.id.cookDash_card_notifications:
                checkNotifications();
                break;

        }
    }


    private void checkTodayMenu() {
        Intent i = new Intent(getActivity(), CheckTodayMenu.class);
        startActivity(i);
    }

    private void checkWeekMenu() {
        Toast.makeText(getActivity(), "Showing Weekly Meal Plans", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), ViewWeekPlan.class);
        startActivity(i);

    }

    private void checkNotifications() {
        Toast.makeText(getActivity(), "My notifications", Toast.LENGTH_SHORT).show();
    }


}
