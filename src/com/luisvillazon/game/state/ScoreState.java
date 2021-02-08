package com.luisvillazon.game.state;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.MenuButton;
import com.luisvillazon.game.main.Resources;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ScoreState extends InfoState {

    private final int ACTION_MENU = 1;
    private final int ACTION_QUIT = 2;
    private int actionRequested;
    private int generated;
    private int wasted;
    private int outages;
    private int pollution;
    private int score;
    private MenuButton B1, B2;

    public ScoreState(int generated, int wasted, int outages, int pollution) {
        // the parameters are the score metrics passed from GameState
        this.generated = generated;
        this.wasted = wasted;
        this.outages = outages;
        this.pollution = pollution;
        this.score = generated - wasted - outages - pollution;
    }

    @Override
    public void init() {
        System.out.println("SEQUENCE: init ScoreState");
        actionRequested = 0;
        backgroundImage = Resources.controlRoom;
        backgroundColor = Resources.GLASS;
        B1 = new MenuButton("Menu", tab(2), line(19), tab(2), line(2));
        B2 = new MenuButton("Quit", tab(5), line(19), tab(2), line(2));
        GameMain.staticSideBar.setVisible(false);
    }

    @Override
    public void update() {
        if (actionRequested == ACTION_MENU) {
            setCurrentState(new MenuState()); // start game on level 1
        }
        if (actionRequested == ACTION_QUIT) {
            GameMain.sGame.exit(); // bail immediately
        }
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
        // TODO add some nice animations as the different metrics appear
        int marginX = 20;
        int marginY = 20;
        int gutterX = 10;
        int gutterY = gutterX;
        g.setColor(backgroundColor); // should be paritally transparent if used with a bg image
        g.fillRoundRect(marginX,marginY,
                tab(9) - marginX*2, line(22),
                gutterX*2, gutterY*2);
        g.fillRoundRect(marginX,marginY,
                tab(9) - marginX*2, line(22),
                gutterX*2, gutterY*2);
        g.setColor(Resources.SMOKED_GLASS);
        g.fillRoundRect(marginX + gutterX, marginY + gutterY,
                tab(9)-marginX*2-gutterX*2, line(22)-gutterY*2,
                gutterX*2, gutterY*2);
        g.setColor(Resources.DARK_BROWN);
        g.setFont(titleFont);
        g.drawString("Week 1", tab(1), line(3));
        g.drawString("Performance Review", tab(1), line(5));
        g.setColor(Resources.DARK_GREEN);
        g.setFont(normalFont);
        g.drawString("electricity generated:", tab(1), line(7));
        g.drawString("wasted:", tab(1), line(9));
        g.drawString("outages:", tab(1), line(11));
        g.drawString("pollution", tab(1), line(13));
        g.drawString("score:", tab(1), line(15));
        g.fillRect(tab(6),line(13)+gutterY, 150,5);

        g.drawString(Integer.toString(generated), tab(6), line(7));
        g.setColor(Resources.RED);
        g.drawString("-" + Integer.toString(wasted), tab(6), line(9));
        g.drawString("-" + Integer.toString(outages), tab(6), line(11));
        g.drawString("-" + Integer.toString(pollution), tab(6), line(13));
        if (score >= 0) {
            g.setColor(Resources.DARK_GREEN);
        }
        g.drawString(Integer.toString(score), tab(6), line(15));

        // stars

        // buttons
        g.setFont(titleFont);
        B1.draw(g);
        B2.draw(g);
    }

    @Override
    public void onClick(MouseEvent e) {
        if (B1.isAt(e.getX(), e.getY())) {
            actionRequested = ACTION_MENU;
        }
        if (B2.isAt(e.getX(), e.getY())) {
            actionRequested = ACTION_QUIT;
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
