package org.ncfrcteams.frcscoutinghub2016.matchdata.schedule;

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
        if(!message.substring(0,3).equals("<frc")) {
            return "";
        }

        String[] pair = message.substring(7).split(">"); // { "<frc:D,Q22,4828" , "0,0,0,0..." }
        String[] head = pair[0].split(","); // { "<frc:D" , "Q22" , "4828" }

        Match match = getMatch(Integer.parseInt(head[1].substring(1)),head[1].charAt(0) == 'Q');

        if(head[0].charAt(5) == 'D') {
            match.addData(Integer.parseInt(head[2]),pair[1]);
        } else {
            match.addComment(Integer.parseInt(head[2]),pair[1]);
        }
        return match.getTitle() + " has been updated";
    }

    private Match getMatch(int matchNum, boolean isQual) {
        for(Match match : matches) {
            if(match.getMatchNum() == matchNum && match.isQual() == isQual)
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

        int lastNum = 1;
        int currNum;
        Match newMatch;
        boolean lastMatchWasQual = matchDescriptorList.get(0).isQual();

        for(MatchDescriptor matchDescriptor : matchDescriptorList) {
            currNum = matchDescriptor.getMatchNum();

            if(!matchDescriptor.isQual() && lastMatchWasQual) {
                lastNum = 1;
            }

            for(int i=lastNum; i<currNum; i++) {
                newMatch = Match.getBlank(i,matchDescriptor.isQual());
                matches.add(newMatch);
            }

            newMatch = Match.getFromDescriptor(matchDescriptor);
            matches.add(newMatch);

            lastNum = currNum;
            lastMatchWasQual = matchDescriptor.isQual();
        }

        if(scheduleChangeListener != null) {
            scheduleChangeListener.notifyScheduleChanges(getMatches());
        }
    }

    public interface ScheduleChangeListener {
        void notifyScheduleChanges(ArrayList<Match> matches);
    }
}
