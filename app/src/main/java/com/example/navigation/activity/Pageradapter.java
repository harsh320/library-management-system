package com.example.navigation.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.navigation.lending.frag1;
import com.example.navigation.lending.frag2;
import com.example.navigation.lending.frag3;
import com.example.navigation.lending.frag4;

public class Pageradapter extends FragmentStatePagerAdapter {
    int numberoftabs;
    public Pageradapter(FragmentManager fm, int numberoftabs) {
        super(fm);
        this.numberoftabs=numberoftabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                frag1 frag1=new frag1();
                return frag1;
            case 1:
                frag2 frag2=new frag2();
                return frag2;
            case 2:
                frag3 frag3=new frag3();
                return frag3;
            case 3:
                frag4 frag4=new frag4();
                return frag4;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numberoftabs;
    }
}
