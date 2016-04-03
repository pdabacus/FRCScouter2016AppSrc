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
    //private Obstacle[] obstacles = new Obstacle[8];
    private int[] barriers = new int[8];
    private boolean isQual;
    private boolean isBlank;
    private String returnAddress;

    public MatchDescriptor(int matchNum, int[] teams, boolean isQual, boolean isBlank, String phoneNum) {
        this.matchNum = matchNum;
        this.teams = teams;
        this.isQual = isQual;
        this.isBlank = isBlank;
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

        return new MatchDescriptor(matchNum, teams, isQual, false, phonenum);
    }

    /*
    public String toString(Team t) {
        StringBuilder s = new StringBuilder();

        s.append((t.getValue() < 3 ) ? "R" : "B");
        s.append(",");
        s.append(isQual ? "Q" : "E");
        s.append(",");
        s.append(matchNum);
        s.append(",");
        s.append(teams[t.getValue()]);

        for(Obstacle o : obstacles) {
            s.append(",");
            s.append(o.getValue());
        }

        for(int barrier : barriers) {
            s.append(",");
            s.append(barrier);
        }

        s.append(",");
        s.append(returnAddress);

        return s.toString(); // " R, Q, 22, 4828, 1, 2, 3, 4, 5, 6, 7, 8, 919-000-1111 "
    }
    */

    private String toString(int t) {
        StringBuilder s = new StringBuilder();
        s.append((t < 3 ) ? "R" : "B");
        s.append(",");
        s.append(isQual ? "Q" : "E");
        s.append(",");
        s.append(matchNum);
        s.append(",");
        s.append(teams[t]);

        for(int barrier : barriers) {
            s.append(",");
            s.append(barrier);
        }

        s.append(",");
        s.append(returnAddress);

        return s.toString(); // " R, Q, 22, 4828, 1, 2, 3, 4, 5, 6, 7, 8, 919-000-1111 "
    }

    /*
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
    */

    public String[] getStrings() {
        return new String[] {
                toString(teams[0]), toString(teams[1]), toString(teams[2]),
                toString(teams[3]), toString(teams[4]), toString(teams[5])
        };
    }

    @Override
    public int compareTo(Object another) {
        MatchDescriptor other = (MatchDescriptor) another;
        if(this.isQual() == other.isQual()) {
            //return Integer.compare(this.getMatchNum(), other.getMatchNum());
            int a = this.getMatchNum();
            int b = other.getMatchNum();
            if(a < b){
                return 1;
            } else if(a > b){
                return -1;
            }
            return 0;
        } else {
            if(this.isQual()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    //************************************** Getters and Setters ***********************************

    public boolean isQual() {
        return isQual;
    }

    public boolean getIsBlank() {
        return isBlank;
    }

    public void setIsBlank(boolean b){
        isBlank = b;
    }

    public int getMatchNum() {
        return matchNum;
    }

    public String getPhoneNum() {
        return returnAddress;
    }

    /*
    public synchronized Obstacle[] getObstacles() {
        return obstacles.clone();
    }

    public synchronized void setObstacles(Obstacle[] o) {
        obstacles = o;
    }
    */

    public synchronized int[] getBarriers() {
        return barriers;
    }

    public synchronized void setBarriers(int[] bs) {
        barriers = bs;
    }

    public int[] getTeams() {
        return teams;
    }

}
