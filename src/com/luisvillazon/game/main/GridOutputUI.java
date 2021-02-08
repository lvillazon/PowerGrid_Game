package com.luisvillazon.game.main;

import com.luisvillazon.game.model.Meter;
import com.luisvillazon.game.model.PowerGrid;

import javax.swing.*;
import java.awt.*;

public class GridOutputUI extends SidebarPanel {
    // read-only UI to report the current state of the grid
    private JLabel megawattReadout;
    private Meter megawattMeter;
    private PowerGrid grid;

    public GridOutputUI(PowerGrid grid) {
        super("Grid Output");
        this.grid = grid;

        megawattReadout = new JLabel("", JLabel.CENTER);
        //System.out.println("GRID MAX AT THIS POINT =" + grid.getMaxOutput());
        megawattMeter = new Meter();//grid.getMaxOutput());

        // add all the widgets to the panel
        setBackground(Sidebar.PANEL_COLOR);
        contents.add(megawattMeter, BorderLayout.CENTER);
        contents.add(megawattReadout, BorderLayout.LINE_END);
    }

    public void update() {
        megawattMeter.setValue(grid.totalOutput(), grid.getMaxOutput());
        megawattReadout.setText(grid.totalOutput() + "MW ");
    }
}

