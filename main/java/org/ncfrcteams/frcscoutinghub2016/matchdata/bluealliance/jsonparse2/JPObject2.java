package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse2;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPObject2 {

    public <V> V getAsType(Class<V> v) {
        Object o = this;
        if(v.isInstance(o)) {
            return (V) o;
        } else {
            return null;
        }
    }

    public Class getType() {
        return JPObject2.class;
    }
}
