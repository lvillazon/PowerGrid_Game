package com.luisvillazon.game.state;

import com.luisvillazon.game.main.Resources;
import com.luisvillazon.game.model.*;

import java.awt.*;

public class Level2State extends GameState {

    public Level2State() {
        super();
        System.out.println("SEQUENCE: Level 2 constructor");
        sky.addClouds(20);
        sky.setWindForecast(new int[]{20,80,5,100,50,0,100});
    }

    @Override
    public void init() {
        super.init();
        System.out.println("SEQUENCE: init Level 2");
        // add all the buildings for the level
        addStation(new CoalFired(300, 400, 398, 434, 477, 453,
                176,323,280,250,315,250));
        addStation(new WindTurbine(sky,587, 67, 121, 213));
        addConsumer(new House(6,221, 603, 397, 698)); // counts as only 6 houses, for game balance
        addConsumer(new Office(531, 464, 653, 584));
    }

    @Override
    public void update() {
        super.update();

        // weekend "boss fight"
        // office deactivates
        // house demand is doubled on sat, halved on sun

    }



    @Override
    protected void drawScenery(Graphics g) {
        // overlay the scene image for level 2
        g.drawImage(Resources.level2Background, Resources.L2_BACKGROUND_OFFSET, 0, null);
    }
/*
    @Override
    public void render(Graphics g) {
        super.render(g);
        //w.render(g);
    }

 */
}
