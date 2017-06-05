package com.example.jaseem.tandem.SwipePager;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.jaseem.tandem.R;

/**
 * Created by Jaseem on 5/8/17.
 */

public class PagerPage extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pager);

        //mPager = (ViewPager) findViewById(R.id.vpPager);
        //pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        //mPager.setAdapter(pagerAdapter);
    }
}
