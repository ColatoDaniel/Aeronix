package org.view;

import org.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * VIEW: Il pannello di gioco.
 * Visualizza l'area di rendering 3D a sinistra e il quadro strumenti a destra.
 * Riceve la telemetria tramite updateTelemetry() - non tocca mai direttamente lo stato del model.
 */
public class GameView extends JPanel {
    // ── telemetry cache (written by controller, read by SidePanel) ────────────
    private double throttle, speed, altitude, verticalClimb;
    private double orientationY, orientationZ; // compass, roll
    // ── sub-panels ────────────────────────────────────────────────────────────
    private SidePanel sidePanel;
    private RenderingPanel renderingPanel;
    private Image flightDials;
    // ── listeners provided by controller ─────────────────────────────────────
    private ActionListener onSettings;
    private ActionListener onControls;
    private ActionListener onReset;
    public GameView() {
        setLayout(new BorderLayout());
        flightDials = Utils.makeImage(new File(AppConfig.RESOURCES_FOLDER, "AirplaneDials.png"));
        sidePanel = new SidePanel();
        add(sidePanel, BorderLayout.EAST);
    }
    /** Called once the panel has been laid out so we know its size. */
    public RenderingPanel initRenderingPanel(int width, int height) {
        renderingPanel = new RenderingPanel(width, height);
        add(renderingPanel, BorderLayout.CENTER);
        validate();
        return renderingPanel;
    }
    /** Controller wires action listeners before the panel is shown. */
    public void setSettingsListener(ActionListener l) {
        onSettings = l; sidePanel.rebuildButtons();
    }
    public void setControlsListener(ActionListener l) {
        onControls = l; sidePanel.rebuildButtons();
    }
    public void setResetListener(ActionListener l) {
        onReset = l;
        sidePanel.rebuildButtons();
    }
    /** Controller updates telemetry every physics tick. */
    public void updateTelemetry(double throttle, double speed, double altitude, double verticalClimb, double orientationY, double orientationZ) {
        this.throttle = throttle;
        this.speed = speed;
        this.altitude = altitude;
        this.verticalClimb = verticalClimb;
        this.orientationY = orientationY;
        this.orientationZ = orientationZ;
    }
    public boolean isRenderingInitialised() {
        return renderingPanel != null;
    }
    public static String name() {
        return "GamePanel";
    }
    // ── inner SidePanel ───────────────────────────────────────────────────────
    private class SidePanel extends JPanel {
        private Button settingsBtn, controlsBtn, resetBtn;
        SidePanel() {
            setBackground(new Color(90, 94, 97));
            setPreferredSize(new Dimension(AppConfig.DEFAULT_WIDTH / 4, 100));
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            rebuildButtons();
        }
        void rebuildButtons() {
            removeAll();
            resetBtn = new Button("Reset", 20, 150, 50);
            settingsBtn= new Button("Settings", 20, 150, 50);
            controlsBtn = new Button("Controls", 20, 150, 50);
            if (onReset!= null) resetBtn.addActionListener(onReset);
            if (onSettings!= null) settingsBtn.addActionListener(onSettings);
            if (onControls!= null) controlsBtn.addActionListener(onControls);
            add(resetBtn);
            add(settingsBtn);
            add(controlsBtn);
            revalidate();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (flightDials == null) return;
            Graphics2D g2d = (Graphics2D) g;
            int W = AppConfig.DEFAULT_WIDTH / 4;

            // draw dial images
            g.drawImage(flightDials, W/2, 400-W/2, W, 400, 250, 15,  480, 245, this);
            g.drawImage(flightDials, 0, 400, W, W+400, 0, 245, 480, 728, this);
            g2d.setStroke(new BasicStroke(4));
            g2d.setColor(Color.WHITE);
            drawThrottle(g2d, 50, 230, throttle);
            drawDialNeedle(g2d, 252, 315, 60, orientationY);
            drawDialNeedle(g2d, 85,  487, 60, speed / 75);
            drawTurnCoordinator(g2d, 85, 659, orientationZ);
            drawDialNeedle(g2d, 255, 489, 55, altitude / 300 / Math.PI);
            drawDialNeedle(g2d, 255, 489, 30, altitude / 3000 / Math.PI);
            drawDialNeedle(g2d, 255, 659, 60, verticalClimb / Math.PI / 10 - Math.PI / 2);
        }

        private void drawDialNeedle(Graphics2D g, int cx, int cy, int len, double rot) {
            rot -= Math.PI / 2;
            g.drawLine(cx, cy, cx + (int)(Math.cos(rot)*len), cy + (int)(Math.sin(rot)*len));
        }

        private void drawTurnCoordinator(Graphics2D g, int cx, int cy, double rot) {
            int ex = (int)(Math.cos(-rot)*50), ey = (int)(Math.sin(-rot)*50);
            g.drawLine(cx, cy, cx+ex, cy+ey);
            g.drawLine(cx, cy, cx-ex, cy-ey);
        }

        private void drawThrottle(Graphics2D g, int x, int y, double amt) {
            double tAmt = 1 - amt;
            g.setFont(new Font(AppConfig.FONTSTYLE, Font.PLAIN, 30));
            g.drawString("Throttle", x + 20, y);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(x, y, 10, 70);
            g.setColor(Color.WHITE);
            g.drawRect(x, (int)(y + 70*tAmt) - 10, 10, 10);
        }
    }
}
