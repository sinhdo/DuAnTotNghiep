package com.example.duantotnghiep.adapter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.duantotnghiep.fragment.LoginTabFragment;
import com.example.duantotnghiep.fragment.RegTabFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTab;
    public  LoginAdapter(FragmentManager fm, Context context, int totalTab){
        super(fm);
        this.context = context;
        this.totalTab = totalTab;
    }

    @Override
    public int getCount() {
        return totalTab;
    }

    public Fragment getItem(int position){
        switch (position){
            case 0:
                LoginTabFragment loginTabFragment =new LoginTabFragment();
                return loginTabFragment;
            case 1:
                RegTabFragment regTabFragment = new RegTabFragment();
                return regTabFragment;
            default:
                return null;
        }
    }
}
