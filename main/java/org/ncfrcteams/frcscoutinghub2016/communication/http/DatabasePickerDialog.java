package org.ncfrcteams.frcscoutinghub2016.communication.http;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by pavan on 4/9/16.
 */
public class DatabasePickerDialog{

    private static final String NEGATIVE_TEXT = "Cancel";
    private static final String GETFILES = "manage/auth/index.php";
    private Dialog dialog;
    private ListView view;
    private ArrayAdapter<String> adapter;
    private String urlext;
    private String[][] POSTs;
    private String[][] FILES;
    private boolean pretty;
    private String returnAddress;

    public DatabasePickerDialog(final Context context, final DatabasePickerListener listener, String user,
                                String pass, String url, String urlext, String[][] POSTs, String[][] FILES,
                                boolean pretty, String returnAddress) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final DatabasePickerDialog thisDialog = this;

        thisDialog.urlext = url + urlext;
        thisDialog.POSTs = POSTs;
        thisDialog.FILES = FILES;
        thisDialog.pretty = pretty;
        thisDialog.returnAddress = returnAddress;

        thisDialog.view = new ListView(context);
        thisDialog.adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1, new ArrayList<String>());
        alert.setView(thisDialog.view);
        alert.setTitle("Pick Database");
        thisDialog.view.setAdapter(thisDialog.adapter);
        thisDialog.view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onPickedDatabase(thisDialog.urlext, thisDialog.POSTs, thisDialog.FILES, thisDialog.pretty,
                        thisDialog.returnAddress, thisDialog.adapter.getItem(position));
                dialog.dismiss();
            }
        });

        String[][] P = {{"user", user}, {"pass", pass}, {"type", "file"}};
        String[][] F = { {"NONONO", "NONONO"} };

        new POST(context, url + GETFILES, P, F, false, new POST.POSTListener() {
            @Override
            public void POSTResult(String returnAddress, String result) {
                ArrayList<String> filenames = new ArrayList<>();
                for (String pt : result.split(",")) {
                    filenames.add(pt.trim());
                }
                thisDialog.update(filenames);
            }
        }, "download").execute();

        final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //dismiss
            }
        };

        alert.setNegativeButton(NEGATIVE_TEXT, negativeListener);

        dialog = alert.create();
    }


    public void show() {
        dialog.show();
    }

    public void update(ArrayList<String> filenames) {
        adapter.clear();
        adapter.addAll(filenames);
    }

    public interface DatabasePickerListener{
        void onPickedDatabase(String urlext, String[][] POSTs, String[][] FILES, boolean pretty,
                              String returnAddress, String filename);
    }

}
