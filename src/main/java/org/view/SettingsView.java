package org.view;

import org.model.AppConfig;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * VIEW: Settings panel — cursori per FOV e sensibilità della fotocamera.
 * Espone i metodi getValue in modo che il controller possa leggere le scelte dell'utente.
 */
public class SettingsView extends JPanel {
    private BackButton   backButton;
    private SliderSetting fovSlider;
    private SliderSetting sensitivitySlider;
    // provided by controller
    private ActionListener onControlsOpen;
    public SettingsView(CardLayout layout, JPanel cardHolder) {
        setOpaque(false);
        setLayout(new BorderLayout());
        // top bar
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        top.setOpaque(false);
        backButton = new BackButton(layout, cardHolder);
        JLabel title = new JLabel("Settings");
        title.setFont(new Font(AppConfig.FONTSTYLE, Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        top.add(backButton);
        top.add(title);
        add(top, BorderLayout.NORTH);
        // content
        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 30));
        content.setOpaque(false);
        fovSlider = new SliderSetting("FOV",         30, 120);
        sensitivitySlider = new SliderSetting("Sensitivity",  1,  30);
        Button controlsBtn = new Button("Controls", 50);
        controlsBtn.addActionListener(e -> { if (onControlsOpen != null) onControlsOpen.actionPerformed(e); });
        content.add(fovSlider);
        content.add(sensitivitySlider);
        content.add(controlsBtn);
        add(content, BorderLayout.CENTER);
    }
    @Override
    protected void paintComponent(Graphics g) {
        paintBg(g);
        requestFocusInWindow();
    }
    private void paintBg(Graphics g) {
        g.drawImage(AppConfig.BACKGROUND_IMAGE, 0, 0, getWidth(), (int)(((double)getWidth() / AppConfig.BACKGROUND_IMAGE.getWidth(this)) * AppConfig.BACKGROUND_IMAGE.getHeight(this)), this);
    }
    public void setControlsListener(ActionListener l) {
        onControlsOpen = l;
    }

    public void setBackDestination(String name) {
        backButton.setDestination(name);
    }
    public void setFov(double v) {
        fovSlider.setValue(v);
    }
    public void setSensitivity(double v) {
        sensitivitySlider.setValue(v);
    }
    public double getFov() {
        return fovSlider.getValue();
    }
    public double getSensitivity() {
        return sensitivitySlider.getValue();
    }

    public static String name() {
        return "SettingsPanel";
    }
    // ── inner SliderSetting ───────────────────────────────────────────────────
    private static class SliderSetting extends JPanel implements ChangeListener {
        private JTextField display;
        private JSlider slider;
        SliderSetting(String label, int min, int max) {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.LEFT, 50, 50));
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font(AppConfig.FONTSTYLE, Font.BOLD, 40));
            lbl.setForeground(Color.WHITE);
            display = new JTextField(5);
            display.setEditable(false);
            display.setFont(new Font(AppConfig.FONTSTYLE, Font.BOLD, 40));
            slider = new JSlider(min, max);
            slider.setOpaque(false);
            slider.addChangeListener(this);
            add(lbl); add(display); add(slider);
        }
        void setValue(double v) {
            slider.setValue((int)v);
            display.setText("" + v);
        }
        int getValue() {
            return slider.getValue();
        }
        @Override
        public void stateChanged(ChangeEvent e) {
            display.setText("" + slider.getValue());
        }
    }
}
