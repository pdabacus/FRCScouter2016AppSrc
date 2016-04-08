package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import java.text.DateFormat;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Kyle Brown on 4/6/2016.
 */
public class EventSelectorDialog implements android.view.View.OnClickListener, ListView.OnItemClickListener {
    private Context context;

    private Dialog dialog;
    private EventScheduleListener eventScheduleListener;

    private ListView eventListView;

    private EditText startDateView;
    private EditText endDateView;
    private Date startDate;
    private Date endDate;

    private ArrayAdapter<String> adapter;
    private List<BlueAllianceWebInterface.Event> events;

    public EventSelectorDialog(Context context, EventScheduleListener eventScheduleListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ba_event_selector, null);

        startDateView = (EditText) view.findViewById(R.id.startDate);
        endDateView = (EditText) view.findViewById(R.id.endDate);
        eventListView = (ListView) view.findViewById(R.id.eventListView);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -4);
        startDate = cal.getTime();
        cal.add(Calendar.DATE, 8);
        endDate = cal.getTime();

        startDateView.setText(dateToString(startDate));
        endDateView.setText(dateToString(endDate));
        startDateView.setOnClickListener(this);
        endDateView.setOnClickListener(this);

        adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1, new ArrayList<String>());
        eventListView.setAdapter(adapter);
        eventListView.setOnItemClickListener(this);

        EventListUpdateTask listViewTask = new EventListUpdateTask(this,adapter);
        listViewTask.execute(startDate, endDate);

        this.context = context;
        this.eventScheduleListener = eventScheduleListener;

        //*******************************************
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
        alert.setView(view);
        dialog = alert.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == startDateView.getId() || v.getId() == endDateView.getId()) {
            startDate = stringToDate(startDateView.getText().toString());
            endDate = stringToDate(endDateView.getText().toString());

            EventListUpdateTask listViewTask = new EventListUpdateTask(this,adapter);
            listViewTask.execute(startDate,endDate);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        GetScheduleTask scheduleTask = new GetScheduleTask();
        scheduleTask.execute(events.get(position).key);
        dialog.hide();
    }

    public void setEvents(List<BlueAllianceWebInterface.Event> events) {
        this.events = events;
    }


    class GetScheduleTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... key) {
            return BlueAllianceWebInterface.getScheduleCSVForEvent(key[0]);
        }

        @Override
        protected void onPostExecute(String schedule) {
            if(eventScheduleListener != null) {
                eventScheduleListener.scheduleListener(schedule);
            }
            Toast.makeText(context, schedule, Toast.LENGTH_SHORT).show();
            Log.d("Schedule",schedule);
        }
    }

    public static String dateToString(Date date) {
        return BlueAllianceWebInterface.EVENT_DATE_FORMAT.format(date);
    }

    public static Date stringToDate(String string) {
        try {
            return BlueAllianceWebInterface.EVENT_DATE_FORMAT.parse(string);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static interface EventScheduleListener {
        void scheduleListener(String schedule);
    }
}
