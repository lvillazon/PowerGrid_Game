package com.luisvillazon.game.state;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.MenuButton;
import com.luisvillazon.game.main.Resources;

public class MenuState extends InfoState {

    private final int ACTION_LEVEL1 = 1;
    private final int ACTION_LEVEL2 = 2;
    private int actionRequested;
    private boolean changeStatePlease = false;
    private MenuButton L1;
    private MenuButton L2;
    private Image menuImage;


    @Override
    public void init() {
        System.out.println("SEQUENCE: init MenuState");
        actionRequested = 0;
        backgroundImage = Resources.welcome;
        backgroundColor = Resources.GLASS;
        L1 = new MenuButton("Level 1", tab(2), line(5), tab(2), line(2));
        L2 = new MenuButton("Level 2", tab(2), line(8), tab(2), line(2));
        createScreenLayout();

    }

    @Override
    public void update() {
        if (actionRequested == ACTION_LEVEL1) {
            setCurrentState(new Level1State()); // start game on level 1
        }
        if (actionRequested == ACTION_LEVEL2) {
            setCurrentState(new Level2State()); // start game on level 2
        }
    }

    private void createScreenLayout() {
        // avoids screen flicker by building the static screen elements once, directly
        // onto the inherited backgroundImage, instead of redrawing them on every render call
        int marginX = tab(1)/2;
        int marginY = line(1)/2;
        int gutterX = 10;
        int gutterY = gutterX;
        Graphics g = backgroundImage.getGraphics();
        g.setColor(backgroundColor); // should be partially transparent if used with a bg image
        g.fillRoundRect(marginX,marginY, tab(5), line(18), gutterX*2, gutterY*2);
        g.setColor(Resources.SMOKED_GLASS);
        g.fillRoundRect(marginX + gutterX, marginY + gutterY,
                tab(5)-gutterX*2, line(18)-gutterY*2,
                gutterX*2, gutterY*2);
        g.setColor(Resources.DARK_BROWN);
        g.setFont(titleFont);
        g.drawString("Power Grid", tab(1), line(4));

        // draw menu buttons
        // TODO make them behave more like buttons
        L1.draw(g);
        L2.draw(g);
    }

    @Override
    public void onClick(MouseEvent e) {
        // setting a flag rather than performing the level switch here directly
        // because the action listener methods run in a different thread from the game
        // and so cannot be guaranteed to initialise the level properly before the game
        // attempts to render the first frame

        if (L1.isAt(e.getX(), e.getY())) {
            actionRequested = ACTION_LEVEL1;
        }
        if (L2.isAt(e.getX(), e.getY())) {
            actionRequested = ACTION_LEVEL2;
        }
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        // TODO
        System.out.println("key down");
     }

    @Override
    public void onKeyRelease(KeyEvent e) {
        // TODO
        System.out.println("key up");
    }

}
