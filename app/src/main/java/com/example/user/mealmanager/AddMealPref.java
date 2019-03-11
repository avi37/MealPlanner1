package com.example.user.mealmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class AddMealPref {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AddMealPref";
    private static final String IS_DATA_STORED = "IsDataStored";
    private static final String KEY_U_ID = "u_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_MEAL_TYPE = "mealType";
    private static final String KEY_FOOD_TYPE = "foodType";
    private static final String KEY_ID = "id";
    private static final String KEY_COOK_NAME = "cook_name";
    private static final String KEY_NOP = "noOfPersons";
    private static final String KEY_RECIPE_NAME = "recipeName";

    public AddMealPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public String getKeyCookName() {
        return pref.getString(KEY_COOK_NAME, null);
    }

    public String getKeyUId() {
        return pref.getString(KEY_U_ID, null);
    }

    public String getKeyDate() {
        return pref.getString(KEY_DATE, null);
    }

    public String getKeyTime() {
        return pref.getString(KEY_TIME, null);
    }

    public String getKeyMealType() {
        return pref.getString(KEY_MEAL_TYPE, null);
    }

    public String getKeyFoodType() {
        return pref.getString(KEY_FOOD_TYPE, null);
    }

    public String getKeyId() {
        return pref.getString(KEY_ID, null);
    }

    public String getKeyNop() {
        return pref.getString(KEY_NOP, null);
    }

    public String getKeyRecipeName() {
        return pref.getString(KEY_RECIPE_NAME, null);
    }


    public void addVP1Data(String u_id, String date, String time, String mealType, String foodType, String id, String cook_name) {
        // Storing login value as TRUE
        editor.putBoolean(IS_DATA_STORED, true);

        editor.putString(KEY_U_ID, u_id);
        editor.putString(KEY_DATE, date);

        editor.putString(KEY_TIME, time);
        editor.putString(KEY_MEAL_TYPE, mealType);
        editor.putString(KEY_FOOD_TYPE, foodType);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_COOK_NAME, cook_name);

        editor.commit();
        //Toast.makeText(_context, "Saved successfully", Toast.LENGTH_SHORT).show();
    }

    public void addVP2Data(String nop, String recipe_name) {
        editor.putString(KEY_NOP, nop);
        editor.putString(KEY_RECIPE_NAME, recipe_name);

        editor.commit();
    }

    public void deleteAddMealPref() {
        editor.clear();
        editor.commit();

        Toast.makeText(_context, "Data cleared successfully", Toast.LENGTH_SHORT).show();
    }

    // Get Data_Stored State
    public boolean isDataStored() {
        return pref.getBoolean(IS_DATA_STORED, false);
    }

}
