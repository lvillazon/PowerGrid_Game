package com.luisvillazon.game.model;

import com.luisvillazon.game.main.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Office extends Consumer{
    private int UNIT_PRICE = 75;

    public Office(int x1, int y1, int x2, int y2) {
        super("office");
        Rectangle singleWindow = new Rectangle(x1, y1, x2 - x1, y2 - y1);
        ArrayList<Rectangle> windows = new ArrayList<Rectangle>();
        windows.add(singleWindow);
        setWindows(windows);

        icon = new ImageIcon(Resources.officeIcon);
        setDemand(7, 6);
        setDemand(8, 60);
        setDemand(9, 75);
        setDemand(10, 75);
        setDemand(11, 75);
        setDemand(12, 65);
        setDemand(13, 55);
        setDemand(14, 70);
        setDemand(15, 75);
        setDemand(16, 70);
        setDemand(17, 50);
        setDemand(18, 35);
        setDemand(19, 20);
        setDemand(20, 5);
        setDemand(21, 5);
        setDemand(22, 5);
    }

    @Override
    public void update() {
        super.update();
        // offices are active 7am - 11pm
        if ((TimeOfDay.getHour() >7) && (TimeOfDay.getHour() <= 23)) {
            setActive((true));
        } else {
            setActive(false);
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }

    @Override
    public int getMaxDemand() {
        return 75;
    }

    @Override
    public int getUnitPrice() {
        return UNIT_PRICE;
    }
}

