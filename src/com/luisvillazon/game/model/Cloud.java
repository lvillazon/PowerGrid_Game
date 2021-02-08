package com.luisvillazon.game.model;

import com.luisvillazon.game.main.GameMain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.luisvillazon.game.main.GameMain.GAME_WIDTH;

public class Cloud {

    private ArrayList<Point> cloudBlobs; // coords of each round blob of cloud stuff
    private ArrayList<Integer> blobSize; // radius of each blob
    private int verticalVariation; // how much wiggle there is when determining the initial y pos of a cloud
    private final int[] sizeVariation = {10, 17, 20, 23, 26, 26, 26, 30, 26, 26, 26, 23, 20, 17, 10}; // variability of the initial smoke puff size
    private Random rnd; // random number generator used for this smoke plume
    private Color cloudColor;
    private int y;
    private double x; // x coord is stored internally as a double to allow smoother movement at different wind speeds

    public Cloud(int x, int y) {
        cloudBlobs = new ArrayList<Point>();
        blobSize = new ArrayList<Integer>();
        cloudColor = Color.WHITE;

        // each cloud is made up of 5 blobs - TODO random cloud shapes?
        this.x = x;
        this.y =y;
        Point p;
        int size;

        cloudBlobs.add(new Point(0,0));
        blobSize.add(3);
        cloudBlobs.add(new Point(25,2));
        blobSize.add(5);
        cloudBlobs.add(new Point(36,8));
        blobSize.add(10);
        cloudBlobs.add(new Point(41,16));
        blobSize.add(20);
        cloudBlobs.add(new Point(51,21));
        blobSize.add(25);
        cloudBlobs.add(new Point(63,27));
        blobSize.add(30);
        cloudBlobs.add(new Point(82,7));
        blobSize.add(10);
        cloudBlobs.add(new Point(88,14));
        blobSize.add(10);
        cloudBlobs.add(new Point(90,6));
        blobSize.add(10);
        cloudBlobs.add(new Point(96,8));
        blobSize.add(10);
        cloudBlobs.add(new Point(96,10));
        blobSize.add(5);


        // random generation - doesn't always look good
        /*
        for (int i=0; i<15; i++) {
            size = ThreadLocalRandom.current().nextInt(sizeVariation[i], 2 * sizeVariation[i]);
            p = new Point(i * 5 - size/2, -size-ThreadLocalRandom.current().nextInt(0, 5));
            cloudBlobs.add(p);
            blobSize.add(size);
        }

         */
    }

    public int getX() {
        // convert to int so the x coord can be used to draw the cloud
        return (int) x;
    }

    public int getY() { return y; }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move(double windSpeed) {
        x = x + windSpeed;
    }

    public void render(Graphics g, Color cloudColor) {
        if (cloudBlobs.size() > 0) {
            for (int i = 0; i < cloudBlobs.size(); i++) {
                g.setColor(cloudColor);
                g.fillOval(cloudBlobs.get(i).x+getX(), y-cloudBlobs.get(i).y, blobSize.get(i), blobSize.get(i));
            }
        }
    }
}
