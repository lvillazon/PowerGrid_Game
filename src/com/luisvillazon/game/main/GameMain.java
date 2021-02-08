package com.luisvillazon.game.main;
import javax.swing.JFrame;
import java.awt.*;
/* game development framework taken from
   The Beginners Guide to Android Development
   by James Cho
 */

public class GameMain {
    //TODO add these to an options screen
    public static final boolean SMOKE_TRANSPARENCY = false;

    public static final String GAME_TITLE = "PowerGrid";
    public static final int GAME_WIDTH = 1024, GAME_HEIGHT = 768;
    public static Game sGame; // I think the s is to remind me that this is static
    public static Sidebar staticSideBar;

    public static void main(String[] args) {
        System.out.println("SEQUENCE: GameMain main()");
        JFrame frame = new JFrame(GAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        sGame = new Game(GAME_WIDTH, GAME_HEIGHT);
        frame.add(BorderLayout.CENTER, sGame);
        System.out.println("Sidebar loads here");
        staticSideBar = new Sidebar(GAME_WIDTH, GAME_HEIGHT);
        frame.add(BorderLayout.EAST, GameMain.staticSideBar);
        staticSideBar.setVisible(false);

        frame.pack(); // tell window to resize to fit components (or PreferredSize)
        frame.setIconImage(Resources.gameIcon);
        frame.setVisible(true);
    }
}
