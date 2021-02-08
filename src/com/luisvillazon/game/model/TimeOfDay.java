package com.luisvillazon.game.model;

import com.luisvillazon.game.main.GameMain;

public class TimeOfDay {
    private static final int GAME_DAY_LENGTH = 30; // time in seconds for a game day to elapse
    private static int updateInterval = 86400 / ((GAME_DAY_LENGTH * 1000) / GameMain.sGame.TARGET_DRAW_TIME);
    private static int seconds =0; // game seconds elapsed since midnight
    private static int day = 0;
    private static final String[] dayName = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    public static boolean endOfLevel() {
        if (day==7) { // end after this many days (DEBUG, should normally be a week)
            return true;
        } else {
            return false;
        }
    }

    public static int getSeconds() { return seconds; }

    public static int getMinutes() {
        return seconds / 60;
    }

    public static int getHour() {
        return getMinutes() / 60;
    }

    public static int getDay() {
        return day;
    }

    public static String getDayName() {
        return dayName[(day + 7)%7];
    }

    public static String getDayName(int d) {
        d = (d + 7)%7;
        return dayName[d];
    }

    public static int hoursToMinutes(int hours) {
        return hours * 60;
    }

    public static int minutesToHours(int minutes) {
        return minutes / 60;
    }

    public static void reset() {
        // return time back to 0
        // used for each new level
        day = 0;
        seconds = 0;
    }


    public static void update() {
        /* if we want a game day to last GD seconds
           86400 game seconds must elapse in GD real-word seconds
           if each frame takes T milliseconds to draw
           then GD * 1000 / T frames pass for each game day
           so 86400 / (GD * 1000 / T) game seconds must pass each frame
         */
        seconds = seconds + updateInterval;
        if (seconds >= 86400) { // 86400 = seconds in 24 hours
            seconds = 0;
            day++;
        }
    }
}
