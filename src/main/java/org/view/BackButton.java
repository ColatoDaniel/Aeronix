package org.view;

import org.model.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** VIEW: Un pulsante "Back" che commuta i pannelli CardLayout. */
public class BackButton extends JButton implements ActionListener {

    private CardLayout layout;
    private JPanel parentPanel;
    private String destination;

    public BackButton(CardLayout layout, JPanel parentPanel, String destination) {
        this.layout = layout;
        this.parentPanel = parentPanel;
        this.destination = destination;
        style();
    }

    public BackButton(CardLayout layout, JPanel parentPanel) {
        this(layout, parentPanel, null);
    }

    private void style() {
        setText("Back");
        setBackground(new Color(184, 71, 42));
        setFont(new Font(AppConfig.FONTSTYLE, Font.PLAIN, 30));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setPreferredSize(new Dimension(150, 50));
        addActionListener(this);
    }

    public void setDestination(String name) {
        destination = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (destination != null)
            layout.show(parentPanel, destination);
        else
            layout.previous(parentPanel);
    }
}
