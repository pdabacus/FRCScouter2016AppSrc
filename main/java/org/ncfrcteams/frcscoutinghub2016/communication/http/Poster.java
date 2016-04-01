package org.ncfrcteams.frcscoutinghub2016.communication.http;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by pavan on 4/1/16.
 */
public class Poster {

    private static final String CREATE = "create/view/index.php";
    private static final String UPLOAD = "upload/view/index.php";
    private static final String APPEND = "append/view/index.php";
    private static final String DOWNLOAD = "download/view/index.php";
    private static final String DELETE = "delete/view/index.php";
    private Context context;
    private String url;
    private String user;
    private String pass;
    private boolean pretty;


    public Poster(Context context, String url, String user, String pass, boolean pretty){
        this.context = context;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.pretty = pretty;
    }

    private void sendPostRequest(Context context, String url, String[][] POSTs, String[][] data, boolean pretty) {
        new POST(context, url, POSTs, data, pretty).execute();
    }

    public void reset(String url, String user, String pass, boolean pretty){
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.pretty = pretty;
    }

    public String getUser(){
        return this.user;
    }

    public void setURL(String url){
        this.url =  url;
    }

    public void createDatabase() {
        String[][] POSTs = {{"team", user}, {"pass", pass}};
        String[][] FILES = null;
        sendPostRequest(context, url + CREATE, POSTs, FILES, pretty);
    }

    public void uploadDatabase(String database) {
        String[][] POSTs = {{"team", user}, {"pass", pass}};
        String[][] FILES = {{"file", database}};
        sendPostRequest(context, url + UPLOAD, POSTs, FILES, pretty);
    }

    public void appendToDatabase(String database) {
        String[][] POSTs = {{"team", user}, {"pass", pass}, {"matchrec", database}};
        String[][] FILES = null;
        sendPostRequest(context, url + APPEND, POSTs, FILES, pretty);
    }

    public void downloadDatabase() {
        String[][] POSTs = {{"team", user}, {"pass", pass}};
        String[][] FILES = null;
        sendPostRequest(context, url + DOWNLOAD, POSTs, FILES, pretty);
    }

    public void deleteDatabase() {
        String[][] POSTs = {{"team", user}, {"pass", pass}};
        String[][] FILES = null;
        sendPostRequest(context, url + DELETE, POSTs, FILES, pretty);
    }

}
