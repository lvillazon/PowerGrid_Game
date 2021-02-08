package com.luisvillazon.game.model;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Date;

public class Throttle {
    // handles the lag and rate behaviour of the power station throttle
    private int currentThrottle; // 0 - 100%
    private int minThrottle; // lowest allowed value (max is always 100%)
    private LinkedList<ThrottleChange> throttleChangeSchedule; // sequence of scheduled changes to the throttle
    private long rampUpLag; // delay in ms before commands to increase throttle are actioned
    private long shutDownLag; // same, for commands to decrease
    private long rampUpRate; // how much the throttle can increase per unit time
    private long shutDownRate; // same, for decreasing

    public Throttle(int min, double upLag, double upRate, double downLag, double downRate) {
        // lag and rate are passed as floats to allow them to be expressed in seconds
        // but converted internally into ms to work better with the Instant class
        minThrottle = min;
        currentThrottle = 0;
        rampUpLag = (long)(upLag * 1000);
        rampUpRate = (long)(upRate * 1000);
        shutDownLag = (long)(downLag * 1000);
        shutDownRate = (long)(downRate * 1000);
        throttleChangeSchedule = new LinkedList<ThrottleChange>();
    }

    public void set(int targetThrottle) {
        // sets up a new schedule that will bring the current throttle to the target value
        // as dictated by the lag and rate values
        if (targetThrottle < minThrottle) {
            targetThrottle = minThrottle; // clip to minimum allowed value
        }

        if (targetThrottle > currentThrottle) {
            // until the lag time has passed, the throttle should keep to any previous
            // schedule. So we must look through the current schedule and discard
            // all scheduled changes, but only up until that time.
            Date updateTime = new Date();
            Date scheduledTime;
            updateTime.setTime(updateTime.getTime() + rampUpLag); // = now + lag
            if (throttleChangeSchedule.size() >0) {
                ThrottleChange tc = throttleChangeSchedule.getLast();
                scheduledTime = tc.getTime();

                while ((tc != null) && (scheduledTime.after(updateTime))) {
                    throttleChangeSchedule.removeLast();
                    tc = throttleChangeSchedule.getLast();
                    scheduledTime = tc.getTime();
                }
            }
            // add new throttle change tuples to the schedule,
            // for each percentage point change between the current and the target
            // the throttle starts changing after the initial lag
            for (int i = currentThrottle; i <= targetThrottle; i++) {
                ThrottleChange nextThrottle = new ThrottleChange(i, updateTime);
                throttleChangeSchedule.add(nextThrottle);
                // subsequent changes use the rate, not the lag
                updateTime.setTime(updateTime.getTime() + rampUpRate);
                // System.out.println("adding Time=" + updateTime + " Throttle=" + i);
            }
        } else {
            // do the same thing but counting down - this could probably be modularised better
            Date updateTime = new Date();
            Date scheduledTime;
            updateTime.setTime(updateTime.getTime() + shutDownLag); // = now + lag
            if (throttleChangeSchedule.size() >0) {
                ThrottleChange tc = throttleChangeSchedule.getLast();
                scheduledTime = tc.getTime();

                while ((tc != null) && (scheduledTime.after(updateTime))) {
                    throttleChangeSchedule.removeLast();
                    tc = throttleChangeSchedule.getLast();
                    scheduledTime = tc.getTime();
                }
            }
            // add new throttle change tuples to the schedule,
            // for each percentage point change between the current and the target
            // the throttle starts changing after the initial lag
            for (int i = currentThrottle; i >= targetThrottle; i--) {
                ThrottleChange nextThrottle = new ThrottleChange(i, updateTime);
                throttleChangeSchedule.add(nextThrottle);
                // subsequent changes use the rate, not the lag
                updateTime.setTime(updateTime.getTime() + shutDownRate);
                // System.out.println("adding Time=" + updateTime + " Throttle=" + i);
            }
        }
        //displaySchedule(); // DEBUG
    }

    private void update() {
        // checks for scheduled changes and sets the throttle value accordingly
        if (throttleChangeSchedule.size() > 0) {
            Date now = new Date();
            Date scheduledTime;
            ThrottleChange tc = throttleChangeSchedule.getFirst();

            // apply all scheduled changes whose time is overdue
            while ((tc != null) && (tc.getTime().before(now))) {
                currentThrottle = tc.getThrottle();
                throttleChangeSchedule.removeFirst();
                if (throttleChangeSchedule.size() > 0) {
                    tc = throttleChangeSchedule.getFirst();
                } else {
                    tc = null;
                }
            }
        }
    }

    public int getCurrentThrottle() {
        this.update();
        // System.out.println("Returning " + currentThrottle);
        return currentThrottle;
    }

    public void displaySchedule() {
        // TEST
        // print out times for scheduled throttle changes
        ThrottleChange tc;
        Iterator<ThrottleChange> iter = throttleChangeSchedule.iterator();
        System.out.println("----Throttle Schedule ----");
        while (iter.hasNext()) {
            tc = iter.next();
            System.out.print(tc.getTime());
            System.out.println(" " + tc.getThrottle());
        }
    }
}
