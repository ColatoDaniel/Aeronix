package org.controller;

import org.model.*;

import java.awt.event.*;
//Controller: traduce eventi da tastiera in flag booleani di input su AirplaneModel
public class AirplaneInputController implements KeyListener {
    private final AirplaneModel airplane;
    private GameSettings settings;
    public AirplaneInputController(AirplaneModel airplane, GameSettings settings) {
        this.airplane = airplane;
        this.settings = settings;
    }
    // Viene chiamato ogni cambio di impostazioni per renderle subito effettive
    public void updateSettings(GameSettings settings) {
        this.settings = settings;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (!airplane.isPhysicsEnabled()) return;
        int k = e.getKeyCode();
        if (k == settings.throttleUp) airplane.inputThrottleUp = true;
        else if (k == settings.throttleDown) airplane.inputThrottleDown = true;
        else if (k == settings.pitchUp) airplane.inputPitchUp = true;
        else if (k == settings.pitchDown) airplane.inputPitchDown = true;
        else if (k == settings.rollLeft) airplane.inputRollLeft = true;
        else if (k == settings.rollRight) airplane.inputRollRight = true;
        else if (k == settings.yawLeft) airplane.inputYawLeft = true;
        else if (k == settings.yawRight) airplane.inputYawRight = true;
        else if (k == settings.brakes) airplane.inputBrakes = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (!airplane.isPhysicsEnabled()) return;
        int k = e.getKeyCode();
        if (k == settings.throttleUp) airplane.inputThrottleUp = false;
        else if (k == settings.throttleDown) airplane.inputThrottleDown = false;
        else if (k == settings.pitchUp) airplane.inputPitchUp = false;
        else if (k == settings.pitchDown) airplane.inputPitchDown = false;
        else if (k == settings.rollLeft) airplane.inputRollLeft = false;
        else if (k == settings.rollRight) airplane.inputRollRight = false;
        else if (k == settings.yawLeft) airplane.inputYawLeft = false;
        else if (k == settings.yawRight) airplane.inputYawRight = false;
        else if (k == settings.brakes) airplane.inputBrakes = false;
    }
    @Override public void keyTyped(KeyEvent e) {}
}
