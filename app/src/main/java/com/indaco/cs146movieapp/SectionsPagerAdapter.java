package com.indaco.cs146movieapp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by stefan on 11/29/2015.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public android.support.v4.app.Fragment getItem(int position){
        switch(position) {
            case 0:
                return new ProfileFragment();
            case 1:
                return new MoviesFragment();
            case 2:
                return new FriendsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position){
        Locale locale = Locale.getDefault();
        switch(position){
            case 0:
                return "Profile".toUpperCase(locale);
            case 1:
                return "Movies".toUpperCase(locale);
            case 2:
                return "Friends".toUpperCase(locale);
        }
        return null;
    }
}
