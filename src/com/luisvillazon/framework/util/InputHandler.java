package com.luisvillazon.framework.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import com.luisvillazon.game.state.State;

public class InputHandler implements KeyListener, MouseListener {
    // This dispatches mouse clicks and key presses to the
    // corresponding methods for the current game state

    private State currentState;

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentState.onClick(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentState.onKeyPress(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentState.onKeyRelease(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // NOT USED
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // NOT USED
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // NOT USED
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // NOT USED
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // NOT USED
    }
}
