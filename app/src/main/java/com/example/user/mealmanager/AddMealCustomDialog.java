package com.example.user.mealmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class AddMealCustomDialog extends android.app.DialogFragment {

    View view;

    ImageView repeatMeal, addNewMeal;
    String condition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        condition = getArguments().getString("condition");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_meal_dialog, container);

        repeatMeal = view.findViewById(R.id.addNewMeal_iv_repeatMeal);
        addNewMeal = view.findViewById(R.id.addNewMeal_iv_addMeal);

        repeatMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RepeatMealActivity.class);
                i.putExtra("condition", condition);
                startActivity(i);
                dismiss();

            }
        });

        addNewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SelectMenuActivity.class);
                i.putExtra("condition", condition);
                startActivity(i);
                dismiss();
            }
        });

        return view;
    }

}
