package org.ncfrcteams.frcscoutinghub2016.ui.hub;

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

import java.util.ArrayList;

/**
 * Created by pavan on 3/31/16.
 */
public class HubCreateMatchDialog {

    private static final String POSITIVE_TEXT = "Create";
    private static final String NEGATIVE_TEXT = "Cancel";
    private String[] matchtitles;
    private Dialog dialog;
    private View view;

    public HubCreateMatchDialog(final Context context, final HubCreateDialogListener listener, ArrayList<String> matchtitles) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final HubCreateMatchDialog thisDialog = this;

        view = LayoutInflater.from(context).inflate(R.layout.h_dialog_create, null);
        alert.setView(view);

        this.matchtitles = new String[matchtitles.size()];
        for(int i = 0; i < matchtitles.size(); i++) {
            this.matchtitles[i] = matchtitles.get(i).trim();
        }

        String phonenum = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        ((EditText) view.findViewById(R.id.phonenum)).setText(phonenum);

        final HubCreateDialogListener dialogListener = listener;

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String[] teamsString = {
                        ((EditText) view.findViewById(R.id.red1)).getText().toString(),
                        ((EditText) view.findViewById(R.id.red2)).getText().toString(),
                        ((EditText) view.findViewById(R.id.red3)).getText().toString(),
                        ((EditText) view.findViewById(R.id.blue1)).getText().toString(),
                        ((EditText) view.findViewById(R.id.blue2)).getText().toString(),
                        ((EditText) view.findViewById(R.id.blue3)).getText().toString(),
                };

                int[] teams = new int[6];
                for (int i = 0; i < teamsString.length; i++) {
                    if (teamsString[i].equals("")){
                        teams[i] = 0;
                    } else{
                        teams[i] = Integer.parseInt(teamsString[i]);
                    }
                }

                String matchNumString = ((EditText) view.findViewById(R.id.matchnum)).getText().toString();
                int matchnum = (matchNumString.equals("") ? 0 : Integer.parseInt(matchNumString));
                boolean isQual = ((RadioButton) view.findViewById(R.id.qual)).isChecked();
                String phonenum = ((EditText) view.findViewById(R.id.phonenum)).getText().toString();

                if(repeatsIn(teams) || arrayContains(teams, 0) || matchnum == 0 || phonenum.equals("")){
                    Toast.makeText(context, "Invalid Match Setup", Toast.LENGTH_SHORT).show();
                } else{
                    boolean isRedo = isRedo(thisDialog.matchtitles, (isQual ? "Qual " : "Elim ") + matchnum);
                    boolean addRedosEnabled = false; //TODO add as parameter settings (this value used elsewhere)
                    if(!isRedo || addRedosEnabled) {
                        dialogListener.onNewMatchCreate(teams, matchnum, isQual, phonenum);
                        Toast.makeText(context, (isQual ? "Qual " : "Elim ") + matchnum + (isRedo ? "Redo " : "")
                                + " Created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Match Already Exists", Toast.LENGTH_LONG).show();;
                    }
                }
            }

            private boolean repeatsIn(int[] teams) {
                for(int i = 0; i < teams.length; i++){
                    for(int j = i; j < teams.length; j++){
                        if (i != j && teams[i] == teams[j]){
                            return true;
                        }
                    }
                }
                return false;
            }

            private boolean arrayContains(int[] array, int item){
                for (int val : array) {
                    if(val == item){
                        return true;
                    }
                }
                return false;
            }

            private boolean isRedo(String[] array, String item){
                for (String val : array) {
                    if(val.equals(item)){
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

    public interface HubCreateDialogListener{
        void onNewMatchCreate(int[] teams, int matchnum, boolean isQual, String phonenum);
    }

}
