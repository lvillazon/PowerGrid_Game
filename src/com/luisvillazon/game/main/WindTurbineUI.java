package com.luisvillazon.game.main;

import com.luisvillazon.game.model.Meter;
import com.luisvillazon.game.model.PowerStation;
import com.luisvillazon.game.model.WindTurbine;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WindTurbineUI extends PowerStationUI { //implements ChangeListener {
    private final ImageIcon onIcon = new ImageIcon(Resources.buttonOnImage);
    private final ImageIcon offIcon = new ImageIcon(Resources.buttonOffImage);
    private JToggleButton onOffButton;
    private JSlider powerSlider;
    private Meter windGauge;
    private JLabel windReadout;

/*
    megawattMeter = new Meter();//grid.getMaxOutput());

    // add all the widgets to the panel
    setBackground(Sidebar.PANEL_COLOR);
        contents.add(megawattMeter, BorderLayout.CENTER);
        contents.add(megawattReadout, BorderLayout.LINE_END);
}

    public void update() {
        megawattMeter.setValue(grid.totalOutput(), grid.getMaxOutput());
        megawattReadout.setText(grid.totalOutput() + "MW ");


 */

    public WindTurbineUI(PowerStation station, String stationName) {
        super(station, stationName);

        //FlowLayout turbineUILayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        BorderLayout turbineUILayout = new BorderLayout();
        JPanel turbineUIPanel = new JPanel(turbineUILayout);
        turbineUIPanel.setBackground(Sidebar.PANEL_COLOR);
        onOffButton = new JToggleButton();
        onOffButton.setIcon(offIcon);
        onOffButton.setPressedIcon(onIcon);
        onOffButton.setIconTextGap(-20);
        onOffButton.setBackground(Sidebar.PANEL_COLOR);
        onOffButton.setContentAreaFilled(false);
        onOffButton.setFocusPainted(false);
        onOffButton.setBorderPainted(false);
        onOffButton.setMargin(new Insets(2, 2, 0, 2));
        onOffButton.addActionListener(this);
        onOffButton.setSelected(true);

        windGauge = new Meter();

        // DEBUG slider control to test turbine animation
        /*
        powerSlider = new JSlider();
        powerSlider.setOrientation(JSlider.HORIZONTAL);
        powerSlider.setMaximum(100);
        powerSlider.setValue(0);
        powerSlider.addChangeListener(this);
        turbineUIPanel.add(powerSlider, BorderLayout.PAGE_START);
         */
        turbineUIPanel.add(onOffButton, BorderLayout.LINE_START);
        turbineUIPanel.add(windGauge, BorderLayout.CENTER);
        contents.add(turbineUIPanel);
        contents.setPreferredSize(new Dimension(0, 50));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // toggle the turbine on and off
        if (!station.isActive()) {
            onOffButton.setIcon(onIcon);
            station.setActive(true);
            station.setThrottle(windGauge.getValue());
        } else {
            onOffButton.setIcon(offIcon);
            station.setActive(false);
        }
    }

    /*
    @Override
    public void stateChanged(ChangeEvent e) {
        ((WindTurbine) station).setWindSpeed(powerSlider.getValue());
        windGauge.setValue(powerSlider.getValue(), 100);
        if (station.isActive()) {
            //station.setThrottle(powerSlider.getValue());
        }
    }
    */

    public void setMeter(int w) {
        windGauge.setValue(w, 100);
    }

 }