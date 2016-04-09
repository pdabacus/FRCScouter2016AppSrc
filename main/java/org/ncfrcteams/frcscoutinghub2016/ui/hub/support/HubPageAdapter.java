package org.ncfrcteams.frcscoutinghub2016.ui.hub.support;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.communication.http.POST;
import org.ncfrcteams.frcscoutinghub2016.communication.http.Poster;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.fragments.HubContentsFragment;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.fragments.HubManageFragment;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.fragments.HubListFragment;


/**
 * Created by Pavan Dayal on 3/18/2016.
 */

public class HubPageAdapter extends FragmentPagerAdapter implements POST.POSTListener{

    public Context context;
    public HubManageFragment create;
    public HubListFragment listView;
    public HubContentsFragment content;
    private Poster poster;

    public HubPageAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.create = HubManageFragment.newInstance();
        this.listView = HubListFragment.newInstance();
        this.content = HubContentsFragment.newInstance();
        this.poster = new Poster(context, "http://pavanec2.us.to/frc/database/", "test", "test", true, this);
    }

    //*********************************** HubPageAdapter Methods ***********************************

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return create;
            case 1:
                return listView;
            default:
                return content;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    //*********************************** Poster Methods ************************************

    public void POSTRequest(String requestType, String extra){
        switch(requestType){
            case "upload":
                poster.uploadDatabase(listView.mySchedule.getDatabase());
                break;
            case "download":
                poster.downloadDatabase();
                break;
            case "schedule":
                poster.uploadSchedule(extra);
                break;
            default:
                break;
        }
    }

    public void autopush(){
        //TODO auto push to server every so often and when this is called
        Toast.makeText(context, "autopush", Toast.LENGTH_SHORT).show();
    }

    //*********************************** SMS Methods **********************************************

    public void addSMStoSchedule(String number, String message){
        String result = listView.mySchedule.addSMS(message);
        if (! result.equals("")) {
            Toast.makeText(context, number + " updated " + result, Toast.LENGTH_LONG).show();
        }
    }

    //*********************************** POST Listener **********************************************

    @Override
    public void POSTResult(String returnAddress, String result) {
        listView.POSTResult(returnAddress, result);
    }

    //**********************************************************************************************

}
