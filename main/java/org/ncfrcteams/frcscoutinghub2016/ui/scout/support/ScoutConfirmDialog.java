package org.ncfrcteams.frcscoutinghub2016.ui.scout.support;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kyle Brown on 3/15/2016.
 */
public class ScoutConfirmDialog {
    private static final String POSITIVE_TEXT = "Success";
    private static final String NEUTRAL_TEXT = "Eh";
    private static final String NEGATIVE_TEXT = "Failure";
    private Dialog dialog;
    private final String identifier;

    /**
     *
     * @param context the context of the created Dialog
     * @param cL the ConfirmationListener that will receive confirmation from this Dialog
     * @param title the title displayed by the Dialog
     * @param prompt the message of this Dialog
     * @param identifier the String that will be sent back with the integer state value
     * @param hasNeutral whether or not this Dialog should have a neutral button
     */
    public ScoutConfirmDialog(Context context, ConfirmationListener cL, String title, String prompt, String identifier, boolean hasNeutral) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        this.identifier = identifier;

        alert.setTitle(title);
        alert.setMessage(prompt);

        final ScoutConfirmDialog thisDialog = this;
        final ConfirmationListener confirmationListener = cL;

        //****************************************Normal Buttons************************************
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirmationListener.onConfirm(thisDialog.getIdentifier(),2);
            }
        };

        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                confirmationListener.onConfirm(thisDialog.getIdentifier(),0);
            }
        };

        alert.setPositiveButton(POSITIVE_TEXT, positiveListener);
        alert.setNegativeButton(NEGATIVE_TEXT, negativeListener);

        //****************************************Neutral Button************************************
        if(hasNeutral) {
            DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    confirmationListener.onConfirm(thisDialog.getIdentifier(),1);
                }
            };

            alert.setNeutralButton(NEUTRAL_TEXT, neutralListener);
        }

        //Create
        dialog = alert.create();
    }

    /**
     * Displays the Dialog
     * If you are going to change the animation that it is displayed with do so here
     */
    public void show() {
        dialog.show();
    }

    /**
     * @return the identifier that was given to specify what was confirmed
     */
    public String getIdentifier() {
        return identifier;
    }

    public interface ConfirmationListener {
        void onConfirm(String identifier, int state);
    }
}
