package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPList extends JPObject {
    private List<JPObject> list;

    public JPList(List<JPObject> list) {
        this.list = list;
    }

    public List<JPObject> getList() {
        return list;
    }

    public Class getType() {
        return JPList.class;
    }
}
