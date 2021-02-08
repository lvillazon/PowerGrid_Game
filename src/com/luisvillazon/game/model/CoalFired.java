package com.luisvillazon.game.model;

/*
    Coal Power Station
    slow to ramp up
    even slower to ramp down
    cannot be shut down completely
    emits pollution
 */

import com.luisvillazon.game.main.CoalFiredUI;
import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.PowerStationUI;

import java.awt.*;
import java.util.ArrayList;

public class CoalFired extends PowerStation {
    private int UNIT_COST = 10; // abitrary cost per unit of electricity produced
    private double UNIT_POLLUTION = 0.3; // arbitrary units
    private int x, y;
    private int width, height;
    private Smoke coolingTower1;
    private Smoke coolingTower2;
    private Smoke coolingTower3;
    private Smoke coolingTower4;
    private Smoke chimney1;
    private Smoke chimney2;

    public CoalFired(int x1, int y1, int x2, int y2, int x3, int y3,
                     int smokeX1, int smokeY1, int smokeX2, int smokeY2, int smokeX3, int smokeY3) {
        super("coal station");
        Rectangle topWindow = new Rectangle(x1, y1, x2 - x1, y2 - y1);
        Rectangle bottomWindow = new Rectangle(x1, y2, x3 - x1, y3 - y2);
        ArrayList<Rectangle> windows = new ArrayList<Rectangle>();
        windows.add(topWindow);
        windows.add(bottomWindow);
        setWindows(windows);
        setActive(false);
        chimney1 = new Smoke(smokeX2, smokeY2-10, (x3-x2)/2, Color.DARK_GRAY, 0);
        chimney2 = new Smoke(smokeX3, smokeY3-10, (x3-x2)/2, Color.DARK_GRAY, 0);
        coolingTower1 = new Smoke(smokeX1, smokeY1, 100);

        setUI(new CoalFiredUI(this, "Didcot coal-fired"));
        setMaxOutput(200); // maximum power (in MW) of the station at full throttle
        throttle = new SimpleThrottle(30, 2.0, 0.2, 0.5);
    }

    @Override
    public int getUnitCost() {
        return UNIT_COST;
    }

    @Override
    public int getPollution() {
        return (int) (UNIT_POLLUTION * getMwhProduced());
    }

    @Override
    public void update() {
        super.update();
        // animate smoke
        if (isActive()) {
            chimney1.setIntensity(throttle.getCurrentThrottle());//.getCurrentOutput());
            chimney2.setIntensity(throttle.getCurrentThrottle());
            coolingTower1.setIntensity(100);
            chimney1.update();
            chimney2.update();
            coolingTower1.update();
        } else {
            chimney1.setIntensity(0);
            chimney2.setIntensity(0);
            coolingTower1.setIntensity(0);
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
            coolingTower1.render(g);
            chimney1.render(g);
            chimney2.render(g);
    }
}