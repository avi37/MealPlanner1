package com.example.user.mealmanager;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SessionManager sessionManager;
    AddMealPref addMealPref;

    NavigationView navigationView;

    TextView textView_navHeader_username;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        addMealPref = new AddMealPref(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textView_navHeader_username = navigationView.getHeaderView(0).findViewById(R.id.navHeader_tv_username);
        userName = sessionManager.getUserName();
        textView_navHeader_username.setText(userName);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (sessionManager.getUserRole().equals("0")) {
            ft.add(R.id.content_main_navigation, new CookDashboardFragment());
            setTitle("Cook Dashboard");
        } else {
            ft.add(R.id.content_main_navigation, new DashboardFragment());
            setTitle("Dashboard");
        }
        navigationView.getMenu().getItem(0).setChecked(true);
        ft.commit();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actBar_menu_help) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Toast.makeText(this, "Dashboard Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_my_profile) {
            startActivity(new Intent(this, MyProfileActivity.class));
        } else if (id == R.id.nav_my_recipes) {
            startActivity(new Intent(this, MyRecipesActivity.class));
        } else if (id == R.id.nav_logout) {
            showLogoutAlert();
        } else if (id == R.id.nav_quit) {
            System.exit(0);
        } else if (id == R.id.nav_about_dev) {
            startActivity(new Intent(this, AboutDevActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Log out alert");
        alertDialog.setMessage("Are you sure you want to from your account?");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                addMealPref.deleteAddMealPref();
                sessionManager.logoutUser();
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
    }


}
