package org.view;

import org.model.AppConfig;

import javax.swing.*;
import java.awt.*;

/**
 * VIEW: La finestra dell'applicazione.
 * Possiede il JFrame e il CardLayout root che passa da un pannello principale all'altro.
 */
public class MainFrame {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cardHolder;

    public MainFrame() {
        frame = new JFrame("Aeronix");
        frame.setSize(AppConfig.DEFAULT_WIDTH, AppConfig.DEFAULT_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        cardHolder = new JPanel();
        cardLayout = new CardLayout();
        cardHolder.setLayout(cardLayout);
        frame.add(cardHolder);
        frame.setVisible(true);
        frame.validate();
    }
    public void addPanel(JPanel panel, String name) {
        cardHolder.add(panel, name);
    }
    public void showPanel(String name) {
        cardLayout.show(cardHolder, name);
    }
    public CardLayout getCardLayout() {
        return cardLayout; }
    public JPanel getCardHolder() {
        return cardHolder; }
    public JFrame getFrame() {
        return frame;
    }
}
