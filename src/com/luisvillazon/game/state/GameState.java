package com.luisvillazon.game.state;

import com.luisvillazon.game.main.GameMain;
import com.luisvillazon.game.main.Resources;
import com.luisvillazon.game.model.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.luisvillazon.game.main.GameMain.*;

public class GameState extends State {

    private final Color TEXTBOX_COLOR = Resources.SMOKED_GLASS;
    private Font scoreFont;
    private ArrayList<PowerStation> stations;
    private ArrayList<Consumer> consumers;
    protected PowerGrid grid;
    private Calendar day;
    protected Sky sky;

    public GameState() {
        System.out.println("SEQUENCE: GameState constructor");
        sky = new Sky();
        stations = new ArrayList<PowerStation>();
        consumers = new ArrayList<Consumer>();
//        scoreFont = new Font("Bahnschrift SemiBold Condensed", Font.PLAIN, 36);
        scoreFont = new Font("Black Wolf", Font.PLAIN, 36);
        day = new Calendar(10, 50, 70, 70);
    }


    protected void drawScenery(Graphics g) {
        // doesn't do anything here
        // overridden for each level
        // overlay the scene image for level 1
        //g.drawImage(Resources.gameBackground, 0, 0, null);

        //TEST
        // overlay the scene image for level 2
 //       g.drawImage(Resources.level2Background.get(backgroundFrameNumber), -300, 0, null);
  //      backgroundFrameNumber = (backgroundFrameNumber+1)%24;
        // add status text

    }

    private void drawTextOverlay(Graphics g) {
        // player status info
        // currently, shows the time of day + score
        g.setColor(TEXTBOX_COLOR);
        g.fillRect(0,0, GAME_WIDTH,45);
        g.setColor(Resources.AMBER);
        g.setFont(scoreFont);
        g.drawString("MWhr supplied: " + grid.totalUnitsSupplied(), 10, 34);
        g.drawString("Cost/unit: " + grid.costPerUnit(), 450, 34);
    }

    public PowerGrid getGrid() {
        return grid;
    }

    protected void initGrid(ArrayList<Building> buildings) {
        // create the grid and connect up the buildings
        grid = new PowerGrid();
        for (Building b: buildings) {
            if (b instanceof PowerStation) {
                stations.add((PowerStation) b);
            }
            if (b instanceof Consumer) {
                consumers.add((Consumer) b);
            }
        }
        grid.addAll(stations, consumers);
    }

    protected void addStation(PowerStation p) {
        System.out.println("Adding station " + p.getName() + " to grid...");
        stations.add(p);
        grid.connect(p);
        staticSideBar.addToSideBar(p.getUI());
        System.out.println("...added successfully.");
    }

    protected void addConsumer(Consumer c) {
        consumers.add(c);
        grid.connect(c);
    }

    @Override
    public void init() {
        System.out.println("SEQUENCE: init GameState");
        GameMain.staticSideBar.init();
        grid = new PowerGrid();
        GameMain.staticSideBar.setVisible(true);
        TimeOfDay.reset(); // start each level at the same time of day/ day of week
    }

    @Override
    public void update() {
        sky.update();
        for (PowerStation s: stations) {
            s.update();
        }
        for (Consumer c: consumers) {
            c.update();
        }

        grid.update();
        TimeOfDay.update();
        day.update();
        if (TimeOfDay.endOfLevel() == true) {
            setCurrentState(new ScoreState(grid.totalUnitsProduced(),
                                           grid.totalUnitsProduced()-grid.totalUnitsSupplied(),
                                           grid.totalOutages(),
                                           grid.totalPollution()));
        }
        //System.out.println(powerGrid.voltage());
    }

    @Override
    public void render(Graphics g) {
        sky.render(g);

        // draw the window backgrounds in the correct colour for each building
        for (PowerStation s: stations) {
            s.render(g);
        }

        for (Consumer c: consumers) {
            c.render(g);
        }

        drawScenery(g);
        drawTextOverlay(g);
        day.render(g); // display the calendar at the start of each new day
    }

    @Override
    public void onClick(MouseEvent e) {
        // TODO
        System.out.println("X:" + e.getX() + " Y:" + e.getY());
        System.out.print("Frame draw time:" + GameMain.sGame.getFrameDrawTime() + "ms  ");
        System.out.print("Max fps:" + GameMain.sGame.getFPS() + "  ");
        System.out.println("Draw budget remaining for 60fps:" + (1000 - GameMain.sGame.getFrameDrawTime()*60) + "ms");
    }

    @Override
    public void onKeyPress(KeyEvent e) {
        // TODO
        //System.out.println("key down");
    }

    @Override
    public void onKeyRelease(KeyEvent e) {
        // TODO
        //System.out.println("key up");
    }
}
