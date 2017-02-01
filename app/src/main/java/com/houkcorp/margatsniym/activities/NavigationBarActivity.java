package com.houkcorp.margatsniym.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.houkcorp.margatsniym.R;

public class NavigationBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_bar);

        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);
    }
}