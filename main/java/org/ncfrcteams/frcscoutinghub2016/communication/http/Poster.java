package org.ncfrcteams.frcscoutinghub2016.communication.http;

import android.content.Context;
import android.util.Log;

/**
 * Created by pavan on 4/1/16.
 */
public class Poster implements DatabasePickerDialog.DatabasePickerListener, SignInDialog.SignInDialogListener{

    private static final String UPLOAD = "manage/auth/upload/index.php";
    private static final String DOWNLOAD = "manage/auth/download/index.php";
    private static final String GETFILES = "manage/auth/index.php";
    private Context context;
    private String url;
    private String user;
    private String pass;
    private boolean pretty;
    private POST.POSTListener listener;


    public Poster(Context context, String url, String user, String pass, boolean pretty, POST.POSTListener listener){
        this.context = context;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.pretty = pretty;
        this.listener = listener;
    }

    private void sendPostRequest(Context context, String url, String[][] POSTs, String[][] data,
                                   boolean pretty, String returnAddress) {

        new POST(context, url, POSTs, data, pretty, listener, returnAddress).execute();
    }

    public void signin(){
        new SignInDialog(context, this, user, pass, url).show();
    }

    public void uploadDatabase(String database) {
        String[][] POSTs = {{"user", user}, {"pass", pass}, {"filename", "data.csv"}};
        String[][] FILES = {{"file", database}};
        new DatabasePickerDialog(context, this, user, pass, url, UPLOAD, POSTs, FILES, pretty, "upload").show();
    }

    public void downloadDatabase() {
        String[][] POSTs = {{"user", user}, {"pass", pass}, {"filename", "data.csv"}};
        String[][] FILES = {{"NONONO", "NONONO"}};
        new DatabasePickerDialog(context, this, user, pass, url, DOWNLOAD, POSTs, FILES, false, "download").show();
    }

    @Override
    public void onPickedDatabase(String url, String[][] POSTs, String[][] FILES, boolean pretty,
                                 String returnAddress, String filename) {

        POSTs[2] = new String[] {"filename", filename};
        sendPostRequest(context, url, POSTs, FILES, pretty, returnAddress);
    }

    @Override
    public void onSignIn(String user, String pass, String url) {
        this.user = user;
        this.pass = pass;
        this.url = url;
    }
}
