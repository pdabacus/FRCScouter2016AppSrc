package org.ncfrcteams.frcscoutinghub2016.matchdata.schedule;

import android.content.Context;
import android.telephony.TelephonyManager;

import org.ncfrcteams.frcscoutinghub2016.matchdata.Obstacle;
import org.ncfrcteams.frcscoutinghub2016.matchdata.Team;

import java.io.Serializable;

/**
 * Created by Kyle Brown on 3/10/2016.
 */

public class MatchDescriptor implements Serializable, Comparable {
    private int matchNum = 0;
    private int[] teams;
    private Obstacle[] obstacles = new Obstacle[8];
    private boolean isQual = true;
    private String returnAddress;

    public MatchDescriptor(Context context, int matchNum, int[] teams) {
        this.matchNum = matchNum;
        this.teams = teams;

        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        returnAddress = tMgr.getLine1Number();
    }

    public MatchDescriptor(Context context, int matchNum, int[] teams, boolean isQual) {
        this.matchNum = matchNum;
        this.teams = teams;
        this.isQual = isQual;

        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        returnAddress = tMgr.getLine1Number();
    }

    public static MatchDescriptor fromString(Context context, String s) {
        String[] parts = s.split(",");
        int matchNum = Integer.parseInt(parts[0]);
        int[] teams = new int[6];
        for(int i=1; i<parts.length; i++) {
            teams[i-1] = Integer.parseInt(parts[i]);
        }
        return new MatchDescriptor(context,matchNum,teams);
    }

    public String toString(Team t) {
        StringBuilder s = new StringBuilder();

        s.append((t.getValue() > 2)? "B" : "R");
        s.append(",");
        s.append(isQual?"Qual":"Elim");
        s.append(",");
        s.append(matchNum);
        s.append(",");
        s.append(teams[t.getValue()]);

        for(Obstacle o : obstacles) {
            s.append(",");
            s.append(o.getValue());
        }

        s.append(",");
        s.append(returnAddress);

        return s.toString();
    }

    public String[] getStrings() {
        return new String[] {
                toString(Team.R1),
                toString(Team.R2),
                toString(Team.R3),
                toString(Team.B1),
                toString(Team.B2),
                toString(Team.B3)
        };
    }

    @Override
    public int compareTo(Object another) {
        MatchDescriptor other = (MatchDescriptor) another;
        if(this.isQual() == other.isQual()) {
            return Integer.compare(this.getMatchNum(),other.getMatchNum());
        } else {
            if(this.isQual()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public boolean isQual() {
        return isQual;
    }

    public int getMatchNum() {
        return matchNum;
    }

    public int getTeamNum(Team t) {
        return teams[t.getValue()];
    }

    public synchronized void setObstacles(Obstacle[] o) {
        obstacles = o;
    }

    public synchronized Obstacle[] getObstacles() {
        return obstacles.clone();
    }


}
