package org.ncfrcteams.frcscoutinghub2016.ui.hub.support;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.matchdata.Obstacle;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.Match;

import java.util.ArrayList;

/**
 * Created by pavan on 4/1/16.
 */
public class TempDialog1 {

    private static final String POSITIVE_TEXT = "Make QRs";
    private static final String NEGATIVE_TEXT = "Cancel";
    private Match match;
    Obstacle[] obstacles;
    private Dialog dialog;
    private View view;

    public TempDialog1(final Context context, final TempDialog1Listener listener, Match match) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final TempDialog1 thisDialog = this;

        view = LayoutInflater.from(context).inflate(R.layout.h_dialog_temp1, null);
        alert.setView(view);

        thisDialog.match = match;
        thisDialog.obstacles = match.getObstacles();

        ((EditText) view.findViewById(R.id.bar1)).setText(obstacles[0].getValue());
        ((EditText) view.findViewById(R.id.bar2)).setText(obstacles[1].getValue());
        ((EditText) view.findViewById(R.id.bar3)).setText(obstacles[2].getValue());
        ((EditText) view.findViewById(R.id.bar4)).setText(obstacles[3].getValue());

        ((EditText) view.findViewById(R.id.bar5)).setText(obstacles[5].getValue());
        ((EditText) view.findViewById(R.id.bar6)).setText(obstacles[6].getValue());
        ((EditText) view.findViewById(R.id.bar7)).setText(obstacles[7].getValue());
        ((EditText) view.findViewById(R.id.bar8)).setText(obstacles[8].getValue());

        final TempDialog1Listener dialogListener = listener;

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String[] barriersStrings = {
                        ((EditText) view.findViewById(R.id.bar1)).getText().toString(), //blue top = 1
                        ((EditText) view.findViewById(R.id.bar2)).getText().toString(), //2
                        ((EditText) view.findViewById(R.id.bar3)).getText().toString(), //3
                        ((EditText) view.findViewById(R.id.bar4)).getText().toString(), //4
                                                                                        //blue low = 0
                                                                                        //red low = 9
                        ((EditText) view.findViewById(R.id.bar5)).getText().toString(), //8
                        ((EditText) view.findViewById(R.id.bar6)).getText().toString(), //7
                        ((EditText) view.findViewById(R.id.bar7)).getText().toString(), //6
                        ((EditText) view.findViewById(R.id.bar8)).getText().toString(), //red bottom = 5
                };

                Obstacle[] barriers = new Obstacle[6];
                for (int i = 0; i < barriersStrings.length; i++) {
                    if (barriersStrings[i].equals("")){
                        barriers[i] = Obstacle.LOW_BAR;
                    } else{
                        barriers[i] = Obstacle.getObstacle(Integer.parseInt(barriersStrings[i]));
                    }
                }
                
                if(arrayContains(barriers, Obstacle.LOW_BAR)){
                    Toast.makeText(context, "Invalid Match Setup", Toast.LENGTH_SHORT).show();
                } else{
                    int teams[] = thisDialog.match.getTeams();
                    int matchNum = thisDialog.match.getMatchNum();
                    boolean isQual = thisDialog.match.isQual();
                    String phoneNum = thisDialog.match.getPhoneNum();

                    dialogListener.onTempDialog1(matchNum, isQual, teams, barriers, phoneNum);
                    Toast.makeText(context, "Generating QR Codes", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean arrayContains(Obstacle[] array, Obstacle item){
                for (Obstacle val : array) {
                    if(val == item){
                        return true;
                    }
                }
                return false;
            }
        };

        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        };

        alert.setPositiveButton(POSITIVE_TEXT, positiveListener);
        alert.setNegativeButton(NEGATIVE_TEXT, negativeListener);

        dialog = alert.create();
    }

    public void show() {
        dialog.show();
    }

    public interface TempDialog1Listener{
        void onTempDialog1(int matchNum, boolean isQual, int[] teams, Obstacle[] barriers, String phoneNum );
    }

}
