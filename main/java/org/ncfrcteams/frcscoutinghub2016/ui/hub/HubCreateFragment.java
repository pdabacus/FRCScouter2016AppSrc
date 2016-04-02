package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.support.HubCreateDialog;

import java.util.ArrayList;

public class HubCreateFragment extends Fragment implements View.OnClickListener,
        HubCreateDialog.HubCreateDialogListener{

    private HubCreateFragListener mListener;
    public HubCreateFragment() {
    }

    public static HubCreateFragment newInstance() {
        return new HubCreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_frag_create, container, false);

        view.findViewById(R.id.createMatch).setOnClickListener(this);
        view.findViewById(R.id.downloadDatabase).setOnClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HubCreateFragListener) {
            mListener = (HubCreateFragListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement HubCreateFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.createMatch:
                ArrayList<String> matchTitles = mListener.getMatchTitles();
                new HubCreateDialog(getContext(), this, matchTitles).show();
                break;
            case R.id.downloadDatabase:
                mListener.downloadDatabase("schedule", "schedule");
            default:
                break;
        }
    }

    @Override
    public void onNewMatchCreate(int[] teams, int matchnum, boolean isQual, String phonenum) {
        mListener.addNewMatch(teams, matchnum, isQual, phonenum);
    }

    public interface HubCreateFragListener {
        void addNewMatch(int[] teams, int matchnum, boolean isQual, String phonenum);
        ArrayList<String> getMatchTitles();
        void downloadDatabase(String user, String pass);
    }
}
