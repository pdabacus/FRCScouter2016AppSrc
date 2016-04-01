package org.ncfrcteams.frcscoutinghub2016.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.ncfrcteams.frcscoutinghub2016.R;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.Match;
import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavan on 3/30/16.
 */
public class DatabaseAdapter extends ArrayAdapter<Match> implements Schedule.ScheduleChangeListener{

    private Context context;
    private DatabaseListener listener;

    public DatabaseAdapter(Context context, DatabaseListener listener, ArrayList<Match> matches) {
        super(context, R.layout.h_frag_list_listview, matches);
        this.context = context;
        this.listener = listener;
    }

    public DatabaseAdapter(Context context, DatabaseListener listener) {
        super(context, R.layout.h_frag_list_listview, new ArrayList<Match>());
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.listener.onListChange();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.h_frag_list_listview, parent, false);
        }

        Match match = getItem(position);
        TextView listViewText = (TextView) view.findViewById(R.id.listViewText);
        listViewText.setText(match.getTitle());
        listViewText.setTextColor(match.getColor());

        boolean inSearchIndex = false;
        if(inSearchIndex) {
            ((ImageView) view.findViewById(R.id.listViewImage)).setImageResource(R.drawable.hub_add);
        }

        return view;
    }

    @Override
    public synchronized void notifyScheduleChanges(List<Match> matches) {
        this.clear();
        this.addAll(matches);
    }

    public interface DatabaseListener{
        void onListChange();
    }

}
