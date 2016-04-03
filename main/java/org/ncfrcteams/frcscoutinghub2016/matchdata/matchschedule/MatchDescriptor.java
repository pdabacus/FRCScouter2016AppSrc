package org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule;

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
    private int[] barriers = new int[8];
    private boolean isQual = true;
    private String returnAddress;

    public MatchDescriptor(Context context, int matchNum, int[] teams, boolean isQual, String phoneNum) {
        this.matchNum = matchNum;
        this.teams = teams;
        this.isQual = isQual;
        this.returnAddress = phoneNum;
    }

    public static MatchDescriptor fromString(Context context, String s) {

        String[] parts = s.split(",");        // { Q22,1000,2000,3000,4000,5000,6000,919-000-1111 }

        if(parts.length != 7 && parts.length != 8){
            return null;
        }

        boolean isQual;
        if(parts[0].trim().charAt(0) == 'E') {
            isQual = false;
        } else {
            isQual = true;
        }

        int stringStartPosition = ((! isQual) || (parts[0].trim().charAt(0) == 'Q') ? 1 : 0);
        int matchNum = Integer.parseInt(parts[0].trim().substring(stringStartPosition));

        int[] teams = new int[6];
        for(int i = 0; i < 6; i++) {
            teams[i] = Integer.parseInt(parts[i + 1]);
        }

        String phonenum;
        if(parts.length == 8) {
            phonenum = parts[7].trim();
        } else{
            phonenum = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        }

        return new MatchDescriptor(context, matchNum, teams, isQual, phonenum);


    }

    public String toString(Team t) {
        StringBuilder s = new StringBuilder();

        s.append((t.getValue() > 2) ? "B" : "R");
        s.append(",");
        s.append(isQual ? "Q" : "E");
        s.append(",");
        s.append(matchNum);
        s.append(",");
        s.append(teams[t.getValue()]);

        /*
        for(Obstacle o : obstacles) {
            s.append(",");
            s.append(o.getValue());
        }
        */

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
            int a = this.getMatchNum();
            int b = other.getMatchNum();
            return (a < b ? 1 : -1);
            //return Integer.compare(this.getMatchNum(), other.getMatchNum());
        } else {
            if(this.isQual()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public boolean isQual() {
        return isQual;
    }

    public int getMatchNum() {
        return matchNum;
    }

    public String getPhoneNum() {
        return returnAddress;
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

    public int[] getTeams() {
        return teams;
    }

}
