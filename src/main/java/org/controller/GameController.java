package org.controller;

import org.model.*;
import org.view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Loop in gioco, gestisce la fisica, input, pause, e navigazione tra i pannelli
public class GameController implements KeyListener, MouseListener, FocusListener {
    // collaboratori
    private final FlightSimulatorApp app;
    private final GameView view;
    // model objects
    private final AirplaneModel airplane;
    private final Camera camera;
    private final Lighting lighting;
    private final Terrain ground;
    private final GameObject runway1;
    // sotto-controller
    private final AirplaneInputController inputController;
    private CameraController cameraController;
    // stati
    private boolean paused = false;
    private boolean renderInitialised = false;
    private Timer physicsTimer;
    public GameController(FlightSimulatorApp app, GameView view) {
        this.app = app;
        this.view = view;
        // costruzione model
        lighting = new Lighting(new Vector3(1, -1, 1), 30, 150);
        camera = new Camera(new Vector3(0, 0, -250), 100000, 100, 60);
        airplane = new AirplaneModel();
        ground = new Terrain(-500, -200, 6000, 1000, 800, 300, 0.02, 30, new Color(1, 75, 148), new Color(15, 99, 0), new Color(200, 200, 210));
        runway1  = new GameObject("runway1", new Mesh("runway.obj", Color.DARK_GRAY, new Vector3(0,-0.09,37),   new EulerAngle(), 300, false, false), new Transform(new Vector3()));
        inputController = new AirplaneInputController(airplane, app.getUser().getSettings());
        // collega i listener del view a questo controller
        view.setSettingsListener(e -> openSettings());
        view.setControlsListener(e -> openControls());
        view.setResetListener(e -> resetAirplane());
        // collega al view
        view.addKeyListener(inputController);
        view.addKeyListener(this);
        view.addMouseListener(this);
        view.addFocusListener(this);
        // timer della fisica
        physicsTimer = new Timer(30, e -> physicsTick());
    }
    // Chiamato quando il GameView diventa visibile per la prima volta
    public void onFirstPaint() {
        if (renderInitialised) return;
        renderInitialised = true;
        User user = app.getUser();
        camera.setFov(user.getSettings().fov);
        camera.setSensitivity(user.getSettings().sensitivity);
        int rw = AppConfig.DEFAULT_WIDTH  - AppConfig.DEFAULT_WIDTH  / 4;
        int rh = AppConfig.DEFAULT_HEIGHT;
        RenderingPanel rp = view.initRenderingPanel(rw, rh);
        cameraController = new CameraController(camera, airplane, 1000, camera.getSensitivity(), this);
        cameraController.attachTo(view);
        rp.setCamera(camera);
        rp.setLighting(lighting);
        rp.setFog(camera.getFarClipDistance() * 0.6, camera.getFarClipDistance(), new Color(91, 215, 252));
        rp.addMesh(airplane.getMesh());
        rp.addMesh(ground);
        rp.addMesh(runway1.getMesh());
        rp.setFPSlimit(150);
        rp.start();
        airplane.startPhysics();
        physicsTimer.start();
    }
    // Applica impostazioni aggiornate
    public void applySettings() {
        camera.setFov(app.getUser().getSettings().fov);
        camera.setSensitivity(app.getUser().getSettings().sensitivity);
        if (cameraController != null) cameraController.setSensitivity(camera.getSensitivity());
    }
    public boolean isPaused(){
        return paused;
    }
    public AirplaneInputController getAirplaneInput(){
        return inputController;
    }
    // pausa / riprendi
    public void pause() {
        if (paused) return;
        paused = true;
        airplane.stopPhysics();
        physicsTimer.stop();
    }
    public void unpause() {
        if (!paused) return;
        paused = false;
        airplane.startPhysics();
        physicsTimer.start();
        applySettings();
        view.repaint();
    }
    public void togglePause() {
        if (paused) unpause();
        else pause();
    }
    // Tick delle fisiche
    private void physicsTick() {
        airplane.tick();
        if (cameraController != null) cameraController.updatePosition();
        view.updateTelemetry(airplane.getThrottle(), airplane.getSpeed(), airplane.getAltitude(), airplane.getVerticalClimb(), airplane.getOrientation().y, airplane.getOrientation().z);
        view.repaint();
    }
    // Azioni
    private void resetAirplane() {
        pause();
        airplane.resetPhysics();
        unpause();
    }
    private void openSettings() {
        pause();
        app.showSettings(GameView.name());
    }
    private void openControls() {
        pause();
        app.showControls(GameView.name());
    }
    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) togglePause();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    //MouseListener
    @Override public void mousePressed(MouseEvent e) {
        view.requestFocusInWindow();
    }
    @Override public void mouseClicked(MouseEvent e){}
    @Override public void mouseReleased(MouseEvent e){}
    @Override public void mouseEntered(MouseEvent e){}
    @Override public void mouseExited(MouseEvent e){}
    // FocusListener
    @Override
    public void focusGained(FocusEvent e) {
        unpause();
        applySettings();
        view.repaint();
    }
    @Override
    public void focusLost(FocusEvent e) {
        pause();
        view.repaint();
    }
}
