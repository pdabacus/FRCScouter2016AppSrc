package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse;

import java.util.List;
import java.util.Map;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPMap extends JPObject {
    private Map<String,JPObject> map;

    public JPMap(Map<String,JPObject> map) {
        this.map = map;
    }

    public Map<String,JPObject> getMap() {
        return map;
    }

    public JPObject get(String key) {
        return map.get(key);
    }

    public Class getType() {
        return JPMap.class;
    }
}
