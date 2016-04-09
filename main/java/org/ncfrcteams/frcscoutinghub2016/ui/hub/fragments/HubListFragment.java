package org.ncfrcteams.frcscoutinghub2016.ui.hub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.ui.MultiQRDialog;
import org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule.Match;
import org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule.MatchDescriptor;
import org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule.Schedule;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.HubEditBarriersDialog;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.support.DatabaseAdapter;

public class HubListFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, DatabaseAdapter.DatabaseListener, HubEditBarriersDialog.TempDialog1Listener {

    private boolean theTimeIsRight;
    private HubListFragListener mListener;
    public Schedule mySchedule;
    public DatabaseAdapter myListAdapter;
    ListView hubListView;

    public HubListFragment() {
    }

    public static HubListFragment newInstance() {
        return new HubListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myListAdapter = new DatabaseAdapter(getContext(), this);
        mySchedule = new Schedule();
        mySchedule.setScheduleChangeListener(myListAdapter);
        theTimeIsRight = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_frag_list, container, false);

        hubListView = (ListView) view.findViewById(R.id.hubListView);
        hubListView.setAdapter(myListAdapter);
        hubListView.setOnItemClickListener(this);
        hubListView.setOnItemLongClickListener(this);
        hubListView.setEmptyView(view.findViewById(R.id.empty));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HubListFragListener) {
            mListener = (HubListFragListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement HubListFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //****************************** HubListFragment OnClick Listener ******************************

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        new HubEditBarriersDialog(getContext(), this, myListAdapter.getItem(position)).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String item = myListAdapter.getItem(position).getTitle();
        Toast.makeText(getActivity(), item + " long", Toast.LENGTH_SHORT).show();
        return true;
    }

    //********************************** DatabaseAdapter Listener **********************************

    @Override
    public void onListChange() { //called 2x every time schedule is changed
        if(theTimeIsRight) {
            mListener.autopush();
        }
    }

    //************************************ HubEditBarriersDialog Listener ************************************

    @Override
    public void generateQRs(Match match) {
        new MultiQRDialog(getContext(), match.getStrings()).show();
    }

    //****************************** HubPageFragment.addNewMatch() Call ****************************

    public void addNewMatch(int[] teams, int matchnum, boolean isQual, String phonenum){
        mySchedule.add(new MatchDescriptor(matchnum, teams, isQual, false, phonenum));
    }

    //****************************** HubPageFragment.POSTResult() Call *****************************

    public void POSTResult(String returnAddress, String result){
        switch (returnAddress){
            case "download":
                Toast.makeText(getContext(), "Downloaded Database: \n" + result, Toast.LENGTH_SHORT).show();
                //TODO ask what to do with downloaded data
                break;
            default:
                break;
        }
    }

    //**********************************************************************************************

    public interface HubListFragListener {
        void autopush();
    }

}
