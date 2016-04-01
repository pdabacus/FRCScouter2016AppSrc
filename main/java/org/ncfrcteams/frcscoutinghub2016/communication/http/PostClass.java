package org.ncfrcteams.frcscoutinghub2016.communication.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.webkit.WebView;

import org.ncfrcteams.frcscoutinghub2016.ui.hub.HubActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pavan on 3/19/16.
 */
public class PostClass extends AsyncTask<String, Void, Void> {

    private final Context context;
    private ProgressDialog progress;
    private String urlstring;
    private boolean pretty;
    private String user;
    private String pass;
    private String data;
    private String boundary = "===" + System.currentTimeMillis() + "===";

    //TODO make constructor use arrays of POST vars and FILES

    public PostClass(Context context, String urlstring, String user,
                     String pass, String data, boolean pretty){
        this.context = context;
        this.urlstring = urlstring;
        this.pretty = pretty;
        this.user = user;
        this.pass = pass;
        this.data = data;
    }

    protected void onPreExecute(){
        progress = new ProgressDialog(this.context);
        progress.setMessage("Uploading Data...");
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            //build http post and send and get
            URL url = new URL(urlstring);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //String urlParameters = "team=" + user + "&pass=" + pass + "&file=" + data;
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            //TODO use for each loops for all of the POST vars and FILES

            //add team=user
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"team\"").append("\r\n");
            writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n\r\n");
            writer.append(user).append("\r\n");
            writer.flush();

            //add pass=pass
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"pass\"").append("\r\n");
            writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n\r\n");
            writer.append(pass).append("\r\n");
            writer.flush();

            //add pretty=detailed
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"pretty\"").append("\r\n");
            writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n\r\n");
            writer.append(pretty ? "true" : "false").append("\r\n");
            writer.flush();

            //add file=datafile
            File datafile = new File(data);
            String fileName = datafile.getName();
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileName);
            writer.append("\"\r\n").append("Content-Type: text/plain");
            writer.append("\r\n").append("Content-Transfer-Encoding: binary").append("\r\n\r\n");
            writer.append(data).append("\r\n"); //if file type is plain text
            writer.flush();

            //close writer
            writer.append("\r\n").flush();
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.close();

            int responseCode = connection.getResponseCode();

            //format output meta data for textview
            final StringBuilder output = new StringBuilder();
            if(!pretty){
                output.append(" Request URL: ").append(url);
                //output.append("\n Request Parameters: " + urlParameters);
                output.append("\n Response Code: ").append(responseCode);
                output.append("\n Type: " + "POST");
                output.append("\nResponse:\n");
            }

            //make string for output
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder responseOutput = new StringBuilder();
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            br.close();
            connection.disconnect();

            //add output to textview string
            output.append(responseOutput.toString());
            ((HubActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                    WebView webView = new WebView(context);
                    webView.loadData(output.toString(), "text/html; charset=UTF-8", null);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setView(webView);
                    dialog.setPositiveButton("Okay", null);
                    dialog.show();
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}