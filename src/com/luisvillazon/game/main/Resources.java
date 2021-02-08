package com.luisvillazon.game.main;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Resources {
    public static final String FILE_PATH = "/resources/";
    public static final int L2_TURBINE_X = 870;
    public static final int L2_TURBINE_Y = 50;
    public static final int L2_TURBINE_WIDTH = 140;
    public static final int L2_TURBINE_HEIGHT = 250;
    public static final int L2_BACKGROUND_OFFSET = -300;

    public static final Color BUFF = new Color(213, 193, 160);
    public static final Color MID_BROWN = new Color(79, 71, 37);
    public static final Color DARK_BROWN = new Color(34, 27, 18);
    public static final Color LIGHT_GREY = new Color(170, 178, 186);
    public static final Color DARK_GREY = new Color(67, 76, 68);
    public static final Color GREY_GREEN = new Color(136, 160, 151);
    public static final Color LIGHT_GREEN = new Color(161, 198, 156);
    public static final Color MID_GREEN = new Color(105, 137, 102);
    public static final Color DARK_GREEN = new Color(42, 73, 62);
    public static final Color KHAKI = new Color(156, 172, 132);
    public static final Color RED = new Color(207, 21, 7);
    public static final Color AMBER = new Color(241, 186, 20);
    public static final Color OFF_WHITE = new Color(0xF2, 0xF5, 0xE1);
    public static final Color WARM_YELLOW = new Color(0xfc, 0xe6, 0xb7);
    public static final Color SUN_YELLOW = new Color(0xf6, 0xdb, 0xa4);
    public static final Color GLASS = new Color(255,255,255,127);
    public static final Color SMOKED_GLASS = new Color(34,27,18, 64);

    // cloud colors are created as a smooth gradient of shades of grey
    // 101 so it can be indexed using the wind speed, which varies from 0 to 100
    public static Color[] cloudColors = new Color[101];

    public static BufferedImage welcome, controlRoom;
    public static BufferedImage gameIcon, houseIcon, factoryIcon, officeIcon;
//    public static BufferedImage houseImage, factoryImage, coalFiredImage;
    public static BufferedImage panelScrewImage;
    public static BufferedImage buttonOffImage, buttonOnImage;
    public static ArrayList<BufferedImage> turbineFrame = new ArrayList<BufferedImage>();
    public static BufferedImage level1Background;
    public static BufferedImage level2Background;

    public static void load() {
        System.out.println("Loading resources...");

        //cropFrames(); // uncomment this line to recreate the cropped, turbine frame files

        welcome = loadImage("welcome.jpg");
        controlRoom = loadImage("control room.png");
        level1Background = loadImage("Level 1.png");
        level2Background = loadImage("Level 2.png");
        gameIcon = loadImage("icon.png");
        //houseImage = loadImage("house.png");
        //factoryImage = loadImage("factory.png");
        //coalFiredImage = loadImage("coal-fired.png");
        panelScrewImage = loadImage("screw_transparent.png");
        buttonOffImage = loadImage("button_off.png");
        buttonOnImage = loadImage("button_on.png");
        houseIcon = loadImage("houseIcon.png");
        factoryIcon = loadImage("factoryIcon.png");
        officeIcon = loadImage("officeIcon.png");

        // load all the frames that make up the turbine animation
        for (int i=1; i<=24; i++) {
            // load the frames in reverse order, because I want the turbine to rotate clockwise
            turbineFrame.add(loadImage("turbine_frame" + (25-i) + ".png"));
        }

        // generate 101 separate cloud colours from white to grey
        for (int i=0; i<101; i++) {
            int cloudTint = (int)(255 - i * 1.5);
            cloudColors[i] = new Color(cloudTint, cloudTint, cloudTint);
        }
    }

    private static AudioClip loadSound(String filename) {
       URL fileURL = Resources.class.getResource(FILE_PATH + filename);
       return Applet.newAudioClip(fileURL);
    }

    private static BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        System.out.println("Looking for resource at " + FILE_PATH + filename);
        try {
            img = ImageIO.read(Resources.class.getResourceAsStream(FILE_PATH + filename));
        } catch (IOException e) {
            System.out.println("Error while reading: " + filename);
            e.printStackTrace();
        }
        return img;
    }

    private static void cropFrames() {
        // level building tool to crop the same region from multiple images at once
        // this is used to grab the frames for the wind turbine animation
        // so that I can composite it back in later
        // This allows the rest of the scene to be changed without needing to create
        // multiple frames of the entire scene
        BufferedImage wholeScene, croppedScene;
        for (int i=1; i<=24; i++) {
            wholeScene = loadImage("Level 2 f" + i + ".png");
            croppedScene = wholeScene.getSubimage(L2_TURBINE_X, L2_TURBINE_Y,
                    L2_TURBINE_WIDTH,L2_TURBINE_HEIGHT);
            File croppedFile = new File("src\\resources\\turbine_frame" + i + ".png");
            try {
                ImageIO.write(croppedScene, "png", croppedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Now write the original scene back, but with a transparent mask cut in the cropped portion,
        // to allow it to be composited back.
        wholeScene = loadImage("Level 2 test.png");
        Graphics2D g2 = (Graphics2D)wholeScene.getGraphics();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(L2_TURBINE_X, L2_TURBINE_Y, L2_TURBINE_WIDTH, L2_TURBINE_HEIGHT);

        File maskFile = new File("src\\resources\\Level 2.png");
        try {
            ImageIO.write(wholeScene, "png", maskFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
