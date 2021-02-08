package com.luisvillazon.game.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import com.luisvillazon.game.main.GameMain;

public abstract class State {

    public abstract void init();
    public abstract void update();
    public abstract void render(Graphics g);
    public abstract void onClick(MouseEvent e);
    public abstract void onKeyPress(KeyEvent e);
    public abstract void onKeyRelease(KeyEvent e);

    public void setCurrentState(State newState) {
        // allows one state to transition to another by accessing the method of the Game object
        GameMain.sGame.setCurrentState(newState);
    }
}
