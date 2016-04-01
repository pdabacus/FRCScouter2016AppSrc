package org.ncfrcteams.frcscoutinghub2016.communication.qr;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Kyle Brown on 3/20/2016.
 */
public class QRFragmentAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private String[] content;

    public QRFragmentAdapter(FragmentManager fm, String[] titles, String[] content) {
        super(fm);
        this.titles = titles;
        this.content = content;
    }

    @Override
    public Fragment getItem(int position) {
        return QRFragment.newInstance(titles[position],content[position]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
