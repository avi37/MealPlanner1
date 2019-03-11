package com.example.user.mealmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;

import static android.content.SharedPreferences.*;

public class SessionManager {

    SharedPreferences mainPref, cookPref;
    Editor editor, cookEditor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String MAIN_PREF_NAME = "MainLoginPref";
    private static final String COOK_PREF_NAME = "CookLoginPref";

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_U_ID = "u_id";
    private static final String KEY_C_ID = "c_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DOB = "dob";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";

    //public static final String KEY_STATUS = "status";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        mainPref = _context.getSharedPreferences(MAIN_PREF_NAME, PRIVATE_MODE);
        editor = mainPref.edit();

        cookPref = _context.getSharedPreferences(COOK_PREF_NAME, PRIVATE_MODE);
        cookEditor = cookPref.edit();
    }

    public void createLoginSession(String u_id, String c_id, String name, String dob, String mobile, String email, String role) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_U_ID, u_id);
        editor.putString(KEY_C_ID, c_id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        //editor.putString(KEY_STATUS, status);

        editor.commit();
        Toast.makeText(_context, "Session Preference created", Toast.LENGTH_SHORT).show();
    }


    public void createCookLoginSession(String id, String name, String dob, String mobile, String email, String role) {

        cookEditor.putBoolean(IS_LOGIN, true);

        cookEditor.putString(KEY_U_ID, id);
        cookEditor.putString(KEY_NAME, name);
        cookEditor.putString(KEY_DOB, dob);
        cookEditor.putString(KEY_MOBILE, mobile);
        cookEditor.putString(KEY_EMAIL, email);
        cookEditor.putString(KEY_ROLE, role);

        cookEditor.commit();

        Toast.makeText(_context, "Cook Preference created", Toast.LENGTH_SHORT).show();

    }

    public String getUserRole() {
        return mainPref.getString(KEY_ROLE, null);
    }

    public String getUserName() {
        return mainPref.getString(KEY_NAME, null);
    }

    public String getUserid() {
        return mainPref.getString(KEY_U_ID, null);
    }

    public String getCId() {
        return mainPref.getString(KEY_C_ID, null);
    }

    public String getNumber() {
        return mainPref.getString(KEY_MOBILE, null);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_U_ID, mainPref.getString(KEY_U_ID, null));
        user.put(KEY_NAME, mainPref.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, mainPref.getString(KEY_MOBILE, null));
        user.put(KEY_EMAIL, mainPref.getString(KEY_EMAIL, null));
        user.put(KEY_ROLE, mainPref.getString(KEY_ROLE, null));

        return user;
    }

    public boolean checkLogin() {
        boolean status = false;

        if (this.isLoggedIn()) {
            status = true;
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainNavigation.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            /*// Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

            _context.startActivity(i);

        }
        return status;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        cookEditor.clear();
        cookEditor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(_context, "User Logged out", Toast.LENGTH_SHORT).show();
        // Staring Login Activity
        _context.startActivity(i);
    }

    // Get Login State
    public boolean isLoggedIn() {
        return mainPref.getBoolean(IS_LOGIN, false);
    }

}
