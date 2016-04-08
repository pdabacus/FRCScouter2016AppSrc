package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2;

import java.util.Map;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPMap2 extends JPObject2 {
    private Map<String,JPObject2> map;

    public JPMap2(Map<String, JPObject2> map) {
        this.map = map;
    }

    public Map<String,JPObject2> getMap() {
        return map;
    }

    public JPObject2 get(String key) {
        return map.get(key);
    }

    public Class getType() {
        return JPMap2.class;
    }
}
