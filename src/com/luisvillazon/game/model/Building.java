package com.luisvillazon.game.model;

import com.luisvillazon.game.main.PowerStationUI;
import com.luisvillazon.game.main.Resources;

import java.awt.*;
import java.util.ArrayList;

public abstract class Building {
    // base class for all buildings that can be "switched on or off"

    protected final Color LIGHT_COLOR = Resources.WARM_YELLOW;
    private boolean isOn; // is this building currently powered up?
    protected ArrayList<Rectangle> windows; // list of all windows that light up on this building
    private String name; // identifier used for UI labels and console diagnostics, eg ("Houses", "Factory")

    public Building(String name) {
        this.name = name;
        setActive(true);
    }

    protected void setWindows(ArrayList<Rectangle> windows) {
        this.windows = windows;
    }

    public String getName() {
        return name;
    }

    public void setActive(boolean state) {
        isOn = state;
    }

    public boolean isActive() {
        return isOn;
    }

    public void render(Graphics g) {
        if (windows != null) {
            if (isOn) {
                g.setColor(LIGHT_COLOR);
            } else {
                g.setColor(Color.BLACK);
            }
            for (Rectangle w : windows) {
                g.fillRect(w.x, w.y, w.width, w.height);
            }
        }
    }
}
