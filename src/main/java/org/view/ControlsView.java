package org.view;

import org.model.AppConfig;
import org.model.GameSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * VIEW: Pannello di rimappatura dei controlli.
 * Legge le combinazioni di tasti correnti da GameSettings; restituisce nuovi valori tramite getter.
 * Il controller chiama i callback applyChanges() / restoreDefaults().
 */
public class ControlsView extends JPanel implements FocusListener {
    private BackButton backButton;
    private ControlSetting throttleUp, throttleDown;
    private ControlSetting pitchUp, pitchDown;
    private ControlSetting rollLeft, rollRight;
    private ControlSetting yawLeft, yawRight;
    private ControlSetting brakes;
    // provided by controller
    private ActionListener onApply;
    private ActionListener onRestoreDefaults;
    public ControlsView(CardLayout layout, JPanel cardHolder) {
        setOpaque(false);
        setLayout(new BorderLayout());
        addFocusListener(this);
        // top bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        top.setOpaque(false);
        backButton = new BackButton(layout, cardHolder);
        JLabel title = new JLabel("Controls");
        title.setFont(new Font(AppConfig.FONTSTYLE, Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        top.add(backButton);
        top.add(title);
        add(top, BorderLayout.NORTH);
        // content
        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 30));
        content.setOpaque(false);
        throttleUp = new ControlSetting("Throttle Up");
        throttleDown = new ControlSetting("Throttle Down");
        pitchUp = new ControlSetting("Pitch Up");
        pitchDown = new ControlSetting("Pitch Down");
        rollLeft = new ControlSetting("Roll Left");
        rollRight = new ControlSetting("Roll Right");
        yawLeft = new ControlSetting("Yaw Left");
        yawRight = new ControlSetting("Yaw Right");
        brakes = new ControlSetting("Brakes");
        for (ControlSetting cs : new ControlSetting[]{throttleUp, throttleDown, pitchUp, pitchDown, rollLeft, rollRight, yawLeft, yawRight, brakes})
            content.add(cs);
        Button restoreBtn = new Button("Restore Defaults", 30);
        restoreBtn.addActionListener(e -> { if (onRestoreDefaults != null) onRestoreDefaults.actionPerformed(e); });
        Button applyBtn = new Button("Apply Changes", 30);
        applyBtn.addActionListener(e -> { if (onApply != null) onApply.actionPerformed(e); });
        content.add(restoreBtn);
        content.add(applyBtn);
        add(content, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(AppConfig.BACKGROUND_IMAGE, 0, 0, getWidth(), (int)(((double)getWidth() / AppConfig.BACKGROUND_IMAGE.getWidth(this)) * AppConfig.BACKGROUND_IMAGE.getHeight(this)), this);
        requestFocusInWindow();
    }
    // ── controller interface ──────────────────────────────────────────────────
    public void setApplyListener(ActionListener l) {
        onApply = l;
    }
    public void setRestoreDefaultsListener(ActionListener l) {
        onRestoreDefaults = l;
    }
    public void setBackDestination(String name) {
        backButton.setDestination(name);
    }
    /** Populate fields from current settings. */
    public void loadSettings(GameSettings s) {
        throttleUp.setKey(s.throttleUp);
        throttleDown.setKey(s.throttleDown);
        pitchUp.setKey(s.pitchUp);
        pitchDown.setKey(s.pitchDown);
        rollLeft.setKey(s.rollLeft);
        rollRight.setKey(s.rollRight);
        yawLeft.setKey(s.yawLeft);
        yawRight.setKey(s.yawRight);
        brakes.setKey(s.brakes);
    }
    /** Write field values into settings object. */
    public void saveSettings(GameSettings s) {
        s.throttleUp = throttleUp.getKeyValue();
        s.throttleDown = throttleDown.getKeyValue();
        s.pitchUp = pitchUp.getKeyValue();
        s.pitchDown = pitchDown.getKeyValue();
        s.rollLeft = rollLeft.getKeyValue();
        s.rollRight = rollRight.getKeyValue();
        s.yawLeft = yawLeft.getKeyValue();
        s.yawRight = yawRight.getKeyValue();
        s.brakes = brakes.getKeyValue();
    }

    public static String name() {
        return "ControlsPanel";
    }
    @Override public void focusGained(FocusEvent e) {}
    @Override public void focusLost(FocusEvent e)   {}

    // ── inner ControlSetting ──────────────────────────────────────────────────

    private static class ControlSetting extends JPanel {
        private JTextField keyDisplay;
        ControlSetting(String label) {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font(AppConfig.FONTSTYLE, Font.BOLD, 30));
            lbl.setForeground(Color.WHITE);
            keyDisplay = new JTextField(4);
            keyDisplay.setFont(new Font(AppConfig.FONTSTYLE, Font.BOLD, 30));
            add(keyDisplay); add(lbl);
        }
        void setKey(int value) {
            switch (value) {
                case KeyEvent.VK_UP:
                    keyDisplay.setText("up");
                    return;
                case KeyEvent.VK_DOWN:
                    keyDisplay.setText("down");
                    return;
                case KeyEvent.VK_LEFT:
                    keyDisplay.setText("left");
                    return;
                case KeyEvent.VK_RIGHT:
                    keyDisplay.setText("right");
                    return;
                case KeyEvent.VK_SPACE: keyDisplay.setText("space");
                return;
                default:
                    keyDisplay.setText("" + (char)(value + 32));
            }
        }

        int getKeyValue() {
            switch (keyDisplay.getText().toLowerCase()) {
                case "up":
                    return KeyEvent.VK_UP;
                case "down":
                    return KeyEvent.VK_DOWN;
                case "left":
                    return KeyEvent.VK_LEFT;
                case "right":
                    return KeyEvent.VK_RIGHT;
                case "space":
                    return KeyEvent.VK_SPACE;
                default:
                    return keyDisplay.getText().toLowerCase().charAt(0) - 32;
            }
        }
    }
}
