package com.luisvillazon.game.model;

import com.luisvillazon.game.main.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Meter extends JPanel {

    private final int MIN_ANGLE = 190;
    private final int MAX_ANGLE = 350;
    private final int ANGLE_RANGE = MAX_ANGLE - MIN_ANGLE;

    private int value; // the reading indicated by the needle
    private int centreX, centreY; // coords of the base of the needle
    private int needleLength;
    private int min, max;
    private ArrayList<int[]> needleX; // holds the points needed to draw the needle in every position
    private ArrayList<int[]> needleY; // each element in the array list holds 3 Xor Y values for a specific needle pos

    public Meter() {
        System.out.println("SEQUENCE: Meter constructor");
        value = 0;
        this.min = 0;
        this.max = 100;
        centreX = getWidth()/2;
        centreY = getHeight(); // start at the bottom
        needleLength = getHeight(); // full height of the meter
        needleX = new ArrayList<int[]>();
        needleY = new ArrayList<int[]>();

        this.setLayout(new BorderLayout(0,0));
        setOpaque(false);
    }

    public void setValue(int value, int max) {
        if (value != this.value) {
            this.value = value;
            this.max = max;
            repaint();
        }
    }

    public int getValue() {
        return value;
    }

    private double getNeedleAngle(int value) {
        // convert an integer value between min and max
        // into an angle pointing to the corresponding point on the scale
        double angleDegrees;
        if ((value >= min) && (value <= max)) {
            angleDegrees = ((double)(value-min)/(max-min) * ANGLE_RANGE) + MIN_ANGLE;
         } else {
            angleDegrees = MIN_ANGLE;
        }
        return Math.toRadians(angleDegrees);
    }

    private void buildNeedlePoints() {
        // precalculate all the possible vertex positions of the needle gauge
        // to avoid having to calculate them during the paint() method
        // resolution of the meter is 1 degree
        final int TIP = 0;
        final int BASE1 = 1;
        final int BASE2 = 2;

        System.out.println("SEQUENCE: Building meter needle points array");

        centreX = getWidth()/2;
        centreY = getHeight(); // start at the bottom
        needleLength = getHeight(); // full height of the meter
        int halfWidth = needleLength/8;
        double angle;

        for (int i = 0; i<= MAX_ANGLE - MIN_ANGLE; i++) {
            int[] xPoints = new int[3];
            int[] yPoints = new int[3];
            angle = Math.toRadians(i + MIN_ANGLE);
            xPoints[TIP] = (int)(centreX + Math.cos(angle) * needleLength);
            yPoints[TIP] = (int)(centreY + Math.sin(angle) * needleLength);
            xPoints[BASE1] = (int)(centreX + Math.cos(angle-1.57) * halfWidth);
            yPoints[BASE1] = (int)(centreY + Math.sin(angle-1.57) * halfWidth);
            xPoints[BASE2] = (int)(centreX + Math.cos(angle+1.57) * halfWidth);
            yPoints[BASE2] = (int)(centreY + Math.sin(angle+1.57) * halfWidth);

            needleX.add(xPoints);
            needleY.add(yPoints);
        }
    }

    private int getPointsIndex(int value) {
        // convert an integer value between min and max
        // into an index in the arrays corresponding to the needle coords that point to this value
        if (value < min) { return 0;}
        if (value > max) { return needleX.size();}
        return (int)((double)(value-min)/(max-min) * ANGLE_RANGE);
    }

    @Override
    public void update(Graphics g) {
        //super.update(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        // the first time paintComponent is called, we need to initialise
        // the array of points for each needle position
        // this speeds up later calls, because we don't need to repeat the trigonometry
        if (needleX.size() == 0) {
            buildNeedlePoints();
        }
        // draw background scale
        g.setColor(Resources.OFF_WHITE);
        g.fillOval(centreX-needleLength,0, needleLength*2,needleLength*2); //centreX, centreY, needleLength, needleLength);
        // draw the needle
        int i = getPointsIndex(value);
        g.setColor(Resources.DARK_BROWN);
        g.fillPolygon(needleX.get(i), needleY.get(i), 3);
    }

    public void OLD_paintComponent(Graphics g) {
        //g.setColor(Color.YELLOW);
        //g.fillOval(0,0, getWidth(), getHeight());

        //TODO optimise this by precalculating all the points
        centreX = getWidth()/2;
        centreY = getHeight(); // start at the bottom
        needleLength = getHeight(); // full height of the meter
        int halfWidth = needleLength/8;

        // draw the needle
        g.setColor(Color.BLACK);

        double needleBase1X = centreX + Math.cos(getNeedleAngle(value)-1.57) * halfWidth;
        double needleBase1Y = centreY + Math.sin(getNeedleAngle(value)-1.57) * halfWidth;
        double needleBase2X = centreX + Math.cos(getNeedleAngle(value)+1.57) * halfWidth;
        double needleBase2Y = centreY + Math.sin(getNeedleAngle(value)+1.57) * halfWidth;

        double needleTipX = centreX + Math.cos(getNeedleAngle(value)) * needleLength;
        double needleTipY = centreY + Math.sin(getNeedleAngle(value)) * needleLength;

        int[] xPoints = {(int)needleBase1X, (int)needleBase2X, (int)needleTipX};
        int[] yPoints = {(int)needleBase1Y, (int)needleBase2Y, (int)needleTipY};
//        int[] xPoints = {centreX-halfWidth, centreX+halfWidth, (int)needleTipX};
//        int[] yPoints = {centreY, centreY, (int)needleTipY};
        g.fillPolygon(xPoints, yPoints, 3);
    }
}
