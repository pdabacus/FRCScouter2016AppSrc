package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse;

import java.util.Map;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPString extends JPObject {
    private String string;

    public JPString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public Class getType() {
        return JPString.class;
    }
}
