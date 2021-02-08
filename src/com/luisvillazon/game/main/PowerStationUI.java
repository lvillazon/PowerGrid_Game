package com.luisvillazon.game.main;

import com.luisvillazon.game.model.PowerStation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PowerStationUI extends SidebarPanel {
    private final Color outputBackground = Resources.OFF_WHITE;
    private Image stationTypeImage;
    private JPanel outputPanel;
    protected JProgressBar output;
    protected PowerStation station;

    public PowerStationUI(PowerStation station, String stationName) {
        super(stationName);
        this.station = station;

        setBackground(Sidebar.PANEL_COLOR);
        outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.setOpaque(false);
        output = new JProgressBar(0, 100);
        output.setPreferredSize(new Dimension(0,40));
        output.setBackground(outputBackground);
        outputPanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.PAGE_START);
        outputPanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.PAGE_END);
        outputPanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.LINE_START);
        outputPanel.add(Box.createRigidArea(new Dimension(10, 10)), BorderLayout.LINE_END);
        outputPanel.add(output, BorderLayout.CENTER);
        contents.add(outputPanel, BorderLayout.PAGE_START);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Quit")) {
            GameMain.sGame.exit();
        }
        if (e.getActionCommand().equals("on/off")){
            station.setActive(!station.isActive());
        }
    }

    private Color getColor(int powerOutput) {
        // from light yellow through orange to red
        if (powerOutput < 60) {
            return Resources.DARK_GREEN;
        }
        if (powerOutput < 85) {
            return Resources.AMBER;
        }
        return Resources.RED;
    }

    public void update() {
        int displayValue = (int)(100*station.getExactOutput()/station.getMaxOutput());
        output.setValue(displayValue);
        output.setForeground(getColor(displayValue));
    }
}

