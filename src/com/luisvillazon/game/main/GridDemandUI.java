package com.luisvillazon.game.main;

import com.luisvillazon.game.model.Consumer;
import com.luisvillazon.game.model.DemandGraph;
import com.luisvillazon.game.model.Meter;
import com.luisvillazon.game.model.PowerGrid;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class GridDemandUI extends SidebarPanel implements ChangeListener {
    // sliders for each Consumer to change how much of the generated power they receive
    private PowerGrid grid;
    private ArrayList<JSlider> sliders;
    private ArrayList<JLabel> readouts;
    private ArrayList<DemandGraph> demands;

    public GridDemandUI(PowerGrid grid) {
        super("Grid Demand");
        this.grid = grid;
        sliders = new ArrayList<JSlider>();
        readouts = new ArrayList<JLabel>();
        demands = new ArrayList<DemandGraph>();
        contents.setLayout(new BoxLayout(contents, BoxLayout.PAGE_AXIS));
        //contents.add(Box.createRigidArea(new Dimension(10, 5)));
        //contents.setLayout(new GridBagLayout());
        //GridBagConstraints c = new GridBagConstraints();

    }

    public void connect(Consumer c) {
        System.out.println("Connecting " + c.getName() + " to demand graph ui");
        // create a new slider for this consumer
        // each slider sits in a panel with an icon to the left and value readout to the right
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.setOpaque(false);
        JLabel newReadout = new JLabel("", c.getIcon(), JLabel.CENTER);
        JSlider newSlider = new JSlider();
        newSlider.setOrientation(JSlider.VERTICAL);
        newSlider.setPreferredSize(new Dimension(30,30));
        newSlider.setPaintTrack(false);
        newSlider.setForeground(Color.BLACK);
        newSlider.setMinorTickSpacing(10);
        newSlider.setPaintTicks(true);
        newSlider.setMaximum(100);
        newSlider.setMinimum(1);
        newSlider.setValue(50);
        newSlider.setOpaque(false);
        newSlider.addChangeListener(this);
        sliderPanel.add(newReadout, BorderLayout.LINE_START);
        sliderPanel.add(newSlider, BorderLayout.LINE_END);

        DemandGraph newGraph = new DemandGraph(c);
        newGraph.setMaxValue(grid.getMaxOutput());
        sliderPanel.add(newGraph, BorderLayout.CENTER);

        readouts.add(newReadout);
        demands.add(newGraph);
        sliders.add(newSlider);
        contents.add(sliderPanel);
        c.addMixer(newSlider); // connect the demand slider to the consumer
    }

    public void connect(ArrayList<Consumer> consumers) {
        // possibly redundant method for connecting a bunch of consumers at once
        System.out.println("Connecting " + consumers.size() + " consumers");
        for (Consumer c: consumers) {
            connect(c);
        }
    }

    public void update() {
        for(DemandGraph d: demands) {
            d.requestUpdate();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
//        for (int i=0; i<sliders.size(); i++) {
//            System.out.println(sliders.get(i).getValue()+"%");
//        }
    }
}
