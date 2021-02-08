package com.luisvillazon.game.model;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class House extends Consumer {
    private int UNIT_PRICE = 75;

    public House(int number_of_houses, int x1, int y1, int x2, int y2) {
        super("house");
        Rectangle singleWindow = new Rectangle(x1, y1, x2 - x1, y2 - y1);
        ArrayList<Rectangle> windows = new ArrayList<Rectangle>();
        windows.add(singleWindow);
        setWindows(windows);

        icon = new ImageIcon(Resources.houseIcon);
        // demand numbers are multiplied by the number of houses so larger communities use more power
        setDemand(6, 2 * number_of_houses);
        setDemand(7, 20 * number_of_houses);
        setDemand(8, 15 * number_of_houses);
        setDemand(18, 12 * number_of_houses);
        setDemand(19, 24 * number_of_houses);
        setDemand(20, 16 * number_of_houses);
        setDemand(21, 13 * number_of_houses);
        setDemand(22, 9 * number_of_houses);
        setDemand(23, 6 * number_of_houses);
        setSaturdayDemand(10, 12 * number_of_houses);
        setSaturdayDemand(11, 12 * number_of_houses);
        setSaturdayDemand(12, 15 * number_of_houses);
        setSaturdayDemand(13, 15 * number_of_houses);
        setSaturdayDemand(14, 12 * number_of_houses);
        setSaturdayDemand(15, 9 * number_of_houses);
        setSaturdayDemand(16, 8 * number_of_houses);
        setSaturdayDemand(17, 12 * number_of_houses);
        setSaturdayDemand(18, 18 * number_of_houses);
        setSaturdayDemand(19, 24 * number_of_houses);
        setSaturdayDemand(20, 30 * number_of_houses);
        setSaturdayDemand(21, 35 * number_of_houses);
        setSaturdayDemand(22, 28 * number_of_houses);
        setSaturdayDemand(23, 26 * number_of_houses);
        setSundayDemand(0,24 * number_of_houses);
        setSundayDemand(1,18 * number_of_houses);
        setSundayDemand(2,18 * number_of_houses);
        setSundayDemand(3,12 * number_of_houses);
        setSundayDemand(4,6 * number_of_houses);
        setSundayDemand(5,0 * number_of_houses);
        setSundayDemand(6,0 * number_of_houses);
        setSundayDemand(7,0 * number_of_houses);
        setSundayDemand(8,0 * number_of_houses);
        setSundayDemand(9,6 * number_of_houses);
        setSundayDemand(10,12 * number_of_houses);
        setSundayDemand(11,12 * number_of_houses);
        setSundayDemand(12,18 * number_of_houses);
        setSundayDemand(13,20 * number_of_houses);
        setSundayDemand(14,18 * number_of_houses);
        setSundayDemand(15,10 * number_of_houses);
        setSundayDemand(16,10 * number_of_houses);
        setSundayDemand(17,12 * number_of_houses);
        setSundayDemand(18,12 * number_of_houses);
        setSundayDemand(19,24 * number_of_houses);
        setSundayDemand(20,16 * number_of_houses);
        setSundayDemand(21,13 * number_of_houses);
        setSundayDemand(22,9 * number_of_houses);
        setSundayDemand(23,6 * number_of_houses);
    }

    @Override
    public void update() {
        super.update();
        // houses are active 6-8:30 am
        // then again from 5:30 until 11pm
        if (((TimeOfDay.getHour() >6) && (TimeOfDay.getHour() <= 8)) ||
            ((TimeOfDay.getHour() > 18) && (TimeOfDay.getHour() < 23))) {
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
        return 24;
    }

    @Override
    public int getUnitPrice() {
        return UNIT_PRICE;
    }
}
