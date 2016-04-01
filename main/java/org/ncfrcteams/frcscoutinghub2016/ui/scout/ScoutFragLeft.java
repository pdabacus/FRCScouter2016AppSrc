package org.ncfrcteams.frcscoutinghub2016.ui.scout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.ncfrcteams.frcscoutinghub2016.R;

public class ScoutFragLeft extends Fragment implements View.OnClickListener{

    private int orientation;
    private OnFragListener mListener;
    private Button leftHighGoal;
    private Button leftLowGoal;

    public ScoutFragLeft() {
    }

    public static ScoutFragLeft newInstance(){
        return new ScoutFragLeft();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            orientation = ScoutMainActivity.myMatchRecord.get("Orientation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_frag_left, container, false);
        ImageView leftGoal = (ImageView) view.findViewById(R.id.leftGoal);
        leftHighGoal = (Button) view.findViewById(R.id.leftHighGoal);
        leftHighGoal.setOnClickListener(this);
        leftLowGoal = (Button) view.findViewById(R.id.leftLowGoal);
        leftLowGoal.setOnClickListener(this);
        view.findViewById(R.id.leftSuccess).setOnClickListener(this);
        view.findViewById(R.id.leftFailure).setOnClickListener(this);

        if(orientation == 2){
            leftGoal.setImageResource(R.drawable.goal_red);
            leftGoal.setRotation(180);
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
        switch(view.getId()){
            case R.id.leftHighGoal:
                ScoutMainActivity.myMatchRecord.setActiveButton("Left High");
                break;
            case R.id.leftLowGoal:
                ScoutMainActivity.myMatchRecord.setActiveButton("Left Low");
                break;
            case R.id.leftSuccess:
                String button = ScoutMainActivity.myMatchRecord.getActiveButton();
                ScoutMainActivity.myMatchRecord.increment(button, 1);
                setButtonText(button);
                break;
            case R.id.leftFailure:
                String mybutton = ScoutMainActivity.myMatchRecord.getActiveButton();
                ScoutMainActivity.myMatchRecord.increment(mybutton, 0);
                setButtonText(mybutton);
                break;
            default:
                break;
        }
    }

    private void setButtonText(String button){
        String text = ScoutMainActivity.myMatchRecord.getString(button, true);
        if(! button.equals("None")) {
            switch (button) {
                case "Left High":
                    leftHighGoal.setText(text);
                    break;
                case "Left Low":
                    leftLowGoal.setText(text);
                    break;
                default:
                    break;
            }
        }
    }

    public void updateFragment(){
        setButtonText("Left High");
        setButtonText("Left Low");
    }

    public interface OnFragListener {
        void onFrag1Interaction(Uri uri);
    }

}
