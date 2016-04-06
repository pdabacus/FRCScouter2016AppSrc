package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule.Match;


/**
 * Created by pavan on 4/1/16.
 */
public class HubEditBarriersDialog {

    private static final String POSITIVE_TEXT = "Generate QRs";
    private static final String NEGATIVE_TEXT = "Cancel";

    private Dialog dialog;
    private Context context;
    private View view;
    private Match match;
    private TempDialog1Listener listener;

    public HubEditBarriersDialog(final Context context, final TempDialog1Listener listener, Match match) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);

        final HubEditBarriersDialog thisDialog = this;

        thisDialog.context = context;
        thisDialog.view = LayoutInflater.from(context).inflate(R.layout.h_dialog_temp1, null);
        thisDialog.match = match;
        thisDialog.listener = listener;

        alert.setView(view);

        ((TextView) view.findViewById(R.id.tempMatch)).setText(thisDialog.match.getTitle());

        int[] barriers = match.barriers();

        ((EditText) view.findViewById(R.id.bar1)).setText(barriers[0] == 0 ? "" : String.valueOf(barriers[0]));
        ((EditText) view.findViewById(R.id.bar2)).setText(barriers[1] == 0 ? "" : String.valueOf(barriers[1]));
        ((EditText) view.findViewById(R.id.bar3)).setText(barriers[2] == 0 ? "" : String.valueOf(barriers[2]));
        ((EditText) view.findViewById(R.id.bar4)).setText(barriers[3] == 0 ? "" : String.valueOf(barriers[3]));
        ((EditText) view.findViewById(R.id.bar5)).setText(barriers[4] == 0 ? "" : String.valueOf(barriers[4]));
        ((EditText) view.findViewById(R.id.bar6)).setText(barriers[5] == 0 ? "" : String.valueOf(barriers[5]));
        ((EditText) view.findViewById(R.id.bar7)).setText(barriers[6] == 0 ? "" : String.valueOf(barriers[6]));
        ((EditText) view.findViewById(R.id.bar8)).setText(barriers[7] == 0 ? "" : String.valueOf(barriers[7]));

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String[] barriersStrings = {
                        ((EditText) view.findViewById(R.id.bar1)).getText().toString(),
                        ((EditText) view.findViewById(R.id.bar2)).getText().toString(),
                        ((EditText) view.findViewById(R.id.bar3)).getText().toString(),
                        ((EditText) view.findViewById(R.id.bar4)).getText().toString(),
                        ((EditText) view.findViewById(R.id.bar5)).getText().toString(),
                        ((EditText) view.findViewById(R.id.bar6)).getText().toString(),
                        ((EditText) view.findViewById(R.id.bar7)).getText().toString(),
                        ((EditText) view.findViewById(R.id.bar8)).getText().toString(),
                };

                int[] barriers = new int[8];
                for (int i = 0; i < barriersStrings.length; i++) {
                    if (barriersStrings[i].equals("") || barriersStrings[i].equals("9")){
                        barriers[i] = 0;
                    } else{
                        barriers[i] = Integer.parseInt(barriersStrings[i]);
                    }
                }

                thisDialog.match.setBarriers(barriers);

                if(arrayContains(barriers, 0)){
                    Toast.makeText(thisDialog.context, "Invalid Match Setup", Toast.LENGTH_SHORT).show();
                } else{
                    thisDialog.listener.generateQRs(thisDialog.match);
                    Toast.makeText(thisDialog.context, "Generating QR Codes", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean arrayContains(int[] array, int item){
                for (int val : array) {
                    if(val == item){
                        return true;
                    }
                }
                return false;
            }
        };

        final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                thisDialog.dialog.dismiss();
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton(POSITIVE_TEXT, positiveListener);
        alert.setNegativeButton(NEGATIVE_TEXT, negativeListener);

        dialog = alert.create();
    }

    public void show() {
        dialog.show();
    }

    public interface TempDialog1Listener{
        void generateQRs(Match match);
    }

}
