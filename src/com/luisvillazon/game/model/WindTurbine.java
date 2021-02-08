package com.luisvillazon.game.model;

/*
    Wind Turbine Power Station
    Throttle is determined by the wind speed
    Damaged at high output levels
    can be shut down completely
    no pollution
    no operating costs
    bird strike?
 */

    import com.luisvillazon.game.main.*;

    import java.awt.*;
    import java.util.ArrayList;
    import java.util.concurrent.ThreadLocalRandom;

public class WindTurbine extends PowerStation {
    private final int MINIMUM_WIND = 8; // threshold % required to turn the blades
    private final int UNIT_COST = 0; // abitrary cost per unit of electricity produced
    private final double UNIT_POLLUTION = 0.0; // arbitrary units
    private int x, y; // top left of the hit box for the turbine blades
    private int width, height; //hit box for the blades, centred on x,y
    private double virtualBladeNumber = 0.0;
    private int bladeFrameNumber = 0;
    private int renderFrame = 0;
    private Sky sky; // allows the turbine to react to the wind
    private int previousWindSpeed = 0;

    public WindTurbine(Sky s, int x, int y, int width, int height) {
        super("wind turbine");
        setUI(new WindTurbineUI(this, "Wind Turbine"));
        setMaxOutput(50); // maximum power (in MW) of the station at full throttle
        throttle = new SimpleThrottle(0, 0, 0.1,  0.1);
        sky = s;
        setActive(false);
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
    public void setActive(boolean state) {
        super.setActive(state);
        if (state == false) {
            setThrottle(0);
            previousWindSpeed = 0;
        }
    }

    @Override
    public void update() {
        super.update();
        int w = (int) sky.getWindSpeed();
        if (isActive()) {
            // set throttle proportional to wind speed
            if (w != previousWindSpeed) {
                previousWindSpeed = w;
                // blades don't turn at all if the wind is too slow
                if (w >= MINIMUM_WIND) {
                    setThrottle(w);
                } else {
                    setThrottle(0);
                }
            }
        }
        WindTurbineUI ui = (WindTurbineUI) getUI();
        ui.setMeter(w);
    }



    @Override
    public void render(Graphics g) {
        super.render(g);
        // TODO spin turbine blades proportional to throttle
        g.drawImage(Resources.turbineFrame.get(bladeFrameNumber),
                Resources.L2_TURBINE_X+Resources.L2_BACKGROUND_OFFSET,
                Resources.L2_TURBINE_Y, null);

        // animate rotation proportional to throttle by adding a non-integer number
        // of blade animation frames each frame
        virtualBladeNumber = virtualBladeNumber + (double)throttle.getCurrentThrottle()/50;
        bladeFrameNumber = (int)virtualBladeNumber % 24;

    }

    public void setWindSpeed(int s) {
        // DEBUG - just used to test so I can set he wind speed from the slider UI
        sky.testWindSpeed = s;
    }
}