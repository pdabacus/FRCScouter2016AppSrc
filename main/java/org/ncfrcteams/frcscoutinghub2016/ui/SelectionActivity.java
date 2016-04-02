package org.ncfrcteams.frcscoutinghub2016.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.communication.qr.MultiQRDialog;
import org.ncfrcteams.frcscoutinghub2016.ui.hub.HubActivity;
import org.ncfrcteams.frcscoutinghub2016.ui.scout.ScoutMainActivity;


public class SelectionActivity extends AppCompatActivity {

    private int orientation;
    private ImageView orientation1;
    private ImageView orientation2;
    private String[] messages;
    private boolean backpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        Bundle intentData = getIntent().getExtras();
        if((intentData != null) && intentData.containsKey("Data")) {
            messages = intentData.getStringArray("Data");
            Button invisibleButton = (Button) findViewById(R.id.invisibleButton);
            invisibleButton.setVisibility(View.VISIBLE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orientation = 1;
        orientation1 = (ImageView) findViewById(R.id.orientation1);
        orientation2 = (ImageView) findViewById(R.id.orientation2);
        backpress = false;
    }



    @Override
    protected void onResume() {
        super.onResume();
        backpress = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            //TODO add settings
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(backpress) {
            super.onBackPressed();
        } else{
            backpress = true;
            Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
        }
    }

    public void orientation1Selected(View view){
        orientation1.setAlpha((float) 1.0);
        orientation2.setAlpha((float) 0.6);
        orientation = 1;
    }

    public void orientation2Selected(View view){
        orientation1.setAlpha((float) 0.6);
        orientation2.setAlpha((float) 1.0);
        orientation = 2;
    }

    public void launchScouter(View view) {
        boolean QrDroidInstalled;
        PackageManager pm = getApplicationContext().getPackageManager();
        try {
            pm.getPackageInfo("la.droid.qr", PackageManager.GET_ACTIVITIES);
            QrDroidInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            QrDroidInstalled = false;
        }

        if(QrDroidInstalled) {
            // run qrdroid intent
            try {
                Intent qrDroid = new Intent("la.droid.qr.scan");
                startActivityForResult(qrDroid, 2);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else{
            // run barcode scanner intent
            try {
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showQRs(View view){
        new MultiQRDialog(this, messages).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String matchdata = null;

        //result from barcode scanner app
        try {
            IntentResult brscanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (brscanResult != null) {
                matchdata = brscanResult.getContents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //result from barcode scanner app
        if(requestCode == 2 && null != data && data.getExtras() != null) {
            matchdata = data.getExtras().getString("la.droid.qr.result");
        }

        if((matchdata != null) && matchdata.split(",").length == 13){
            Intent intent = new Intent(this, ScoutMainActivity.class);
            intent.putExtra("Match Setup", matchdata);
            intent.putExtra("Orientation", orientation);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed Scan", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchHub(View view){
        Intent intent = new Intent(this, HubActivity.class);
        startActivity(intent);
        finish();
    }
}
