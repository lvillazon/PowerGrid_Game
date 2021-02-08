package com.luisvillazon.game.state;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.Resources;
import com.luisvillazon.game.model.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.luisvillazon.game.main.GameMain.*;

public class Level1State extends GameState {

    //TEST for L2
    private int backgroundFrameNumber = 0;

    public Level1State() {
        super();
        System.out.println("SEQUENCE: Level 1 constructor");
        sky.addClouds(2);
    }

    @Override
    public void init() {
        super.init();
        System.out.println("SEQUENCE: init Level 1");
        // add all the buildings for the level
        addStation(new CoalFired(505, 424, 635,447,683, 490,
                425, 324, 602, 234, 640, 234));
        addConsumer(new House(3,538, 620, 617, 683)); // represents 3 houses on the screen
        addConsumer(new Factory(31, 399, 328, 425, 364,458));
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected void drawScenery(Graphics g) {
        // overlay the scene image for level 1
        g.drawImage(Resources.level1Background, 0, 0, null);
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }
}
