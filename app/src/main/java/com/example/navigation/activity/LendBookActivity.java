package com.example.navigation.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.navigation.R;

public class LendBookActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ff0000"));
        tabLayout.addTab(tabLayout.newTab().setText("ADD BOOK"));
        tabLayout.addTab(tabLayout.newTab().setText("LEND BOOK"));
        tabLayout.addTab(tabLayout.newTab().setText("REMOVE BOOK"));
        tabLayout.addTab(tabLayout.newTab().setText("others"));
        tabLayout.setTabGravity(0);
        PagerAdapter pageradapt=new Pageradapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageradapt);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    }