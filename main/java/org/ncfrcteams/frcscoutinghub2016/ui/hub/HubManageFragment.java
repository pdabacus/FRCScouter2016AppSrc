package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ncfrcteams.frcscoutinghub2016.R;

public class HubManageFragment extends Fragment implements View.OnClickListener{

    private HubCreateFragListener mListener;
    public HubManageFragment() {
    }

    public static HubManageFragment newInstance() {
        return new HubManageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.h_frag_manage, container, false);

        view.findViewById(R.id.uploadDatabase).setOnClickListener(this);
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
            case R.id.uploadDatabase:
                mListener.POSTRequest("upload");
                break;
            case R.id.downloadDatabase:
                mListener.POSTRequest("download");
            default:
                break;
        }
    }

    public interface HubCreateFragListener {
        void POSTRequest(String requestType);
    }
}
