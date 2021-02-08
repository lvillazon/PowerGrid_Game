package com.luisvillazon.game.model;
import java.util.Date;

public class ThrottleChange {
    // simple tuple class to hold a time and a throttle level
    // this is used to say "after this many seconds, set the new throttle level to such and such"
    private int throttle;
    private Date time;

    public ThrottleChange(int t, Date d) {
        throttle = t;
        time = new Date(d.getTime());
    }

    public int getThrottle() {
        return throttle;
    }

    public Date getTime() {
        return time;
    }
}
