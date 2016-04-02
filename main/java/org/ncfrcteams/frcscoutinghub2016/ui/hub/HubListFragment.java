package org.ncfrcteams.frcscoutinghub2016.ui.hub;

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
import org.ncfrcteams.frcscoutinghub2016.communication.qr.MultiQRDialog;
import org.ncfrcteams.frcscoutinghub2016.matchdata.Obstacle;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.Match;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.MatchDescriptor;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.Schedule;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.support.DatabaseAdapter;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.support.TempDialog1;

public class HubListFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, DatabaseAdapter.DatabaseListener, TempDialog1.TempDialog1Listener {

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        new TempDialog1(getContext(), this, myListAdapter.getItem(position)).show();
        //Toast.makeText(getActivity(), myListAdapter.getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
        //mListener.switchToDetails(myListAdapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String item = myListAdapter.getItem(position).getTitle();
        Toast.makeText(getActivity(), item + " long", Toast.LENGTH_SHORT).show();
        return true;
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

    @Override
    public void onListChange() { //called 2x every time schedule is changed
        if(theTimeIsRight) {
            mListener.autopush();
        }
    }

    public void addNewMatch(int[] teams, int matchnum, boolean isQual, String phonenum){
        mySchedule.add(new MatchDescriptor(getContext(), matchnum, teams, isQual, phonenum));
    }

    @Override
    public void onTempDialog1(int matchNum, boolean isQual, int[] teams, int[] barriers, String phoneNum) {
        String[] qrtexts = new String[6];
        String b = "";
        for (int barrier : barriers) {
            b += barrier + ",";
        }

        String a;
        for (int i = 0; i < 6; i++) {
            a = (i < 3 ? "R," : "B,") + (isQual ? "Q," : "E,") + String.valueOf(matchNum) + "," + teams[i];
            qrtexts[i] = a + "," + b + phoneNum;
        }

        Toast.makeText(getContext(), qrtexts[2] + qrtexts[3] , Toast.LENGTH_LONG).show();

        new MultiQRDialog(getContext(), qrtexts).show();
    }

    public interface HubListFragListener {
        void autopush();
        void switchToDetails(Match match);
    }

}
