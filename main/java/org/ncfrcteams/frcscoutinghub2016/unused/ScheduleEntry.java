package org.ncfrcteams.frcscoutinghub2016.unused;

import org.ncfrcteams.frcscoutinghub2016.matchdata.schedule.MatchDescriptor;

/**
 * Created by pavan on 3/30/16.
 */

public class ScheduleEntry {
    private int matchNum;
    private boolean isQual;
    private MatchDescriptor matchDescriptor;

    private ScheduleEntry(int matchNum, MatchDescriptor matchDescriptor, boolean isQual) {
        this.matchNum = matchNum;
        this.matchDescriptor = matchDescriptor;
        this.isQual = isQual;
    }

    public static ScheduleEntry getBlank(int matchNum, boolean isQual) {
        return new ScheduleEntry(matchNum,null,isQual);
    }

    public static ScheduleEntry getFromDescriptor(MatchDescriptor matchDescriptor) {
        return new ScheduleEntry(matchDescriptor.getMatchNum(),matchDescriptor,matchDescriptor.isQual());
    }

    /**
     * @return whether or not this ScheduleEntry is a placeholder or a real match
     */
    public boolean isPlaceHolder() {
        return matchDescriptor == null;
    }

    /**
     * @return a string representation of this ScheduleEntry
     */
    public String toString() {
        return (isQual? "Qual " : "Elim ") + matchNum;
    }
}
