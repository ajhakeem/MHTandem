package com.example.jaseem.tandem.SwipePager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Jaseem on 5/8/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FreeTimesFragment();
            case 1:
                return new Frag2();
            default:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
