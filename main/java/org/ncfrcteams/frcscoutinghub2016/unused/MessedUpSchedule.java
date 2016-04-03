package org.ncfrcteams.frcscoutinghub2016.unused;

import org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule.MatchDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kyle Brown on 3/16/2016.
 */
public class MessedUpSchedule {
    private List<MatchDescriptor> matchDescriptorList;
    private List<ScheduleEntry> scheduleEntries;
    private List<ScheduleEntry> validEntries;

    private ScheduleChangeListener scheduleChangeListener = null;

    //****************************************UI Methods*********************************

    /**
     * Creates and initializes a new Schedule object
     */
    public MessedUpSchedule() {
        matchDescriptorList = new ArrayList<>();
        scheduleEntries = new ArrayList<>();
        validEntries = new ArrayList<>();
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
    public synchronized List<ScheduleEntry> getScheduleEntries() {
        return new ArrayList<>(scheduleEntries);
    }

    private void update() {
        Collections.sort(matchDescriptorList);
        scheduleEntries.clear();

        boolean lastMatchWasQual = true;

        int lastNum = 0;
        int currNum;
        ScheduleEntry newEntry;

        int index;

        for(MatchDescriptor matchDescriptor : matchDescriptorList) {
            currNum = matchDescriptor.getMatchNum();

            if(!matchDescriptor.isQual() && lastMatchWasQual) {
                lastNum = 0;
            }

            for(int i=lastNum+1; i<currNum; i++) {
                newEntry = ScheduleEntry.getBlank(i,matchDescriptor.isQual());
                scheduleEntries.add(newEntry);
            }

            newEntry = ScheduleEntry.getFromDescriptor(matchDescriptor);
            index = validEntries.indexOf(newEntry);

            if(index == -1) {
                validEntries.add(newEntry);
                index = validEntries.size()-1;
            }

            scheduleEntries.add(validEntries.get(index));

            validEntries.add(newEntry);

            lastNum = currNum;
            lastMatchWasQual = matchDescriptor.isQual();
        }

        if(scheduleChangeListener != null) {
            scheduleChangeListener.notifyScheduleChanges(getScheduleEntries());
        }
    }

    public interface ScheduleChangeListener {
        void notifyScheduleChanges(List<ScheduleEntry> scheduleEntries);
    }
}
