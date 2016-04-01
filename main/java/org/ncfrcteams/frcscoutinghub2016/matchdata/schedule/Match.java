package org.ncfrcteams.frcscoutinghub2016.matchdata.schedule;

/**
 * Created by pavan on 3/30/16.
 */
public class Match {

    private int matchnum;
    private int matchStatus;
    private boolean isQual;
    private MatchDescriptor matchDescriptor;
    private String[][] matchRecords = new String[6][2];

    public Match(int matchnum, int matchStatus, boolean isQual, MatchDescriptor matchDescriptor){
        this.matchnum = matchnum;
        this.matchStatus = matchStatus;
        this.isQual = isQual;
        this.matchDescriptor = matchDescriptor;
    }

    public static Match getBlank(int matchNum, boolean isQual) {
        return new Match(matchNum, 0, isQual, null);
    }

    public static Match getFromDescriptor(MatchDescriptor matchDescriptor) {
        return new Match(matchDescriptor.getMatchNum(), 0, matchDescriptor.isQual(), matchDescriptor);
    }

    public boolean isPlaceHolder() {
        return matchDescriptor == null;
    }

    public String getTitle(){
        return (isQual ? "Qual " : "Elim ") + matchnum;
    }

    public int getColor(){
        switch(matchStatus){
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

    public Object[] parseMessage(String message) {
        String[] pair = message.substring(7).split(">"); // { "<frc:D,Q22,4828" , "0,0,0,0..." }
        String[] head = pair[0].split(","); // { "<frc:D" , "Q22" , "4828" }

        Object[] result = new Object[5];
        result[0] = head[1].charAt(0) == 'Q'; //boolean isQual
        result[1] = Integer.parseInt(head[1].substring(1)); //int matchNum
        result[2] = Integer.parseInt(head[2]); //int teamNum
        result[3] = (head[0].charAt(5) == 'D'); //boolean isRecord
        result[4] = pair[1]; //String contents

        return result;
    }


    public int[] getTeams() {
        return matchDescriptor.getTeams();
    }

    public Obstacle[] getObstacles() {
        return matchDescriptor.getObstacles();
    }
}
