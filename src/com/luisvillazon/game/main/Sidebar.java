package com.luisvillazon.game.main;

//import javafx.geometry.Side;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Sidebar extends JPanel implements ActionListener {
    private int sidebarWidth, sidebarHeight;
    private final int TOTAL_PANELS = 5;
    private ArrayList<SidebarPanel> instrumentPanels;
    private int nextFreePanel; // keeps track of which sidebar panels have been assigned already
    static final Color PANEL_COLOR = Resources.LIGHT_GREEN;

    public Sidebar(int gameWidth, int gameHeight) {
        System.out.println("SEQUENCE: Sidebar constructor");
        this.sidebarWidth = 250;
        this.sidebarHeight = gameHeight;
        setPreferredSize(new Dimension(sidebarWidth, sidebarHeight));
        setBackground(PANEL_COLOR);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setOpaque(true);
    }

    public void init() {
        System.out.println("SEQUENCE: init Sidebar");
        instrumentPanels = new ArrayList<SidebarPanel>(TOTAL_PANELS);
        for (int i=0; i<TOTAL_PANELS; i++) {
            SidebarPanel s = new SidebarPanel(" ");
            instrumentPanels.add(s);
            add(s); // add to the JPanel as well, so it will display
        }
        nextFreePanel = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Quit")) {
            GameMain.sGame.exit();
        }
    }

    public void addToSideBar(SidebarPanel s) {
        // allows other UI elements (eg power station UIs) to add themselves to the sidebar
        // this will replace the next available blank panel
        // This is necessary to prevent the panels from autosizing themselves
        if (nextFreePanel < TOTAL_PANELS) {
            System.out.println("adding to panel " + nextFreePanel);
            if (s == null) {
                System.out.println("ERROR! null sidebar panel");
            }
            if (instrumentPanels == null) {
                System.out.println("ERROR! null instrument panel");
            }
            instrumentPanels.set(nextFreePanel, s);
            nextFreePanel++;
            // take all the panels out and add them back in again to refresh the sidebar
            // this is slightly scruffy but it only happens when the screen is initialised
            // the alternative would be to dynamically check if there were unfilled
            // sidebar slots and create new panels in paint(), which would be more wasteful
            this.removeAll();
            for (int i=0; i<TOTAL_PANELS; i++) {
                add(instrumentPanels.get(i));
            }
        }
    }
}

