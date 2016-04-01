package org.ncfrcteams.frcscoutinghub2016.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Pavan Dayal on 3/18/2016.
 */

public class CustomPageAdapter extends FragmentPagerAdapter{

    public List<Fragment> fragments;
    public List<String> fragtitles;

    public CustomPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> fragtitles) {
        super(fm);
        this.fragments = fragments;
        this.fragtitles = fragtitles;
    }

    @Override

    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.fragtitles.get(position);
    }



}
