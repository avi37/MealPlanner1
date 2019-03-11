package com.example.user.mealmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SundayPlanFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sunday_plan, container, false);
    }

    public static SundayPlanFragment newInstance(int page, String title) {
        SundayPlanFragment fragmentSundayPlan = new SundayPlanFragment();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("Monday", title);
        fragmentSundayPlan.setArguments(args);
        return fragmentSundayPlan;
    }
}
