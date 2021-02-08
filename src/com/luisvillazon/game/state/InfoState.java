package com.luisvillazon.game.state;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.MenuButton;
import com.luisvillazon.game.main.Resources;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class InfoState extends State {

    private final int TAB_WIDTH = 100;
    private final int LINE_HEIGHT = 30;
    protected Font titleFont = new Font("Black Wolf", Font.PLAIN, 48);
    protected Font normalFont = new Font("BahnSchrift, SemiBold, Semicondensed", Font.PLAIN, 48);
    protected BufferedImage backgroundImage;
    protected Color backgroundColor;

    @Override
    public void init() {
        GameMain.staticSideBar.setVisible(false);
    }

    protected int tab(int t) {
        return TAB_WIDTH * t;
    }

    protected int line(int ln) {
        return LINE_HEIGHT * ln;
    }

    @Override
    public void render(Graphics g) {
        // display background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, null);
        } else {
            g.setColor(backgroundColor); // should be paritally transparent if used with a bg image
            g.fillRect(0,0, GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT);
        }
    }
}

