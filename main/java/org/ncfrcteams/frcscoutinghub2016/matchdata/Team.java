package org.ncfrcteams.frcscoutinghub2016.matchdata;

/**
 * Created by Kyle Brown on 3/17/2016.
 */
public enum Team {
    R1(0),R2(1),R3(2),B1(3),B2(4),B3(5);

    private int value;

    Team(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
