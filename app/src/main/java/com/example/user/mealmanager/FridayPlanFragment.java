package com.example.user.mealmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class FridayPlanFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_friday_plan, container, false);

    return view;
    }

    public static FridayPlanFragment newInstance(int page, String title) {
        FridayPlanFragment fragmentFridayPlan = new FridayPlanFragment();
        Bundle args = new Bundle();
        args.putInt("1", page);
        args.putString("Sunday", title);
        fragmentFridayPlan.setArguments(args);
        return fragmentFridayPlan;
    }


}
