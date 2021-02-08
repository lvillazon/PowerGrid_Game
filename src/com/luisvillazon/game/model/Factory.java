package com.luisvillazon.game.model;

import com.luisvillazon.game.main.Resources;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class Factory extends Consumer {
    private final int UNIT_PRICE = 40;
    private Smoke chimney;

    public Factory(int x1, int y1, int cornerX, int cornerY, int x2, int y2) {
        super("factory");
        icon = new ImageIcon(Resources.factoryIcon);
        // the factory outline is a rectangle with one corner snipped off
        // so we model the windows as two rectangles
        Rectangle skyLights = new Rectangle(x1, y1, cornerX - x1, cornerY - y1);
        Rectangle mainWindows = new Rectangle(x1, cornerY, x2 -x1, y2 - cornerY);
        ArrayList<Rectangle> windows = new ArrayList<Rectangle>();
        windows.add(skyLights);
        windows.add(mainWindows);
        setWindows(windows);

        chimney = new Smoke(cornerX-60, y1-120, 20, Color.DARK_GRAY, 0);

        // factories operate from 8am to 5pm
        setDemand(8, 70);
        setDemand(9, 100);
        setDemand(10, 100);
        setDemand(11, 100);
        setDemand(12, 80);
        setDemand(13, 100);
        setDemand(14, 100);
        setDemand(15, 100);
        setDemand(16, 100);
        setDemand(17, 70);
    }

    @Override
    public int getMaxDemand() {
        return 100;
    }

    @Override
    public int getUnitPrice() {
        return UNIT_PRICE;
    }

    @Override
    public void update() {
        super.update();
        // factories are active 9 -5
        if ((TimeOfDay.getHour() >=8) && (TimeOfDay.getHour() < 17)) {
            setActive((true));
        } else {
            setActive(false);
        }
        if (isActive()) {
            chimney.setIntensity(getDemand()/2);
        } else {
            chimney.setIntensity(0);
        }
        chimney.update();
    }


    @Override
    public void render(Graphics g) {
        super.render(g);
        chimney.render(g);
    }
}
