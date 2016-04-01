package org.ncfrcteams.frcscoutinghub2016.communication.qr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

/**
 * Created by Kyle Brown on 3/20/2016.
 */
public class MultiQRDialog {
    private Dialog dialog;

    public MultiQRDialog(FragmentActivity activityContext, String[] titles, String[] contents) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activityContext);

        QRFragmentAdapter mQRFragmentAdapter = new QRFragmentAdapter(activityContext.getSupportFragmentManager(), titles, contents);
        ViewPager viewPager = new ViewPager(activityContext);
        viewPager.setAdapter(mQRFragmentAdapter);

        alert.setView(viewPager);
        alert.setTitle("QR Codes");

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        };

        alert.setPositiveButton("Done", listener);

        dialog = alert.create();
    }

    public void show() {
        dialog.show();
    }
}
