package org.controller;

import org.model.*;

import javax.swing.JPanel;
import java.awt.event.*;
//Gestisce l'input del mouse per muovere la camera intorno a un GameObject
public class CameraController implements MouseMotionListener, MouseWheelListener, MouseListener {
    private final Camera camera;
    private final GameObject focusObj;
    private double distance;
    private double sensitivity;
    private final int MAX_DIST = 4000;
    private final int MIN_DIST = 500;
    private final int MAX_ANGLE = 80;
    private Vector3 directionUnit;
    private Vector3 difference;
    private int prevX, prevY;
    private GameController gameController; // controlla lo stato
    public CameraController(Camera camera, GameObject focusObj, double startDist, double sensitivity, GameController gameController) {
        this.camera = camera;
        this.focusObj = focusObj;
        this.distance = startDist;
        this.sensitivity = sensitivity;
        this.gameController = gameController;
        directionUnit = new Vector3(0, 0, -1);
        difference = Vector3.multiply(directionUnit, startDist);
        updatePosition();
    }
    public void setSensitivity(double s) {
        sensitivity = s;
    }
    // Registra questo controller come listener del pannello di gioco
    public void attachTo(JPanel panel) {
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.addMouseWheelListener(this);
    }
    public void updatePosition() {
        if (gameController.isPaused()) return;
        Vector3 pos = Vector3.add(focusObj.getTransform().getPosition(), difference);
        camera.setPosition(pos);
        camera.lookAt(focusObj.getTransform().getPosition());
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (gameController.isPaused()) return;
        distance  = Math.max(MIN_DIST, Math.min(distance + e.getWheelRotation() * 30, MAX_DIST));
        difference = Vector3.multiply(directionUnit, distance);
        updatePosition();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if (gameController.isPaused()) return;
        directionUnit = Vector3.rotateAroundYaxis(directionUnit, (e.getX() - prevX) / (2000.0 / sensitivity));
        double vOri = camera.getVorientation();
        double dy   = (e.getY() - prevY) / (2000.0 / sensitivity);
        if ((vOri > -MAX_ANGLE && dy > 0) || (vOri < MAX_ANGLE && dy < 0)) {
            double hRad = camera.getHorientation() * 0.017453292519943295;
            directionUnit = Vector3.rotateAroundYaxis(Vector3.rotateAroundXaxis(Vector3.rotateAroundYaxis(directionUnit, -hRad), dy), hRad);
        }
        difference = Vector3.multiply(directionUnit, distance);
        updatePosition();
        prevX = e.getX();
        prevY = e.getY();
    }

    @Override public void mousePressed(MouseEvent e)  {
        prevX = e.getX(); prevY = e.getY();
    }
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

