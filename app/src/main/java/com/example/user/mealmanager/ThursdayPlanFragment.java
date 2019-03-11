package com.example.user.mealmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ThursdayPlanFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_thursday_plan, container, false);


        return view;
    }

    public static ThursdayPlanFragment newInstance(int page, String title) {
        ThursdayPlanFragment fragmentThursdayPlan = new ThursdayPlanFragment();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("Sunday", title);
        fragmentThursdayPlan.setArguments(args);
        return fragmentThursdayPlan;
    }

}
