package org.ncfrcteams.frcscoutinghub2016.matchdata.bluealliance.jsonparse;


import org.ncfrcteams.frcscoutinghub2016.matchdata.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kyle Brown on 4/7/2016.
 */
public class JPObject<T> {
    private T t;

    public JPObject(T t) {
        this.t = t;
    }

    public <V> V getAsType(Class<V> v) {
        Object o = t;
        if(v.isInstance(o)) {
            return (V) o;
        } else {
            return null;
        }
    }

    public Class getType() {
        return t.getClass();
    }
}
