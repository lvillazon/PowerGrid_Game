package com.luisvillazon.game.model;

import com.luisvillazon.game.main.Game;
import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.Resources;
//import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.sql.Time;

public class Calendar {
    // displays the day of the week
    // ideally this will be a brief animation at midnight that tears off a page from the calendar
    // to show you the new day and then fades out

    private int cornerX;
    private int cornerY;
    private int animationSpeed;
    private String yesterday;
    private float alpha;
    private int left;
    private int top;
    private int width;
    private int height;
    private Font dayFont;
    private boolean dayFlip;
    private int day;
    private String today;
    private final Color backgroundColor = Resources.OFF_WHITE;
    private final Color borderColor = Color.BLACK;
    private final Color textColor = Color.BLACK;
    private final Color monthBarColor = Resources.RED;
    private BufferedImage todayPage;
    private BufferedImage yesterdayPage;
    AffineTransform pageTearTransform;
    private double angle, xOffset, yOffset;
    private final double ROTATION_SPEED = Math.toRadians(1); // how fast the page rotates and moves when the day changes
    private final double X_SPEED = -1.0;
    private final double Y_SPEED = 5.0;

    public Calendar(int x, int y, int w, int h) {
        left = x;
        top = y;
        width = w;
        height = h;
        cornerX = left + width;
        cornerY = top + height;
        animationSpeed = 4;
        dayFont = new Font("Arial Black", Font.PLAIN, 20);
        dayFlip = false;
        day = TimeOfDay.getDay();
        today = TimeOfDay.getDayName();
        yesterday = "";
        alpha = 1.0f;

        todayPage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        setPage(todayPage, today);
        yesterdayPage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        pageTearTransform = new AffineTransform();
    }

    private void setPage(BufferedImage page, String day) {
        // draw the picture for a single calendar page
        Graphics g = page.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
        g.setColor(monthBarColor);
        g.fillRect(2, 2, width-4, height/3);
        g.setColor(textColor);
        g.setFont(dayFont);
        g.drawString(day, 10, height - height/4);
        g.setColor(borderColor);
        g.drawRect(2,height/3 +6, width -6, 2*height/3 -10);
    }

    private void copyPage(BufferedImage source, BufferedImage destination) {
        // clone the page image so we can use it for the tear-off page as well
        Graphics g = destination.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
    }

    private void updatePageFlipAnimation(AffineTransform at) { //, int angle, int xOffset, int yOffset) {
        // make the old page rotate and slide down/left, so it looks as if it has been torn off the calendar
        // taken from https://stackoverflow.com/questions/4918482/rotating-bufferedimage-instances

        // reset the transform so it isn't cumulative from frame to frame
        at.setToRotation(0.0);
        at.setToTranslation(0.0, 0.0);
        // translate down and left to slide the page away from the calendar
        at.translate(xOffset, yOffset);
        // translate so that the centre of rotation is just a little in from the top left of the page
        at.translate(left+width/4, top);
        // do the actual rotation
        at.rotate(angle);
        // translate back to the original position of the page, so the net result is just a rotation
        at.translate(-width/4, 0);
    }

    private float pageFade() {
        // generate an alpha for the main calendar page so that it fades out by 2am in game time
        final int FADE_START = 60; // minutes past midnight before calendar starts to fade
        final int FADE_END = 120; // minutes past midnight when the calendar is fully faded
        int minutes = TimeOfDay.getMinutes();
        if (minutes < FADE_START) {
            return 1.0f;
        }
        if (minutes < FADE_END)  {
            return 1.0f - (float)(TimeOfDay.getMinutes()-FADE_START)/(FADE_END-FADE_START);
        } else {
            return 0.0f;
        }
    }

    public void update() {
        if (TimeOfDay.getDay() != day) {
            day = TimeOfDay.getDay();
            copyPage(todayPage, yesterdayPage);
            setPage(todayPage, TimeOfDay.getDayName());
            angle = 0.0;
            xOffset = 0.0;
            yOffset = 0.0;
            dayFlip = true;
        }
        if (dayFlip) {
            if (TimeOfDay.getHour()<1) { // TODO actually test for when the old page is off the screen
                angle = angle + ROTATION_SPEED;
                xOffset = xOffset + X_SPEED;
                yOffset = yOffset + Y_SPEED;
            } else {
                dayFlip = false;
            }
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        float pageAlpha = pageFade();

        if (pageAlpha > 0.0f) {
            // set the alpha blending for today's page to handle the fadeout
            // cribbed from http://zetcode.com/gfx/java2d/transparency/
            AlphaComposite acomp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, pageFade());
            g2.setComposite(acomp);
            // draw the page
            g2.drawImage(todayPage, left, top, null);
            g2.setComposite(AlphaComposite.SrcOver);
        }
        //draw the old date page while the "tear-off" animation is playing
        if (dayFlip) {
            updatePageFlipAnimation(pageTearTransform);
            g2.drawImage(yesterdayPage, pageTearTransform, null);
        }
    }
}
