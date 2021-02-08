package com.luisvillazon.game.model;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.GridOutputUI;
import com.luisvillazon.game.main.GridDemandUI;

import java.util.ArrayList;

public class PowerGrid {
    // The power grid tracks the total power produced and consumed
    // and calculates the overall voltage for the grid.
    private int pricePerUnit; // the value of each unit that is supplied to a consumer
    private ArrayList<PowerStation> stations;
    private ArrayList<Consumer> consumers;
    private GridOutputUI outputPanel;
    private GridDemandUI demandPanel;

    public PowerGrid() {
        System.out.println("SEQUENCE: PowerGrid constructor");
        pricePerUnit = 50; // arbitrary value for now. TODO calculate a reasonable value
        outputPanel = new GridOutputUI(this);
        GameMain.staticSideBar.addToSideBar(outputPanel);

        demandPanel = new GridDemandUI(this);
        GameMain.staticSideBar.addToSideBar(demandPanel);

        stations = new ArrayList<PowerStation>();
        consumers = new ArrayList<Consumer>();
    }

    public void connect(Building b) {
        System.out.println("Connecting " + b.getName() + " to the grid");
        // connect this building to the grid and any associated UI elements
        if (b instanceof PowerStation) {
            stations.add((PowerStation)b);
        }
        if (b instanceof Consumer) {
            consumers.add((Consumer)b);
            //back connect the consumer to the grid
            ((Consumer) b).connectToGrid(this);
            demandPanel.connect((Consumer)b);
        }
    }

    public void addAll(ArrayList<PowerStation> stations, ArrayList<Consumer> consumers) {
        this.stations = stations;
        this.consumers = consumers;
        // back connect every consumer to the grid
        for (Consumer c: consumers) {
            c.connectToGrid(this);
        }
        demandPanel.connect(consumers);
    }
/*
    public void addStation(PowerStation p) {
        stations.add(p);
    }

    public void addConsumer(Consumer c) {
        consumers.add(c);
        c.connectToGrid(this);
    }
*/
    public int getMaxOutput() {
        // returns the total output of all stations at full throttle
        int max = 0;
        for (PowerStation p: stations) {
            max = max + p.getMaxOutput();
        }
        return max;
    }

    public int totalOutput() {
        // output of all stations at current throttle
        int output = 0;
        for (PowerStation p: stations) {
            output = output + p.getCurrentOutput();
        }
        return output;
    }

    public int totalUnitsProduced() {
        // add up power production for all stations
        int total = 0;
        for (PowerStation p: stations) {
            total = total + p.getMwhProduced();
        }
        return total;
    }

    public int totalPollution() {
        int total = 0;
        for (PowerStation p: stations) {
            total = total + p.getPollution();
        }
        return total;
    }

    public int totalUnitsSupplied() {
        // add up the demand from each consumer that was actually met
        int total = 0;
        for (Consumer c: consumers) {
            total = total + c.getMwhSupplied();
        }
        return total;
    }

    public int totalOutages() {
        // add up all the demand from each consumer that was not met
        int total = 0;
        for (Consumer c: consumers) {
            total = total + c.getOutages();
        }
        return total;
    }

    public int totalGeneratingCost() {
        // cost of all electricity generated
        int total = 0;
        for (PowerStation p: stations) {
            total = total + p.getMwhProduced() * p.getUnitCost();
        }
        return total;
    }

    public int totalRevenue() {
        // value of all electricity that was sold
        int total = 0;
        for (Consumer c: consumers) {
            total = total + c.getMwhSupplied() * c.getUnitPrice();
        }
        return total;
    }

    public int totalProfit() {
        // value of all the electricity sold - cost of all electricity produced
        return totalRevenue() - totalGeneratingCost();
    }

    public int costPerUnit() {
        // average cost of each unit actually supplied
        // this takes the cost of all the units generated/ the number of units supplied
        int total = 0;
        int units = totalUnitsSupplied();
        if (units > 0) {
            for (PowerStation p : stations) {
                total = total + p.getMwhProduced() * p.getUnitCost();
            }
            total = total / units;
        }
        return total;
    }

    public void update() {
        outputPanel.update();
        demandPanel.update();
    }

    public int getPowerAllocation(Consumer c) {
        // divides up the current grid output according to the demand sliders
        // if all sliders are in the same position, each consumer gets an equal share
        // if one slider is higher, it will receive a larger share
        int totalAllocations = 0;
        for (Consumer all: consumers) {
            totalAllocations = totalAllocations + all.getAllocationRequest();
        }
        double proportionalAllocation = (double)c.getAllocationRequest()/totalAllocations;
        return (int)(totalOutput() * proportionalAllocation);
    }
}
