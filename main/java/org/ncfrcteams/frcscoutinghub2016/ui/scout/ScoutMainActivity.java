package org.ncfrcteams.frcscoutinghub2016.ui.scout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.communication.sms_server.SendSms;
import org.ncfrcteams.frcscoutinghub2016.matchdata.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.ui.CustomPageAdapter;
import org.ncfrcteams.frcscoutinghub2016.ui.dialogs.ScoutPostMatchDialog;

import java.util.ArrayList;

public class ScoutMainActivity extends AppCompatActivity implements ScoutFragLeft.OnFragListener,
        ScoutFragMid.OnFragListener, ScoutFragRight.OnFragListener, ScoutPostMatchDialog.PostDialogListener{

    public static MatchRecord myMatchRecord;
    public Toolbar toolbar;
    public RelativeLayout myLayout;
    public MenuItem toggleTele;
    public ViewPager viewPager;
    public CustomPageAdapter myPageAdapter;
    public ArrayList<Fragment> fragments = new ArrayList<>();
    public ArrayList<String> fragtitles = new ArrayList<>();
    public int[] colors = {0xffe9ff8f, 0xff30a050};  //yellow and green
    public String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_main);
        Bundle intentData = getIntent().getExtras();
        myMatchRecord = MatchRecord.createMatchRecord(intentData.getString("Match Setup"),
                intentData.getInt("Orientation"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        myLayout = (RelativeLayout) findViewById(R.id.myLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        fragments.add(ScoutFragLeft.newInstance());
        fragments.add(ScoutFragMid.newInstance());
        fragments.add(ScoutFragRight.newInstance());
        fragtitles.add(myMatchRecord.get("Mode") == 1 ? "Offense" : "Defense");
        fragtitles.add("Neutral");
        fragtitles.add(myMatchRecord.get("Mode") == 1 ? "Defense" : "Offense");
        toolbar.setTitle("Robot " + myMatchRecord.get("Team Number") + " (" +
                (myMatchRecord.get("Color") == 1 ? "Red" : "Blue") + ")");
        setSupportActionBar(toolbar);

        myPageAdapter = new CustomPageAdapter(getSupportFragmentManager(), fragments, fragtitles);
        viewPager.setAdapter(myPageAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        myLayout.setBackgroundColor(colors[0]);
        viewPager.setCurrentItem(1);

        comment = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scout, menu);
        toggleTele = menu.findItem(R.id.toggleTele);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggleTele:
                if (item.isChecked()) {
                    item.setChecked(false);
                    myMatchRecord.put("Teleop Active", 0);
                } else {
                    item.setChecked(true);
                    myMatchRecord.put("Teleop Active", 1);
                }
                updateFragments();
                return true;
            case R.id.undo:
                String text = myMatchRecord.undo();
                updateFragments();
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.save:
                new ScoutPostMatchDialog(this, this, myMatchRecord.get("Challenged"),
                        myMatchRecord.get("Climbed"), comment).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPostConfirm(int state, int newchallenge, int newclimb, String newcomment) {
        myMatchRecord.put("Challenged", newchallenge);
        myMatchRecord.put("Climbed", newclimb);
        comment = newcomment;

        if(state == 1){
            String phonenum = myMatchRecord.getPhoneNum();
            String data = myMatchRecord.getData();
            String unique = (myMatchRecord.get("isQual") == 1 ? "Q" : "E") + myMatchRecord.get("Match Number") +
                    "," + myMatchRecord.get("Team Number");

            Toast.makeText(getApplicationContext(), "Sending Data to " + phonenum, Toast.LENGTH_LONG).show();
            SendSms.send(phonenum, data, comment, unique);

            finish();
            Intent intent = new Intent(this, ScoutPrematchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void updateFragments(){
        myMatchRecord.setActiveButton("None");
        if (myMatchRecord.get("Teleop Active") == 0){
            toggleTele.setIcon(R.drawable.toggle_tele_off);
            myLayout.setBackgroundColor(colors[0]);
        } else{
            toggleTele.setIcon(R.drawable.toggle_tele_on);
            myLayout.setBackgroundColor(colors[1]);
        }
        ((ScoutFragLeft) fragments.get(0)).updateFragment();
        ((ScoutFragMid) fragments.get(1)).updateFragment();
        ((ScoutFragRight) fragments.get(2)).updateFragment();
    }

    @Override
    public void onBackPressed() {
        String text = myMatchRecord.undo();
        updateFragments();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFrag1Interaction(Uri uri) {
    }

    @Override
    public void onFrag2Interaction(Uri uri) {
    }

    @Override
    public void onFrag3Interaction(Uri uri) {
    }

}