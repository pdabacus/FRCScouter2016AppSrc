package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.Match;

public class HubContentsFragment extends Fragment {
    private static final String ARG_PARAM1 = "title";
    private int matchId;
    private TextView hubfrag1;
    private Match match;
    private HubContentsFragListener mListener;

    public HubContentsFragment() {
    }

    public static HubContentsFragment newInstance(int matchId) {
        HubContentsFragment fragment = new HubContentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchId = getArguments().getInt(ARG_PARAM1);
            match = mListener.getMatchFromId(matchId); //I checked, and OnAttach happens before OnCreate
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_frag_contents, container, false);
        hubfrag1 = (TextView) view.findViewById(R.id.hubfrag1);
        hubfrag1.setText(matchId);
        //TODO Make this entire fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HubContentsFragListener) {
            mListener = (HubContentsFragListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement HubContentsFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //called by activity. dont use this, instead call getActivity.bac
    public void killMe(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public interface HubContentsFragListener {
        Match getMatchFromId(int matchId);
    }
}
