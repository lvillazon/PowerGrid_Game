package com.luisvillazon.game.model;

public class SimpleThrottle {
    // handles the lag and rate behaviour of the power station throttle
    private int targetThrottle;
    private int currentThrottle; // 0 - 100%
    private int minThrottle; // lowest allowed value (max is always 100%)
    private long lag; // delay in ms before commands to change throttle are actioned
    private long rampUpRate; // how much the throttle can increase per unit time
    private long shutDownRate; // same, for decreasing
    private long responseDelay; // the time when the throttle can begin moving
    private long changeCooldown; // the delay before the throttle can next increment/decrement

    public SimpleThrottle(int min, double lag, double upRate, double downRate) {
        // lag and rate are passed as floats to allow them to be expressed in seconds
        // but converted internally into ms to work better with the Instant class
        minThrottle = min;
        targetThrottle = 0;
        currentThrottle = 0;
        lag = (long)(lag * 1000);
        rampUpRate = (long)(upRate * 1000);
        shutDownRate = (long)(downRate * 1000);
    }

    public void set(int targetThrottle) {
        // sets up a new schedule that will bring the current throttle to the target value
        // as dictated by the lag and rate values
        if (targetThrottle < minThrottle) {
            this.targetThrottle = minThrottle; // clip to minimum allowed value
        } else {
            this.targetThrottle = targetThrottle;
        }

        responseDelay = System.currentTimeMillis() + lag; // = now + lag
    }

    private void update() {
        // move the current throttle towards the target, subject to the throttle lag/rate
        if (targetThrottle != currentThrottle) { // if target = current, we can skip all this unpleasantness
            long now = System.currentTimeMillis();
            if (now > responseDelay) { // first wait until the lag timeout expires
                if (now > changeCooldown) { // then wait for the cooldown between individual increment/decrements
                    if (targetThrottle > currentThrottle) {
                        currentThrottle++;
                        changeCooldown = now + rampUpRate;
                    } else {
                        currentThrottle--;
                        changeCooldown = now + shutDownRate;
                    }
                }
            }
        }
    }

    public int getCurrentThrottle() {
        this.update();
        // System.out.println("Returning " + currentThrottle);
        return currentThrottle;
    }

}
