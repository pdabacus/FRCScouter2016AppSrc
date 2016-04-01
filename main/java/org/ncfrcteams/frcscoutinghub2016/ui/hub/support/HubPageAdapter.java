package org.ncfrcteams.frcscoutinghub2016.ui.hub.support;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.communication.http.Poster;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.HubContentsFragment;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.HubCreateFragment;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.HubListFragment;


/**
 * Created by Pavan Dayal on 3/18/2016.
 */

public class HubPageAdapter extends FragmentPagerAdapter{

    public Context context;
    public HubCreateFragment create;
    public HubListFragment listView;
    public HubContentsFragment content;
    private Poster poster;

    public HubPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.create = HubCreateFragment.newInstance();
        this.listView = HubListFragment.newInstance();
        this.content = HubContentsFragment.newInstance();
        this.poster = new Poster(context, "http://localhost/frc/database/", "test", "test", true);
    }

    //*********************************** HubPageAdapter Methods ***********************************

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return create;
            case 2:
                return content;
            default:
                return listView;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    //*********************************** Poster Methods ************************************

    public void uploadDatabase(){
        poster.uploadDatabase(listView.mySchedule.getDatabase());
    }

    public boolean isDefaultUser(){
        return poster.getUser().equals("test");
    }

    public void setAlternativeUser(){
        poster.reset("http://pavanec2.us.to/frc/database/", "4828", "RoboEagles4828", true);
    }

    public void autopush(){
        //TODO auto push to server every so often and when this is called
        Toast.makeText(context, "autopush", Toast.LENGTH_SHORT).show();
    }

    //*********************************** sms Methods ************************************

    public void addSMStoSchedule(String number, String message){
        String result = listView.mySchedule.addSMS(message);
        if (! result.equals("")) {
            Toast.makeText(context, number + " updated " + result, Toast.LENGTH_LONG).show();
        }
    }

    //**********************************************************************************************

}
