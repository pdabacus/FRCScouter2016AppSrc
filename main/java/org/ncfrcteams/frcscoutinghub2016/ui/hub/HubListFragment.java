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
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.MatchDescriptor;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.Schedule;
import org.ncfrcteams.frcscoutinghub2016.ui.DatabaseAdapter;

public class HubListFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, DatabaseAdapter.DatabaseListener {

    private HubListFragListener mListener;
    public static Schedule mySchedule;
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
        String item = myListAdapter.getItem(position).getText();
        Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
        mListener.switchToDetails();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        String item = myListAdapter.getItem(position).getText();
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
    public void onListChange() {
        //called whenever ANYTHING changes
        mListener.autopush(); //TODO auto push to server?
    }

    public void addNewMatch(int[] teams, int matchnum, boolean isQual, String phonenum){
        mySchedule.add(new MatchDescriptor(getContext(), matchnum, teams));
        //TODO add isQual Boolean and phonenum String
    }

    public String getDatabase() {
        return "asdfasdfasdf"; //TODO generate csv string from mySchedule
    }

    public interface HubListFragListener {
        void autopush();
        void switchToDetails();
    }

}
