package org.ncfrcteams.frcscoutinghub2016.ui.dialogs;

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

/**
 * Created by pavan on 3/31/16.
 */
public class HubCreateDialog{

    private static final String POSITIVE_TEXT = "Create";
    private static final String NEGATIVE_TEXT = "Cancel";
    private Dialog dialog;
    private View view;

    public HubCreateDialog(final Context context, HubCreateDialogListener listener) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final HubCreateDialog thisDialog = this;

        view = LayoutInflater.from(context).inflate(R.layout.h_dialog_create, null);
        alert.setView(view);

        String phonenum = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        ((EditText) view.findViewById(R.id.blue3)).setText(phonenum);

        final HubCreateDialogListener dialogListener = listener;

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int[] teams = {
                        Integer.parseInt(((EditText) view.findViewById(R.id.red1)).getText().toString()),
                        Integer.parseInt(((EditText) view.findViewById(R.id.red2)).getText().toString()),
                        Integer.parseInt(((EditText) view.findViewById(R.id.red3)).getText().toString()),
                        Integer.parseInt(((EditText) view.findViewById(R.id.blue1)).getText().toString()),
                        Integer.parseInt(((EditText) view.findViewById(R.id.blue2)).getText().toString()),
                        Integer.parseInt(((EditText) view.findViewById(R.id.blue3)).getText().toString()),
                };
                int matchnum = Integer.parseInt(((EditText) view.findViewById(R.id.matchnum)).getText().toString());
                boolean isQual = ((RadioButton) view.findViewById(R.id.qual)).isChecked();
                String phonenum = ((EditText) view.findViewById(R.id.phonenum)).getText().toString();

                dialogListener.onNewMatchCreate(teams, matchnum, isQual, phonenum);
                Toast.makeText(context, (isQual ? "Qual " : "Elim ") + matchnum + " Created", Toast.LENGTH_SHORT).show();
            }
        };

        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context, "Cancelled Match Creation", Toast.LENGTH_SHORT).show();
            }
        };

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
