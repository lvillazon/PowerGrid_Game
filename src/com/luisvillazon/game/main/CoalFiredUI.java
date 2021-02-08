package com.luisvillazon.game.main;

import com.luisvillazon.game.model.PowerStation;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CoalFiredUI extends PowerStationUI {

    ArrayList<JToggleButton> powerButtons;
    private ButtonGroup powerButtonsGroup;
    private JPanel powerPanel;
    private final ImageIcon onIcon = new ImageIcon(Resources.buttonOnImage);
    private final ImageIcon offIcon = new ImageIcon(Resources.buttonOffImage);
    private int[] powerLevels = {20, 40, 60, 80, 100};

    public CoalFiredUI(PowerStation station, String name) {
        super(station, name);

        FlowLayout pbLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        powerPanel = new JPanel(pbLayout);
        powerPanel.setBackground(Sidebar.PANEL_COLOR);
        powerButtonsGroup = new ButtonGroup();
        powerButtons = new ArrayList<JToggleButton>();
        for (int i=0; i<5; i++) {
            JToggleButton pb = new JToggleButton();//Integer.toString(powerLevels[i]));
            pb.setIcon(offIcon);
            pb.setPressedIcon(onIcon);
            pb.setIconTextGap(-20);
            pb.setBackground(Sidebar.PANEL_COLOR);
            pb.setContentAreaFilled(false);
            pb.setFocusPainted(false);
            pb.setBorderPainted(false);
            //pb.setPressedIcon();
            pb.setMargin(new Insets(2,2,0,2));
            pb.addActionListener(this);
            powerButtons.add(pb);
            powerButtonsGroup.add(pb);
            powerPanel.add(pb);
        }
        contents.add(powerPanel);
        contents.setPreferredSize(new Dimension(0, 50));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // pressing any button starts up the power station.
        // once on, it cannot be turned off.
        station.setActive(true);

        // change image on the selected button
        for (int i=0; i<powerLevels.length; i++) {
            if (powerButtons.get(i).isSelected()) {
                powerButtons.get(i).setIcon(onIcon);
                station.setThrottle(powerLevels[i]);
            } else {
            powerButtons.get(i).setIcon(offIcon);
            }
        }
    }
}
