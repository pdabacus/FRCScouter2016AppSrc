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
public class POST extends AsyncTask<String, Void, Void> {

    private final Context context;
    private ProgressDialog progress;
    private String urlstring;
    private boolean pretty;
    private String[][] POSTs;
    private String[][] FILES;
    private POSTListener listener;
    private String returnAddress;

    private String boundary = "===" + System.currentTimeMillis() + "===";

    public POST(Context context, String urlstring, String[][] POSTs, String[][] FILES,
                boolean pretty, POSTListener listener, String returnAddress) {
        this.context = context;
        this.urlstring = urlstring;
        this.pretty = pretty;
        this.POSTs = POSTs;
        this.FILES = FILES;
        this.listener = listener;
        this.returnAddress = returnAddress;
    }

    protected void onPreExecute(){
        progress = new ProgressDialog(this.context);
        progress.setMessage(Character.toUpperCase(this.returnAddress.charAt(0)) + this.returnAddress.substring(1) + "ing Data...");
        progress.setIndeterminate(true);
        progress.show();
    }

    private void addPOST(PrintWriter writer, String name, String value){
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"\r\n");
        writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n\r\n").append(value).append("\r\n");
        writer.flush();
    }

    private void addFILE(PrintWriter writer, String name, String value){
        writer.append("--").append(boundary).append("\r\n").append("Content-Disposition: form-data; name=\"");
        writer.append(name).append("\"; filename=\"").append((new File(value)).getName()).append("\"\r\n");
        writer.append("Content-Type: text/plain").append("\r\n").append("Content-Transfer-Encoding: binary");
        writer.append("\r\n\r\n").append(value).append("\r\n");
        writer.flush();
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

            //add pretty, POSTs, and FILES
            addPOST(writer, "pretty", (pretty ? "true" : "false"));
            for(String[] pair : POSTs) {
                addPOST(writer, pair[0], pair[1]);
            }
            for(String[] pair : FILES) {
                if ( ! (pair[0].equals("NONONO") || pair[1].equals("NONONO"))){
                    addFILE(writer, pair[0], pair[1]);
                }
            }

            //close writer
            writer.append("\r\n").flush();
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.close();

            int responseCode = connection.getResponseCode();

            //format output meta data for textview
            final StringBuilder output = new StringBuilder();
            if(false){
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

            //add output to webview
            output.append(responseOutput.toString());
            ((HubActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                    listener.POSTResult(returnAddress, output.toString());
                    if(pretty) {
                        WebView webView = new WebView(context);
                        webView.loadData(output.toString(), "text/html; charset=UTF-8", null);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setView(webView);
                        dialog.setPositiveButton("Okay", null);
                        dialog.show();
                    }
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface POSTListener{
        void POSTResult(String returnAddress, String result);
    }

}