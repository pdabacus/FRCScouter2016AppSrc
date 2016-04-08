package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Kyle Brown on 4/6/2016.
 */
public class EventListUpdateTask extends AsyncTask<Date, String, List<BlueAllianceWebInterface.Event>> {
    private EventSelectorDialog selectorDialog;
    private ArrayAdapter<String> adapter;

    public EventListUpdateTask(EventSelectorDialog selectorDialog, ArrayAdapter<String> adapter) {
        this.selectorDialog = selectorDialog;
        this.adapter = adapter;
    }

    @Override
    protected List<BlueAllianceWebInterface.Event> doInBackground(Date... dates) {
        List<BlueAllianceWebInterface.Event> list;

        list = BlueAllianceWebInterface.getEvents(2016);
        list = BlueAllianceWebInterface.eventsInRange(list,dates[0],dates[1]);

        return list;
    }

    @Override
    protected void onPostExecute(List<BlueAllianceWebInterface.Event> events) {
        adapter.clear();
        adapter.addAll(BlueAllianceWebInterface.getNamesOf(events));
        Log.d("Event names", BlueAllianceWebInterface.getNamesOf(events).toString());
        selectorDialog.setEvents(events);
    }
}
