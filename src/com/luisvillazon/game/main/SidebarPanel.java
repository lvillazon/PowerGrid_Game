package com.luisvillazon.game.main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidebarPanel extends JPanel implements ActionListener {
    // used to for each "instrument panel" on the game UI
    // each one represents a power station or the grid itself

    private int GUIWidth, GUIHeight;
    private JLabel screw;
    private ImageIcon panelScrewIcon;
    private JPanel bevelledArea;
    protected JPanel contents;
    protected JLabel name;
    private Border raisedBevel;
    private Border loweredBevel;
    private Border compoundBevel;

    public SidebarPanel(String name) {
        System.out.println("SEQUENCE: SidebarPanel constructor name=" + name);

        setBackground(Sidebar.PANEL_COLOR);
        setLayout(new BorderLayout(0, 0));
        bevelledArea = new JPanel();
        bevelledArea.setBackground(Sidebar.PANEL_COLOR);
        bevelledArea.setLayout(new BorderLayout(5,5));
        raisedBevel = BorderFactory.createRaisedBevelBorder();
        loweredBevel = BorderFactory.createLoweredBevelBorder();
        compoundBevel = BorderFactory.createCompoundBorder(loweredBevel, raisedBevel);
        bevelledArea.setBorder(compoundBevel);

        // set up a pristine panel for child classes to add to
        contents = new JPanel();
        contents.setLayout(new BorderLayout(0,0));
        contents.setOpaque(false);
        bevelledArea.add(contents, BorderLayout.CENTER);

        panelScrewIcon = new ImageIcon(Resources.panelScrewImage);
        JPanel topScrews = new JPanel(new BorderLayout(0,0));
        JPanel bottomScrews = new JPanel(new BorderLayout(0,0));
        topScrews.setBackground(Sidebar.PANEL_COLOR);
        bottomScrews.setBackground(Sidebar.PANEL_COLOR);
        JLabel topleftScrew = new JLabel(panelScrewIcon, JLabel.CENTER);
        JLabel topRightScrew = new JLabel(panelScrewIcon, JLabel.CENTER);
        JLabel bottomLeftScrew = new JLabel(panelScrewIcon, JLabel.CENTER);
        JLabel bottomRightScrew = new JLabel(panelScrewIcon, JLabel.CENTER);
        topScrews.add(topleftScrew, BorderLayout.LINE_START);
        topScrews.add(topRightScrew, BorderLayout.LINE_END);
        bottomScrews.add(bottomLeftScrew, BorderLayout.LINE_START);
        bottomScrews.add(bottomRightScrew, BorderLayout.LINE_END);
        bevelledArea.add(topScrews, BorderLayout.PAGE_START);
        bevelledArea.add(bottomScrews, BorderLayout.PAGE_END);

        // add a title for this panel between the top screws
        this.name = new JLabel(name, JLabel.CENTER);
        Font panelFont = new Font("Bahnschrift Light",Font.PLAIN, 18);
        this.name.setFont(panelFont);
        //this.name.setPreferredSize(new Dimension(20,0));
        /* proof-of-concept code to add an icon image to the panel title
        this.name.setIcon(new ImageIcon("src/Resources/icon.png"));
        try {
            System.out.println(new File("icon.png").getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        topScrews.add(this.name, BorderLayout.CENTER);

        // add the contents panel to the PowerStationUI panel using invisble spacers to create a border
        add(Box.createRigidArea(new Dimension(10, 5)), BorderLayout.PAGE_START);
        add(Box.createRigidArea(new Dimension(8, 10)), BorderLayout.LINE_START);
        add(Box.createRigidArea(new Dimension(8, 10)), BorderLayout.LINE_END);
        add(Box.createRigidArea(new Dimension(10, 5)), BorderLayout.PAGE_END);
        add(bevelledArea, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}