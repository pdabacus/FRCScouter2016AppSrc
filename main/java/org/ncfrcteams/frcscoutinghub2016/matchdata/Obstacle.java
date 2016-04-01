package org.ncfrcteams.frcscoutinghub2016.matchdata;

/**
 * Created by Kyle Brown on 3/17/2016.
 */
public enum Obstacle {
    LOW_BAR(0), PORTCULLIS(1), CHIVEL_DE_FRISE(2), RAMPARTS(3), MOAT(4),
        DRAW_BRIDGE(5), SALLY_PORT(6), ROCK_WALL(7), ROUGH_TERRAIN(8);

    private int value;

    Obstacle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}