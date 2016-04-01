package org.ncfrcteams.frcscoutinghub2016.matchdata.schedule;

/**
 * Created by pavan on 3/30/16.
 */
public class Match {

    private int matchnum;
    private int matchStatus;
    private boolean isQual;
    private MatchDescriptor matchDescriptor;

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

    public String getText(){
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

}
