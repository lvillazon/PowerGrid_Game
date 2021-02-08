package com.luisvillazon.game.model;

import com.luisvillazon.game.main.Game;
import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.Resources;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.luisvillazon.game.main.GameMain.GAME_HEIGHT;
import static com.luisvillazon.game.main.GameMain.GAME_WIDTH;

public class Sky {
    // handles all the background atmospherics:
    // sky colour
    // sun and moon
    // wind speed
    // clouds
    // smoke?
    private final int WIND_VARIABILITY = 5;
    private final int SKY_SHADING_STEPS = 2; // how minutes between color shifts during a game day
    private final Color SUN_COLOR = Resources.SUN_YELLOW;
    private final Color TEXTBOX_COLOR = Resources.SMOKED_GLASS;
    private ArrayList<Color> skyColors;
    private double windspeed;
    private int[]dailyWind;
    private ArrayList<Cloud> clouds;
    private int verticalVariation; // how much wiggle there is when determining the initial y pos of a cloud
    public int testWindSpeed = 0; // DEBUG - just so I can control the wind directly with the UI slider

    public Sky() {
        skyColors = new ArrayList<Color>();
        buildSkyColors();
        clouds = new ArrayList<Cloud>();
        windspeed = ThreadLocalRandom.current().nextInt(10, 30);
        verticalVariation = 400;
    }

    public void setWindForecast(int[] windForecast) {
        dailyWind = windForecast;
        windspeed = dailyWind[TimeOfDay.getDay()%7];
    }

    Color blend( Color c1, Color c2, double ratio ) {
        // smoothly mix between 2 sky colours. Taken from
        // https://stackoverflow.com/questions/19398238/how-to-mix-two-int-colors-correctly
        if ( ratio > 1f ) ratio = 1f;
        else if ( ratio < 0f ) ratio = 0f;
        double iRatio = 1.0f - ratio;

        int i1 = c1.getRGB();
        int i2 = c2.getRGB();

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int)((a1 * iRatio) + (a2 * ratio));
        int r = (int)((r1 * iRatio) + (r2 * ratio));
        int g = (int)((g1 * iRatio) + (g2 * ratio));
        int b = (int)((b1 * iRatio) + (b2 * ratio));

        return new Color( a << 24 | r << 16 | g << 8 | b );
    }

    private double getRatio(int startMin, int endMin, int timeOfDay) {
        int period = endMin - startMin;
        int elapsed = timeOfDay - startMin;
        double ratio = (double)elapsed/period;
        return ratio;
    }

    private void buildSkyColors() {
        // loads the skyColor arraylist with a colour for each hour of the day
        Color midnight = new Color(0x27, 0x21, 0x4e);
        Color red = new Color(0xff, 0x6b, 0x3e);
        Color orange = new Color(0xf7, 0xc1, 0x6a);
        Color midday = new Color(0xb5, 0xd6, 0xe0);
        double ratio;
        Color hourColor;

        for (int minute=0; minute<24*60; minute = minute + SKY_SHADING_STEPS) {
            int hour = TimeOfDay.minutesToHours(minute);
            if (hour <= 4) {
                hourColor = midnight;
            } else if (hour <= 6) {
                hourColor =  blend(midnight, orange, getRatio(TimeOfDay.hoursToMinutes(6),TimeOfDay.hoursToMinutes(7), minute));
            } else if (hour <= 7) {
                hourColor =  blend(orange, midday, getRatio(TimeOfDay.hoursToMinutes(7),TimeOfDay.hoursToMinutes(8), minute));
            } else if (hour <= 16) {
                hourColor =  midday;
            } else if (hour <= 17) {
                hourColor =  blend(midday, red, getRatio(TimeOfDay.hoursToMinutes(17),TimeOfDay.hoursToMinutes(18), minute));
            } else if (hour <= 20) {
                hourColor =  blend(red, midnight, getRatio(TimeOfDay.hoursToMinutes(19),TimeOfDay.hoursToMinutes(21), minute));
            } else {
                hourColor =  midnight;
            }
            skyColors.add(hourColor);
        }
    }

    private Color getSkyColor(int timeOfDay) {
        // convert the time (in minutes since midnight
        // to an appropriate colour for this time
        int i = TimeOfDay.getMinutes()/SKY_SHADING_STEPS;
        return skyColors.get(i);
    }

    private void drawSunAndMoon(Graphics g) {
        // draw the sun or moon as appropriate
        int globeRadius = 50;
        int skyCentreX = (GAME_WIDTH-GameMain.staticSideBar.getWidth())/2 - globeRadius/2;
        int skyCentreY = GAME_HEIGHT/2;
        int skyRadius = skyCentreX;
        // timeOfDay runs from 0 (midnight) to 60*24-1 (11:59)
        // angle is 0 to 360
        // but at 0 time, we want angle to be 270 for the moon (directly overhead)
        // and 90 for the sun (directly below)
        // so moondegrees = ((double)timeOfDay/(60*24)*360+270)%360;
        // so moondegrees = ((double)timeOfDay/(60*24)*360+90)%360;
        // precalculating the static arithmetic
        double timeConversion = (double)TimeOfDay.getMinutes()/4;
        double moonDegrees = (timeConversion+270)%360;
        double sunDegrees = (timeConversion+90)%360;
        double moonAngle = Math.toRadians(moonDegrees);
        double sunAngle = Math.toRadians(sunDegrees);
        int sunX = (int)(skyCentreX + Math.cos(sunAngle) * skyRadius);
        int sunY = (int)(skyCentreY + Math.sin(sunAngle) * skyRadius);
        int moonX = (int)(skyCentreX + Math.cos(moonAngle) * skyRadius);
        int moonY = (int)(skyCentreY + Math.sin(moonAngle) * skyRadius);

        g.setColor(SUN_COLOR);
        g.fillOval(sunX, sunY, globeRadius, globeRadius);
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(moonX, moonY, globeRadius, globeRadius);
        g.setColor(getSkyColor(TimeOfDay.getMinutes()));
        g.fillOval(moonX-globeRadius/4, moonY-globeRadius/4, globeRadius, globeRadius);
    }

    public void addClouds(int numberOfClouds) {
        // each level can decide how many clouds it should have
        // they are initially positioned randomly and scroll across the screen
        // wrapping around and randomising their height when they reach the right-hand edge
        for (int i=0; i< numberOfClouds; i++){
            int cloudY = ThreadLocalRandom.current().nextInt(0, verticalVariation+1) + 20;
            // x position is centred on the middle part of the screen
            int cloudX = ThreadLocalRandom.current().nextInt(0, GAME_WIDTH);
            clouds.add(new Cloud(cloudX, cloudY));
        }
    }

    /*
    private void addCloud() {
        // new clouds start off-screen on the left
        int cloudY = ThreadLocalRandom.current().nextInt(0, verticalVariation+1) + 20;
        int cloudX = -100;
        clouds.add(new Cloud(cloudX, cloudY));
    }

    private void removeCloud() {
        // remove each cloud as it leaves the screen
        if (clouds.size() > 0) {
            clouds.remove(0);
        }
    }
*/

    private void resetCloud(int i) {
        // as each cloud leaves the screen it is repositioned on the left at a new height
        clouds.get(i).setY(ThreadLocalRandom.current().nextInt(0, verticalVariation+1) + 20);
        clouds.get(i).setX(-100);
    }

    public void update() {
        for (int i=0; i<clouds.size(); i++) {
            // move clouds, according to the wind
            clouds.get(i).move(altitudeWindSpeed(clouds.get(i).getY()));
            if (clouds.get(i).getX() > GAME_WIDTH) {
                resetCloud(i);
            }
        }
    }

    private double altitudeWindSpeed(int y) {
        // wind increases smoothly toward the top of the screen, ie as y -> 0
        double minWind = 0.25;
        double adjustedWind = (getWindSpeed()/20) - 4*(double)y/GAME_HEIGHT;
        //System.out.println(adjustedWind);
        return Math.max(minWind, adjustedWind);
    }

    public double getWindSpeed() {
        // the actual wind speed drifts up or down to match the forecast wind for the day
        if (dailyWind != null) {
            final double WIND_DRIFT = 0.1;
            int forecastWind = dailyWind[TimeOfDay.getDay() % 7];
            if (windspeed < forecastWind - 1) {
                windspeed = windspeed + 0.005;
            } else if (windspeed > forecastWind + 1) {
                windspeed = windspeed - 0.005;
            }
        }
        return windspeed;
    }

    public void render(Graphics g) {
        // draw the correct sky colour for the time of day
        g.setColor(getSkyColor(TimeOfDay.getMinutes()));
        g.fillRect(0,0, GAME_WIDTH, GAME_WIDTH);

        drawSunAndMoon(g);

        if (clouds.size() > 0) {
            for (Cloud c: clouds) {
                c.render(g, Resources.cloudColors[(int)getWindSpeed()]);
            }
        }
    }
}
