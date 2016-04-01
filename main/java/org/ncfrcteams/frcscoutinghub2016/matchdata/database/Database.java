package org.ncfrcteams.frcscoutinghub2016.matchdata.database;

import java.util.ArrayList;

/**
 * Created by pavan on 3/19/16.
 */
public class Database extends ArrayList<String> {

    public String getString () {
        if (size() > 0) {
            StringBuilder ret = new StringBuilder();
            for (String item : this) {
                ret.append(item).append(";");
            }
            return ret.substring(0, ret.length() - 1);
        }
        return "null";
    }

}
