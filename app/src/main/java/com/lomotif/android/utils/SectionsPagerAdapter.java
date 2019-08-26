package com.lomotif.android.utils;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> titleList;
    ArrayList<Fragment> fragmentList;

    public SectionsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position % titleList.size());
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position % titleList.size()).toUpperCase();
    }

    public void add(String title, Fragment fragment) {
        titleList.add(title);
        fragmentList.add(fragment);
    }
//
//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}