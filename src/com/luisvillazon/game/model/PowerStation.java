package com.luisvillazon.game.model;

import com.luisvillazon.game.main.PowerStationUI;
import java.awt.*;

public abstract class PowerStation extends Building {
    // base class for all power stations
    private int maxOutput; // maximum power (in MW) of the station at full throttle
    protected SimpleThrottle throttle; // control that changes the output % for the station
    private double mwhProduced; // running total of the electricity generated so far
    private long lastUpdateTime; // when the total produced electricity was last updated
    private PowerStationUI controlPanel; // link to the UI elements that control throttle etc

    public PowerStation(String name) {
        super(name);
        lastUpdateTime = TimeOfDay.getSeconds();
    }

    public PowerStationUI getUI() { return controlPanel;}

    public void setUI(PowerStationUI uiPanel) {
        controlPanel = uiPanel;
    }

    public abstract int getUnitCost();

    public abstract int getPollution(); // amount of pollution emitted by the station so far

    public int getMaxOutput() {
        return maxOutput;
    }

    public void setMaxOutput(int maxOutput) {
        this.maxOutput = maxOutput;
    }

    public double getExactOutput() {
        return (double)(throttle.getCurrentThrottle()) * maxOutput /100;
    }

    public int getCurrentOutput() {
        return (int)getExactOutput();
    }

    public void setThrottle(int targetThrottle) {
        // requests a new throttle setting
        throttle.set(targetThrottle);
    }

    public int getMwhProduced() {
        return (int)mwhProduced;
    }

    protected void updateProduction() {
        // update the amount of electricity produced every minute of game time
        // using abs() to account for possible rollover at midnight
        if (Math.abs(TimeOfDay.getSeconds() - lastUpdateTime) > 60) {
            lastUpdateTime = TimeOfDay.getSeconds();
            mwhProduced = mwhProduced + (double)getCurrentOutput()/60;
        }
    }

    public void update() {
        updateProduction();
        getUI().update();
    }

}
