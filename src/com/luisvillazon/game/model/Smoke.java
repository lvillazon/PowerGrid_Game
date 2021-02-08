package com.luisvillazon.game.model;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.Resources;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.QuadCurve2D;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.luisvillazon.game.main.GameMain.GAME_WIDTH;
import static com.luisvillazon.game.main.GameMain.SMOKE_TRANSPARENCY;

public class Smoke {

    private class Puff {
        // a single puff of smoke
        // represented as a tuple of x, y coords + size
        private int x;
        private int y;
        private int size;
        public Puff(int x, int y, int s) {
            this.x = x;
            this.y = y;
            size = s;
        }
    }

    // represents smoke billowing from a chimney
    private int intensity; // how dense and big the smoke column appears 0 - 100
    private int chimneyRadius; // size of the smoke column when it first emerges
    private int chimneyX, chimneyY; // the coords of the smoke source
    private ArrayList<Puff> smokePuffs; // all the puffs in the smoke plume
    private int horizontalVariation; // how much wiggle there is when determining the initial x pos of a smoke puff
    private int sizeVariation; // variability of the initial smoke puff size
    private Random rnd; // random number generator used for this smoke plume
    private int newestPuff = 0; // used to keep track of the head and tail of the circular queue of smoke puffs
    private int oldestPuff = 0;
    private Color smokeColor;
    private Color[] smokeTransparentColors;

    public Smoke(int x, int y, int radius, Color c, int intensity) {
        this.intensity = intensity;
        chimneyX = x;
        chimneyY = y;
        chimneyRadius = radius;
        smokePuffs = new ArrayList<>();

        rnd = new Random();
        horizontalVariation = radius/2;
        sizeVariation = 10;

        smokeColor = c; // used for opaque smoke, which gives higher frame rates
        // build an array of colors with progressively greater transparency
        // this is more efficient than generating a new color for each frame, in render()
        smokeTransparentColors = new Color[101]; // 101 to allow transparency from 0 to 100
        for (int transparency = 0; transparency<101; transparency++) {
            int smokeRed = c.getRed(); // RGB components of the initial smoke colour, to allow transparency to be added
            int smokeGreen = c.getGreen();
            int smokeBlue = c.getBlue();
            smokeTransparentColors[transparency] =  new Color(smokeRed, smokeGreen, smokeBlue, transparency);
        }
    }

    public Smoke(int x, int y, int radius) {
        this(x, y, radius, Color.WHITE, 0);
    }

    private void addPuff() {
        // if the smoke hasn't stretched across the screen yet, we create a new puff
        // otherwise, we move the last puff back to the chimney mouth
        int newX = chimneyX + rnd.nextInt(horizontalVariation);
        int newSize = rnd.nextInt(sizeVariation)+sizeVariation;
        if ((smokePuffs.size() == 0) || (smokePuffs.get(oldestPuff).x < GAME_WIDTH)) {
            // new smoke puffs always start at the chimney mouth
            smokePuffs.add(new Puff(newX, chimneyY, newSize));
            newestPuff = smokePuffs.size()-1;
        } else {
            // recycle the oldest puff
            smokePuffs.get(oldestPuff).x = newX;
            smokePuffs.get(oldestPuff).y = chimneyY;
            smokePuffs.get(oldestPuff).size = newSize;
            // adjust the head and tail of the circular queue
            newestPuff = oldestPuff;
            oldestPuff = oldestPuff -1;
            if (oldestPuff < 0) {
                oldestPuff = smokePuffs.size()-1;
            }
        }
    }

    public void update() {

        // add new puffs as the older ones clear the chimney
        if (smokePuffs.size() == 0) { // make sure we have at least one, to prevent index out of range errors
            addPuff();
        } else {
            if ((smokePuffs.get(newestPuff).y < chimneyY - chimneyRadius) || (smokePuffs.get(newestPuff).size >50)) {
                // add new puff once the last one has cleared the chimney
                addPuff();
            }
        }

        // move puffs up and sideways, according to the wind
        for (Puff p: smokePuffs) {
            p.x = p.x + getWind(p.y);
            if (p.y > 0) {
                p.y = p.y - 1;
            }
            if (rnd.nextInt(100) < intensity) {
                p.size++;
            }
        }
    }

    public void setIntensity(int i) {
        intensity = i;
    }

    private int getWind(int y) {
        //TODO make this depend on wind speed
        if (y<30) return 4;
        if (y<60) return 3;
        if (y<100) return 2;
        if (y<150) return 1;
        return 0;
    }

    public void render(Graphics g) {
        if (smokePuffs.size() > 0) {
            for (Puff p: smokePuffs) {

                if (SMOKE_TRANSPARENCY==true) {
                    int transparency = 100 - (100 * (chimneyY - p.y) / chimneyY); //fades from 100 to 0 at top of screen
                    if (transparency < 0) {
                        transparency = 0;
                    }
                    g.setColor(smokeTransparentColors[transparency]);
                } else {
                    g.setColor(smokeColor);
                }
                g.fillOval(p.x, p.y, p.size, p.size);
            }
        }
    }
}
