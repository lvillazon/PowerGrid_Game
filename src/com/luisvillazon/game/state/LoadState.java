package com.luisvillazon.game.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import com.luisvillazon.game.main.Resources;

public class LoadState extends State{

    @Override
    public void init() {
        System.out.println("SEQUENCE: init LoadState");
        Resources.load();
        System.out.println("Loaded successfully");
    }

    @Override
    public void update() {
        System.out.println("SEQUENCE: update LoadState");
        // as soon as all resources are loaded the state transitions
        setCurrentState(new MenuState());
    }

    @Override
    public void render(Graphics g) {
        // NOT USED
    }

    @Override
    public void onClick(MouseEvent e) {
        // NOT USED
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        // NOT USED
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        // NOT USED
    }

}
