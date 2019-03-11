package com.example.user.mealmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

public class ViewPlanCustomPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 7;

    public ViewPlanCustomPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return SundayPlanFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return MondayPlanFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return TuesdayPlanFragment.newInstance(2, "Page # 3");
            case 3:
                return WednesdayPlanFragment.newInstance(3, "Page # 4");
            case 4:
                return ThursdayPlanFragment.newInstance(4, "Page # 5");
            case 5:
                return FridayPlanFragment.newInstance(5, "Page # 6");
            case 6:
                return SaturdayPlanFragment.newInstance(6, "Page # 7");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
