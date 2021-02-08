package com.luisvillazon.game.main;

import java.awt.*;

public class MenuButton {

    private final int ARC = 20; // radius of the rounded corners
    private final int MARGIN = 10;
    private Color textColor;
    private int x;
    private int y;
    private int width;
    private int height;
    private String label;

    public MenuButton(String text, int x, int y, int w, int h) {
        label = text;
        textColor = Resources.AMBER;
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public boolean isAt(int x, int y) {
        // returns true if the button draw area includes the point x,y
        // use this for simple click detection
        if ((x >= this.x) && (x <= this.x + this.width) &&
                (y >= this.y) && (y <= this.y + this.height)) {
            return true;
        }
        return false;
    }

    public void draw(Graphics g) {
        g.setColor(Resources.DARK_BROWN);
        g.drawRoundRect(x, y, width, height, ARC, ARC);
        g.setColor(Resources.SMOKED_GLASS);
        g.fillRoundRect(x, y, width, height, ARC, ARC);
        g.setColor(textColor);
        g.drawString(label, x + MARGIN, y + height - MARGIN);
    }
}
