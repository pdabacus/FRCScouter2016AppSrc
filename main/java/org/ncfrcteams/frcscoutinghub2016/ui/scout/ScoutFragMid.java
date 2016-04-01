package org.ncfrcteams.frcscoutinghub2016.ui.scout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.ui.dialogs.ScoutConfirmDialog;
import org.ncfrcteams.frcscoutinghub2016.R;

import java.util.ArrayList;
import java.util.List;


public class ScoutFragMid extends Fragment implements View.OnClickListener, ScoutConfirmDialog.ConfirmationListener {

    private int orientation;
    private int mode;
    private OnFragListener mListener;
    private List<ImageView> barriers = new ArrayList<>();
    private int[] arrangement = new int[10];

    public ScoutFragMid() {
    }

    public static ScoutFragMid newInstance() {
        return new ScoutFragMid();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = ScoutMainActivity.myMatchRecord.get("Orientation");
        mode = ScoutMainActivity.myMatchRecord.get("Mode");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_frag_mid, container, false);
        arrangement = ScoutMainActivity.myMatchRecord.getBarriers();

        for(int i = 0; i < 10; i++){
            int id = getResources().getIdentifier("barrier"+i, "id", getActivity().getPackageName());
            ImageView barrier = (ImageView) view.findViewById(id);
            barrier.setOnClickListener(this);
            barriers.add(barrier);
            if(0<i && i<9){
                int icon = getResources().getIdentifier("barrier" + arrangement[i], "drawable",
                        getActivity().getPackageName());
                barrier.setImageResource(icon);
            }
        }

        if(orientation == 2){
            view.findViewById(R.id.middle).setRotation(180);
        }

        updateFragment();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragListener) {
            mListener = (OnFragListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view){
        String idstr = view.getResources().getResourceName(view.getId());
        int id = Integer.parseInt(String.valueOf(idstr.charAt(idstr.length() - 1)));
        int mapId = arrangement[id];
        ScoutMainActivity.myMatchRecord.setActiveButton("Barrier " + mapId);
        String barriername = barrierName(mapId);
        String barrierscore = ScoutMainActivity.myMatchRecord.getString("Barrier " + mapId, false);
        new ScoutConfirmDialog(getActivity(), this, barriername + ":", barrierscore.substring(1), String.valueOf(mapId), false).show();
    }

    @Override
    public void onConfirm(String identifier, int state) {
        int mapId = Integer.parseInt(identifier);
        String barriername = barrierName(mapId);
        ScoutMainActivity.myMatchRecord.increment("Barrier " + mapId, 10 * (mapId == 0 ? 9 : mapId) + (state == 2 ? 1 : 0));
        String barrierscore = ScoutMainActivity.myMatchRecord.getString("Barrier " + mapId, false);
        Toast.makeText(getContext(), barriername + barrierscore , Toast.LENGTH_SHORT).show();
    }

    public void updateFragment(){
        //set text?
    }

    public String barrierName(int id){
        switch(id){
            case 1:
                return "Portcullis";
            case 2:
                return "Cheval de Frise";
            case 3:
                return "Ramparts";
            case 4:
                return "Moat";
            case 5:
                return "Draw Bridge";
            case 6:
                return "Sally Port";
            case 7:
                return "Rock Wall";
            case 8:
                return "Rough Terrain";
            default:
                return "Low Bar";
        }
    }

    public interface OnFragListener {
        void onFrag2Interaction(Uri uri);
    }

}
