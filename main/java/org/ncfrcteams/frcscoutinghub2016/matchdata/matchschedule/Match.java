package org.ncfrcteams.frcscoutinghub2016.matchdata.matchschedule;

import org.ncfrcteams.frcscoutinghub2016.matchdata.Obstacle;

/**
 * Created by pavan on 3/30/16.
 */
public class Match {

    public MatchDescriptor matchDescriptor;
    public String[] data = new String[6];
    public String[] comments = new String[6];

    public Match(MatchDescriptor matchDescriptor){
        this.matchDescriptor = matchDescriptor;
    }

    public static Match getBlank(int matchNum, boolean isQual) {
        return new Match(new MatchDescriptor(matchNum, new int[6], isQual, true, ""));
    }

    public static Match getFromDescriptor(MatchDescriptor matchDescriptor) {
        return new Match(matchDescriptor);
    }

    public String getTitle(){
        return (isQual() ? "Qual " : "Elim ") + matchNum();
    }

    public int getColor(){
        switch(getMatchStatus()){
            case 0:
                return 0xffff0000; //red = incomplete
            case 1:
                return 0xff00ff00; //green = ready for scouting
            case 2:
                return 0xffffff00; //yellow = scouting in progress
            default:
                return 0xffffffff; //black = done (all data back)
        }
    }

    public synchronized void addComment(int teamNum, String comment) {
        int[] teamNums = matchDescriptor.getTeams();

        for(int i=0; i<teamNums.length; i++) {
            if(teamNums[i] == teamNum) {
                this.comments[i] = comment;
            }
        }
    }

    public synchronized void addData(int teamNum, String data) {
        int[] teamNums = matchDescriptor.getTeams();

        for(int i=0; i<teamNums.length; i++) {
            if(teamNums[i] == teamNum) {
                this.data[i] = data;
            }
        }
    }
    public synchronized String[] getComments(){
        return comments;
    }
    public synchronized String[] getData(){
        return data;
    }

    public int getMatchStatus(){
        return 0; //TODO - based off what's in data
    }

    //*********************************** MatchDescriptor References *******************************

    public synchronized int[] teams() {
        return matchDescriptor.getTeams();
    }

    /*
    public Obstacle[] obstacles() {
        return matchDescriptor.getObstacles();
    }
    */

    public int[] barriers() {
        return matchDescriptor.getBarriers();
    }

    public void setBarriers(int[] barriers) {
        matchDescriptor.setBarriers(barriers);
    }

    public int matchNum() {
        return matchDescriptor.getMatchNum();
    }

    public boolean isQual() {
        return matchDescriptor.isQual();
    }

    public boolean isBlank() {
        return matchDescriptor.getIsBlank();
    }

    public String phoneNum() {
        return matchDescriptor.getPhoneNum();
    }

}
