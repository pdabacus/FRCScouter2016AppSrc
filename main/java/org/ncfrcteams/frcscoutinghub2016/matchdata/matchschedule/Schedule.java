package org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kyle Brown on 3/16/2016.
 */
public class Schedule {
    private List<MatchDescriptor> matchDescriptorList;
    private List<Match> matches;
    private ScheduleChangeListener scheduleChangeListener = null;

    //****************************************UI Methods*********************************

    /**
     * Creates and initializes a new Schedule object
     */
    public Schedule() {
        matchDescriptorList = new ArrayList<>();
        matches = new ArrayList<>();
    }

    /**
     * Sets the ScheduleChangeListener for this Schedule
     * @param changeListener
     */
    public synchronized void setScheduleChangeListener(ScheduleChangeListener changeListener) {
        this.scheduleChangeListener = changeListener;
    }

    /**
     * Adds a single MatchDescriptor to the matchDescriptorList sorts it then updates matches
     * and informs the scheduleChangeListener of the changes
     * @param matchDescriptor the MatchDescriptor to be added
     */
    public synchronized void add(MatchDescriptor matchDescriptor) {
        this.matchDescriptorList.add(matchDescriptor);
        update();
    }

    /**
     * Adds a list of MatchDescriptors to the matchDescriptorList sorts it then updates the
     * scheduleEntriesList and informs the scheduleChangeListener of the changes
     * @param matchDescriptorList the list of MatchDescriptors to be added
     */
    public synchronized void addAll(List<MatchDescriptor> matchDescriptorList) {
        this.matchDescriptorList.addAll(matchDescriptorList);
        update();
    }

    /**
     * @return an up to date copy of the scheduleEntries list
     */
    public synchronized ArrayList<Match> getMatches() {
        return new ArrayList<>(matches);
    }

    public String addSMS(String message) {
        if(!message.substring(0, 5).equals("<frc:")) {
            return "";
        }

        String[] pair = message.substring(5).split(">"); // { "<frc:D,Q22,4828" , "0,0,0,0..." }
        String[] head = pair[0].split(","); // { "D" , "Q22" , "4828" }

        Match match = getMatch(Integer.parseInt(head[1].substring(1)),head[1].charAt(0) == 'Q');
        if(match == null) {
            return "";
        }

        if(head[0].equals("D")) {
            match.addData(Integer.parseInt(head[2]), pair[1]);
        } else {
            match.addComment(Integer.parseInt(head[2]), pair[1]);
        }

        return match.getTitle();
    }

    public Match getMatch(int matchNum, boolean isQual) {
        for(Match match : matches) {
            if(match.matchNum() == matchNum && match.isQual() == isQual)
                return match;
        }
        return null;
    }

    public synchronized ArrayList<String> getMatchTitles() {
        ArrayList<String> titles = new ArrayList<>();
        for(Match match : matches) {
            titles.add(match.getTitle());
        }
        return titles;
    }

    private void update() {
        Collections.sort(matchDescriptorList);
        matches.clear();

        int lastNum = matchDescriptorList.get(0).getMatchNum();
        boolean lastIsQual = ! matchDescriptorList.get(0).isQual();
        int currNum;
        boolean currIsQual;
        List<MatchDescriptor> replaceList = new ArrayList<>();

        //lastNum = (lastNum > 1 ? 0 : lastNum); //comment out to disable auto adding Match 1+

        for(MatchDescriptor matchDescriptor : matchDescriptorList) {

            currNum = matchDescriptor.getMatchNum();
            currIsQual = matchDescriptor.isQual();

            if(lastIsQual == currIsQual) {
                //add blanks
                for (int i = lastNum + 1; i < currNum; i++) {
                    MatchDescriptor md = new MatchDescriptor(i, new int[6], currIsQual, true, "");
                    //matchDescriptorList.add(md);////////////////////////////////////////////////////
                    replaceList.add(md);
                    matches.add(new Match(md));
                }
            } else {
                //add Match 1+
                Log.d("asdf", "switch " + String.valueOf(currNum) + " " + String.valueOf(currIsQual));
                if(currNum > 1){
                    for (int i = 1; i < currNum; i++) {
                        MatchDescriptor md = new MatchDescriptor(i, new int[6], currIsQual, true, "");
                        //matchDescriptorList.add(md);////////////////////////////////////////////////
                        replaceList.add(md);
                        matches.add(new Match(md));
                    }
                }
            }

            //add the current match
            Log.d("asdf", String.valueOf(currNum) + " " + String.valueOf(currIsQual));
            //replaceList.add(matchDescriptor);///////////////////////////////////////////////////////
            matches.add(Match.getFromDescriptor(matchDescriptor));

            lastNum = currNum;
            lastIsQual = currIsQual;
        }

        matchDescriptorList = replaceList;

        if(scheduleChangeListener != null) {
            scheduleChangeListener.notifyScheduleChanges(getMatches());
        }
    }

    public String getDatabase() {
        ArrayList<String> records = new ArrayList<>();
        for(Match match : matches) {
            String[] data = match.getData();
            String[] comments = match.getComments();

            for(int i = 0; i < 6; i++){
                if(data[i] != null && comments[i] != null){
                    records.add(data[i] + "," + comments[i]);
                }
            }
        }

        if (records.size() > 0) {
            StringBuilder ret = new StringBuilder();
            for (String record : records) {
                ret.append(record).append(";\n");
            }
            return ret.substring(0, ret.length() - 2);
        }
        return "null";
    }

    public interface ScheduleChangeListener {
        void notifyScheduleChanges(ArrayList<Match> matches);
    }

}