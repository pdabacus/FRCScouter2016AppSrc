package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2;

import java.util.List;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPList2 extends JPObject2 {
    private List<JPObject2> list;

    public JPList2(List<JPObject2> list) {
        this.list = list;
    }

    public List<JPObject2> getList() {
        return list;
    }

    public Class getType() {
        return JPList2.class;
    }
}
