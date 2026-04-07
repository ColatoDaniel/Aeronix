package org.view;

import org.model.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** VIEW: JButton con impostazioni predefinite del tema. */
public class Button extends JButton {

    public Button(String text, int fontSize) {
        init(text, fontSize, (int)(0.7 * fontSize * text.length()), fontSize * 2);
    }

    public Button(String text, int fontSize, int width, int height) {
        init(text, fontSize, width, height);
    }

    private void init(String text, int fontSize, int width, int height) {
        setText(text);
        setFont(new Font(AppConfig.FONTSTYLE, Font.PLAIN, fontSize));
        setPreferredSize(new Dimension(width, height));
        setBackground(AppConfig.THEME_COLOR);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
    }
}
