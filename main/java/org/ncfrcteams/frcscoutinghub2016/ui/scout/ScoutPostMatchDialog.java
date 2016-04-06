package org.ncfrcteams.frcscoutinghub2016.ui.scout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import org.ncfrcteams.frcscoutinghub2016.R;

/**
 * Created by pavan on 3/20/16.
 */
public class ScoutPostMatchDialog implements View.OnClickListener{

    private static final String POSITIVE_TEXT = "Submit";
    private static final String NEGATIVE_TEXT = "Keep Scouting";
    private int challenge;
    private int climb;
    private EditText comments;
    private Dialog dialog;
    private View view;

    public ScoutPostMatchDialog(Context context, PostDialogListener listener, int challenge, int climb, String comment) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final ScoutPostMatchDialog thisDialog = this;

        thisDialog.challenge = challenge;
        thisDialog.climb = climb;

        view = LayoutInflater.from(context).inflate(R.layout.s_dialog_post, null);
        alert.setView(view);

        view.findViewById(R.id.challengeNo).setOnClickListener(thisDialog);
        view.findViewById(R.id.challengeTried).setOnClickListener(thisDialog);
        view.findViewById(R.id.challengeYes).setOnClickListener(thisDialog);
        view.findViewById(R.id.climbNo).setOnClickListener(thisDialog);
        view.findViewById(R.id.climbTried).setOnClickListener(thisDialog);
        view.findViewById(R.id.climbYes).setOnClickListener(thisDialog);

        setupRadios();

        thisDialog.comments = (EditText) view.findViewById(R.id.comments);
        thisDialog.comments.setText(comment);

        final PostDialogListener dialogListener = listener;

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialogListener.onPostConfirm(1, thisDialog.challenge, thisDialog.climb,
                        thisDialog.comments.getText().toString());
            }
        };

        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialogListener.onPostConfirm(0, thisDialog.challenge, thisDialog.climb,
                        thisDialog.comments.getText().toString());
            }
        };

        alert.setPositiveButton(POSITIVE_TEXT, positiveListener);
        alert.setNegativeButton(NEGATIVE_TEXT, negativeListener);

        dialog = alert.create();
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.challengeNo:
                challenge = 0;
                break;
            case R.id.challengeTried:
                challenge = 1;
                break;
            case R.id.challengeYes:
                challenge = 2;
                break;
            case R.id.climbNo:
                climb = 0;
                break;
            case R.id.climbTried:
                climb = 1;
                break;
            case R.id.climbYes:
                climb = 2;
                break;
            default:
                break;
        }
        setupRadios();
    }

    public void setCheck(RadioButton radio){
        switch(radio.getId()){
            case R.id.challengeNo:
                radio.setChecked(challenge == 0);
                break;
            case R.id.challengeTried:
                radio.setChecked(challenge == 1);
                break;
            case R.id.challengeYes:
                radio.setChecked(challenge == 2);
                break;
            case R.id.climbNo:
                radio.setChecked(climb == 0);
                break;
            case R.id.climbTried:
                radio.setChecked(climb == 1);
                break;
            case R.id.climbYes:
                radio.setChecked(climb == 2);
                break;
            default:
                break;
        }
    }

    public void setupRadios(){
        switch(climb){
            case 0:
                ((RadioButton) view.findViewById(R.id.challengeNo)).setEnabled(true);
                ((RadioButton) view.findViewById(R.id.challengeTried)).setEnabled(true);
                ((RadioButton) view.findViewById(R.id.challengeYes)).setEnabled(true);
                break;
            case 1:
                if (challenge == 0){
                    challenge = 1;
                }
                ((RadioButton) view.findViewById(R.id.challengeNo)).setEnabled(false);
                ((RadioButton) view.findViewById(R.id.challengeTried)).setEnabled(true);
                ((RadioButton) view.findViewById(R.id.challengeYes)).setEnabled(true);
                break;
            default:
                challenge = 2;
                ((RadioButton) view.findViewById(R.id.challengeNo)).setEnabled(false);
                ((RadioButton) view.findViewById(R.id.challengeTried)).setEnabled(false);
                ((RadioButton) view.findViewById(R.id.challengeYes)).setEnabled(false);
                break;
        }
        setCheck((RadioButton) view.findViewById(R.id.challengeNo));
        setCheck((RadioButton) view.findViewById(R.id.challengeTried));
        setCheck((RadioButton) view.findViewById(R.id.challengeYes));
        setCheck((RadioButton) view.findViewById(R.id.climbNo));
        setCheck((RadioButton) view.findViewById(R.id.climbTried));
        setCheck((RadioButton) view.findViewById(R.id.climbYes));
    }

    public interface PostDialogListener {
        void onPostConfirm(int state, int challenge, int climb, String comment);
    }

}
