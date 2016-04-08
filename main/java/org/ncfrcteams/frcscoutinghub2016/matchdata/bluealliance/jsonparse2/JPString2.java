package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPString2 extends JPObject2 {
    private String string;

    public JPString2(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public Class getType() {
        return JPString2.class;
    }
}
