package org.ncfrcteams.frcscoutinghub2016.communication.http;

import android.content.Context;

/**
 * Created by pavan on 4/1/16.
 */
public class Poster {

    private static final String UPLOAD = "manage/auth/upload/index.php";
    private static final String DOWNLOAD = "manage/auth/download/index.php";
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

    public void reset(String url, String user, String pass, boolean pretty){
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.pretty = pretty;
    }

    public boolean isValidUser(){
        return user.equals("test"); //TODO better check based on results
    }

    public String getUser(){
        return this.user;
    }

    public String getPass(){
        return this.pass;
    }

    public String getURL(){
        return this.url;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setPass(String pass){
        this.pass = pass;
    }

    public void setURL(String url){
        this.url =  url;
    }

    public void uploadDatabase(String database) {
        String[][] POSTs = {{"user", user}, {"pass", pass}, {"filename", "data.csv"}};
        String[][] FILES = {{"file", database}};
        sendPostRequest(context, url + UPLOAD, POSTs, FILES, pretty, "upload");
    }

    public void downloadDatabase() {
        String[][] POSTs = {{"user", user}, {"pass", pass}, {"filename", "data.csv"}};
        String[][] FILES = { {"NONONO", "NONONO"} };
        sendPostRequest(context, url + DOWNLOAD, POSTs, FILES, false, "download");
    }

    public void uploadSchedule(String schedule) {
        String[][] POSTs = {{"user", user}, {"pass", pass}, {"filename", "schedule.csv"}};
        String[][] FILES = { {"file", schedule} };
        sendPostRequest(context, url + UPLOAD, POSTs, FILES, pretty, "upload");
    }

}
