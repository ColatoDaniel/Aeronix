package org.controller;

import org.model.*;
import org.view.*;

import java.awt.event.*;
//Gestisce il remapping dei controlli
public class ControlsController implements FocusListener {
    private final FlightSimulatorApp app;
    private final ControlsView view;
    private final AirplaneInputController inputController; // diffode i cambiamenti di controlli
    public ControlsController(FlightSimulatorApp app, ControlsView view, AirplaneInputController inputController) {
        this.app = app;
        this.view = view;
        this.inputController = inputController;
        view.addFocusListener(this);
        view.setApplyListener(e -> applyChanges());
        view.setRestoreDefaultsListener(e -> restoreDefaults());
    }
    @Override
    public void focusGained(FocusEvent e) {
        view.loadSettings(app.getUser().getSettings());
    }
    @Override
    public void focusLost(FocusEvent e) {}
    private void applyChanges() {
        view.saveSettings(app.getUser().getSettings());
        app.getUser().saveData();
        inputController.updateSettings(app.getUser().getSettings());
    }
    private void restoreDefaults() {
        app.getUser().getSettings().restoreDefaults();
        view.loadSettings(app.getUser().getSettings());
    }
}