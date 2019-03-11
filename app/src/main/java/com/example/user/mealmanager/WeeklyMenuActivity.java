package com.example.user.mealmanager;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class WeeklyMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private final String BASE_URL = "http://www.code-fuel.in/mealplanner/";
    //SessionManager sessionManager;

    FloatingActionButton fab_addMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_menu);

        //sessionManager = new SessionManager(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        fab_addMeal = findViewById(R.id.weeklyMenu_fab_addNewMeal);

        fab_addMeal.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void showAddMealDialog() {
        AddMealCustomDialog addMealCustomDialog = new AddMealCustomDialog();
        Bundle b = new Bundle();
        b.putString("condition", "week");
        addMealCustomDialog.setArguments(b);
        addMealCustomDialog.show(getFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weeklyMenu_fab_addNewMeal:
                showAddMealDialog();
                break;

        }
    }


//------------------------------- APIs --------------------------------------------//

}
