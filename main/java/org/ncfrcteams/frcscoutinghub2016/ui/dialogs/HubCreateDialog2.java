package org.ncfrcteams.frcscoutinghub2016.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;

/**
 * Created by pavan on 3/31/16.
 */
public class HubCreateDialog2 {

    private static final String POSITIVE_TEXT = "Create";
    private static final String NEGATIVE_TEXT = "Cancel";

    private Dialog dialog;
    private View view;

    private final EditText matchNum;

    private final EditText red1;
    private final EditText red2;
    private final EditText red3;
    private final EditText blue1;
    private final EditText blue2;
    private final EditText blue3;

    //private final CheckBox isPlayoff;


    public HubCreateDialog2(final Context context, HubCreateDialogListener listener) {
        //***********************************Setup**************************************************
        final HubCreateDialog2 thisDialog = this;

        view = LayoutInflater.from(context).inflate(R.layout.h_dialog_create, null);

        matchNum = (EditText) view.findViewById(R.id.matchnum);

        red1 = (EditText) view.findViewById(R.id.red1);
        red2 = (EditText) view.findViewById(R.id.red2);
        red3 = (EditText) view.findViewById(R.id.red3);
        blue1 = (EditText) view.findViewById(R.id.blue1);
        blue2 = (EditText) view.findViewById(R.id.blue2);
        blue3 = (EditText) view.findViewById(R.id.blue3);

        //isPlayoff = (CheckBox) view.findViewById(R.id.checkBox);

        final HubCreateDialogListener dialogListener = listener;

        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        final String phoneNum = tMgr.getLine1Number();

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int[] teams = {
                        Integer.parseInt(red1.getText().toString()),
                        Integer.parseInt(red2.getText().toString()),
                        Integer.parseInt(red3.getText().toString()),
                        Integer.parseInt(blue1.getText().toString()),
                        Integer.parseInt(blue2.getText().toString()),
                        Integer.parseInt(blue3.getText().toString()),
                };

                int matchNum = Integer.parseInt(thisDialog.matchNum.getText().toString());

                //boolean isQual = !isPlayoff.isChecked();

                dialogListener.onNewMatchCreate(teams, matchNum,
                        true, phoneNum); //true = isQual
            }
        };

        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, "Cancelled Match Creation", Toast.LENGTH_SHORT).show();
            }
        };

        //********************************Creation********************************
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
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
