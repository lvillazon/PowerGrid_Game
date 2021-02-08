package com.luisvillazon.game.model;

import com.luisvillazon.game.main.Resources;

import javax.swing.*;
import java.awt.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class DemandGraph extends JPanel {

    private final int MAX_BARS = 20;
    private final int MIN_BAR_HEIGHT = 2;
    private final int TOP_BORDER = 10; // gap at the top of the graph (in pixels)
    private final Color BAR_SATISFIED = Resources.DARK_GREEN;
    private final Color BAR_NOT_SATISFIED = Resources.RED;
    private final Color BAR_TOO_LOW = Color.BLACK; // Color(0xF2, 0xF5, 0xE1);
    private final int UPDATE_INTERVAL = 1000; //ms between new bar graph segments appearing

    private int scaleMax; // data is rescaled on the basis of this maximum value
    private ArrayList<Integer> demandValues; // holds all the y values of the histogram
    private ArrayList<Integer> supplyValues; // holds all the y values of the histogram
//    private ArrayList<Color> statusColors; // the color for the bar to indicate whether demand is satisfied
    private Consumer dataSource; // the electricity consumer that this graph is monitoring
    private long lastUpdateTime;

    public DemandGraph(Consumer c) {
        System.out.println("SEQUENCE: DemandGraph constructor");
        demandValues = new ArrayList<Integer>();
        supplyValues = new ArrayList<Integer>();
        dataSource = c;
        lastUpdateTime = 0;

        this.setLayout(new BorderLayout(0,0));
        setOpaque(false);
    }

    public int getDemandValue() {
        // return most recently added value
        return demandValues.get(demandValues.size()-1);
    }

    public int getSupplyValue() {
        // return most recently added value
        return supplyValues.get(supplyValues.size()-1);
    }

    public Consumer getSource() {
        return dataSource;
    }

    public void setMaxValue(int max) {
        scaleMax = max;
    }

    public void requestUpdate() {
        // only allow new bars to be added to the graph every so often
        // to stop it scrolling too fast

        if (ZonedDateTime.now().toInstant().toEpochMilli() > lastUpdateTime + UPDATE_INTERVAL) {
            lastUpdateTime = ZonedDateTime.now().toInstant().toEpochMilli();
            double scalingFactor = (double)(getHeight()-1)/scaleMax;
            // add new values to the supply and demand graphs
            // double scaledValue = ((double)dataSource.getDemand() / scaleMax) * getHeight();
            double scaledDemand = (double)dataSource.getDemand() * scalingFactor +1;
            double scaledSupply = (double)dataSource.getSupply() * scalingFactor +1;
         //   System.out.println("h=" + getHeight() + " max=" + scaleMax + " supply=" +scaledSupply);
            demandValues.add((int)scaledDemand);
            supplyValues.add((int)scaledSupply);

            // if the graph is full, we remove the oldest points
            if (demandValues.size() > MAX_BARS) {
                demandValues.remove(0);
                supplyValues.remove(0);
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        // the bar graph is just a series of same-width rectangles
        // drawn horizontally across the panel
        // with the most recent one on the right-hand side

        int barWidth = getWidth()/MAX_BARS;
        int barHeight;
        int lineY;
        int lineX = 0 + barWidth;
        int oldX = getWidth();
        int oldY = 0;
        if (supplyValues.size()>0) {
            oldY = getHeight() - supplyValues.get(supplyValues.size() - 1);
        }

        // demand bars
        int barX = getWidth()-barWidth;
        for (int i = demandValues.size() - 1; i >= 0; i--) {
            lineX = oldX-barWidth;
            lineY = getHeight() - supplyValues.get(i);

            if (demandValues.get(i) <= supplyValues.get(i)) {
                // demand is completely met, so colour the whole bar green
                g.setColor(BAR_SATISFIED);
                g.fillRect(barX, getHeight()-demandValues.get(i), barWidth, demandValues.get(i));

                // supply line graph
                g.setColor(Resources.RED);
                g.drawLine(oldX, oldY, lineX, lineY);
            } else {
                // the demand bar is split into 2. The part that is above the supply line
                // is coloured differently so you can tell you are not meeting demand
                g.setColor(BAR_NOT_SATISFIED);
                g.fillRect(barX, getHeight() - demandValues.get(i), barWidth, lineY);
                g.setColor(BAR_SATISFIED);
                g.fillRect(barX, lineY, barWidth, demandValues.get(i));
            }
            oldY = lineY;
            oldX = lineX;
            barX = barX - barWidth;
        }

        /* supply line graph
        g.setColor(Resources.RED);
        int lineY;
        int lineX = 0 + barWidth;
        int oldX = getWidth();
        int oldY = getHeight() - supplyValues.get(supplyValues.size()-1);
        for (int i=supplyValues.size()-1; i >= 0; i--) {
            lineX = oldX-barWidth;
            lineY = getHeight() - supplyValues.get(i);
            g.drawLine(oldX, oldY, lineX, lineY);
            oldY = lineY;
            oldX = lineX;
        }

         */
    }
}
