package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse;


/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPObject {

    public <V> V getAsType(Class<V> v) {
        Object o = this;
        if(v.isInstance(o)) {
            return (V) o;
        } else {
            return null;
        }
    }

//    public <U> U getAs

    public Class getType() {
        return JPObject.class;
    }
}
