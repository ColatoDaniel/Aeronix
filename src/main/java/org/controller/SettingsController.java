package org.controller;

import org.model.*;
import org.view.*;

import java.awt.event.*;
// Gestisce il pannello delle impostazioni, legge / scrive GameSettings attraverso SettingsView
public class SettingsController implements FocusListener {
    private final FlightSimulatorApp app;
    private final SettingsView view;
    public SettingsController(FlightSimulatorApp app, SettingsView view) {
        this.app = app;
        this.view = view;
        view.addFocusListener(this);
        // Tasto "Controls" nel menu impostazioni
        view.setControlsListener(e -> app.showControls(SettingsView.name()));
    }
    @Override
    public void focusGained(FocusEvent e) {
        // Carica i valori correnti delle impostazioni
        GameSettings s = app.getUser().getSettings();
        view.setFov(s.fov);
        view.setSensitivity(s.sensitivity);
    }
    @Override
    public void focusLost(FocusEvent e) {
        // Salva le impostazioni aggiornate
        GameSettings s = app.getUser().getSettings();
        s.fov = view.getFov();
        s.sensitivity = view.getSensitivity();
        app.getUser().saveData();
    }
}

