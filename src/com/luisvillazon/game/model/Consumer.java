package com.luisvillazon.game.model;

import javax.swing.*;

public abstract class Consumer extends Building {
    // parent class for all the buildings that use electricity instead of producing it

    // current demand for each hour of the 24-hour day
    protected ImageIcon icon;
    private final int DEMAND_INCREMENTS = 24; // one for each hour - could change later for more precision
    private int[] weekdayDemand;
    private int[] saturdayDemand;
    private int[] sundayDemand;
    private JSlider mixer;
    private PowerGrid grid;
    private double mwhSupplied; // running total of the electricity generated so far
    private long lastUpdateTime; // when the total produced electricity was last updated
    private double outages; // how much of demand was not met

    public abstract int getMaxDemand();
    public abstract int getUnitPrice(); // how much the consumer pays for their electricity

    public Consumer(String name) {
        super(name);
        weekdayDemand = new int[DEMAND_INCREMENTS];
        saturdayDemand = new int[DEMAND_INCREMENTS];
        sundayDemand = new int[DEMAND_INCREMENTS];
        for (int i=0; i<DEMAND_INCREMENTS; i++){
            weekdayDemand[i] = 0;
            saturdayDemand[i] = 0;
            sundayDemand[i] = 0;
        }
    }

    protected void updateSupply() {
        // track supply and outages

        // update the amount of (useful) electricity received every minute of game time
        // oversupply is not counted for this
        // using abs() to account for possible rollover at midnight
        if (Math.abs(TimeOfDay.getSeconds() - lastUpdateTime) > 60) {
            lastUpdateTime = TimeOfDay.getSeconds();
            if (getDemand() < getSupply()) {
                // consumer is undersupplied, so the supply metric is capped at the demand
                mwhSupplied = mwhSupplied + (double) getDemand() / 60;
            } else {
                // consumer is undersupplied, so we count whatever is supplied
                mwhSupplied = mwhSupplied + (double) getSupply() / 60;
                // the difference between demand and supply is the outage
                outages = outages + (double) (getDemand() - getSupply()) /60;
            }
        }
    }

    public int getMwhSupplied() {
        return (int)mwhSupplied;
    }

    public int getOutages() {
        return (int)outages;
    }

    public void update() {
        updateSupply();
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getDemand() {
        int hour = TimeOfDay.getHour();
        int day = TimeOfDay.getDay();
        if (day == 5) {
            return saturdayDemand[hour];
        }
        if (day == 6) {
            return sundayDemand[hour];
        }
        return weekdayDemand[hour];
    }

    public void setDemand(int hour, int d) {
        // replace the default demand value for that hour with another
        if ((hour >= 0) && (hour < DEMAND_INCREMENTS)) {
            weekdayDemand[hour] = d;
        }
    }

    public void setSaturdayDemand(int hour, int d) {
        // replace the default demand value for that hour with another
        if ((hour >= 0) && (hour < DEMAND_INCREMENTS)) {
            saturdayDemand[hour] = d;
        }
    }

    public void setSundayDemand(int hour, int d) {
        // replace the default demand value for that hour with another
        if ((hour >= 0) && (hour < DEMAND_INCREMENTS)) {
            sundayDemand[hour] = d;
        }
    }

    public void addMixer(JSlider mixer) {
        this.mixer = mixer;
    }

    public JSlider getMixer() {
        if (mixer != null) {
            return mixer;
        }
        System.out.print("ERROR! Consumer has no mixer set.");
        return null;
    }

    public void connectToGrid(PowerGrid grid) {
        this.grid = grid;
    }

    public int getAllocationRequest() {
        // returns the value of the allocation slider attached to this consumer
       return getMixer().getValue();
    }

    public int getSupply() {
        // electricity available, as supplied by the grid
        if (grid == null) {
            System.out.println("ERROR! Consumer has no connected grid.");
            return 0;
        }
        return grid.getPowerAllocation(this);
    }

}
