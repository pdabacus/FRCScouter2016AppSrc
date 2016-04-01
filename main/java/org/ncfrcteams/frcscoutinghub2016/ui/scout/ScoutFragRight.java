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


public class ScoutFragRight extends Fragment implements View.OnClickListener{

    private int orientation;
    private OnFragListener mListener;
    private Button rightHighGoal;
    private Button rightLowGoal;

    public ScoutFragRight() {
    }

    public static ScoutFragRight newInstance() {
        return new ScoutFragRight();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = ScoutMainActivity.myMatchRecord.get("Orientation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.s_frag_right, container, false);
        ImageView rightGoal = (ImageView) view.findViewById(R.id.rightGoal);
        rightHighGoal = (Button) view.findViewById(R.id.rightHighGoal);
        rightHighGoal.setOnClickListener(this);
        rightLowGoal = (Button) view.findViewById(R.id.rightLowGoal);
        rightLowGoal.setOnClickListener(this);
        Button rightSuccess = (Button) view.findViewById(R.id.rightSuccess);
        rightSuccess.setOnClickListener(this);
        Button rightFailure = (Button) view.findViewById(R.id.rightFailure);
        rightFailure.setOnClickListener(this);

        if(orientation == 2){
            rightGoal.setImageResource(R.drawable.goal_blue);
            rightGoal.setRotation(180);
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
            case R.id.rightHighGoal:
                ScoutMainActivity.myMatchRecord.setActiveButton("Right High");
                break;
            case R.id.rightLowGoal:
                ScoutMainActivity.myMatchRecord.setActiveButton("Right Low");
                break;
            case R.id.rightSuccess:
                String button = ScoutMainActivity.myMatchRecord.getActiveButton();
                ScoutMainActivity.myMatchRecord.increment(button, 1);
                setButtonText(button);
                break;
            case R.id.rightFailure:
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
        switch(button){
            case "Right High":
                rightHighGoal.setText(text);
                break;
            case "Right Low":
                rightLowGoal.setText(text);
                break;
            default:
                break;
        }
    }

    public void updateFragment(){
        setButtonText("Right High");
        setButtonText("Right Low");
    }

    public interface OnFragListener {
        void onFrag3Interaction(Uri uri);
    }
}
