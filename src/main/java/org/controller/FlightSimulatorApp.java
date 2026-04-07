package org.controller;

import org.model.*;
import org.view.*;

import javax.swing.SwingUtilities;
import java.awt.Graphics;


//Applicazione
/* Crea tutti i Modelli, View, Controller e li collega insieme
 * Attraverso l'MVC la gestione è centralizzata così che gli altri pannelli
 * non hanno bisogno di riferimenti ad altri
 */

public class FlightSimulatorApp {
    // model
    private final User user;
    // view
    private final MainFrame mainFrame;
    private final GameView gameView;
    private final SettingsView settingsView;
    private final ControlsView controlsView;
    // control
    private final GameController gameController;
    private final SettingsController settingsController;
    private final ControlsController controlsController;
    public FlightSimulatorApp() {
        // 1) Carica l'utente
        user = User.getUser("user");

        // 2) Crea la finestra principale
        mainFrame = new MainFrame();

        // 3) Build dei pannelli
        gameView = buildGameView();
        settingsView = new SettingsView(mainFrame.getCardLayout(), mainFrame.getCardHolder());
        controlsView = new ControlsView(mainFrame.getCardLayout(), mainFrame.getCardHolder());

        // 4) Registra i pannelli nella finestra
        mainFrame.addPanel(gameView, GameView.name());
        mainFrame.addPanel(settingsView, SettingsView.name());
        mainFrame.addPanel(controlsView, ControlsView.name());

        // 5) Creazione dei controller
        gameController = new GameController(this, gameView);
        settingsController = new SettingsController(this, settingsView);
        // ControlsController needs AirplaneInputController from GameController
        controlsController = new ControlsController(this, controlsView, gameController.getAirplaneInput());

        // 6) Mostra il pannello di gioco
        mainFrame.showPanel(GameView.name());
    }
    private GameView buildGameView() {
        return new GameView() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!isRenderingInitialised()) {
                    gameController.onFirstPaint();
                }
                requestFocusInWindow();
            }
        };
    }

    //Navigazione, chiamata dai controller
    public void showGame() {
        mainFrame.showPanel(GameView.name());
    }
    public void showSettings(String previousPanel) {
        settingsView.setBackDestination(previousPanel);
        mainFrame.showPanel(SettingsView.name());
    }
    public void showControls(String previousPanel) {
        controlsView.setBackDestination(previousPanel);
        mainFrame.showPanel(ControlsView.name());
    }

    //Accessori per i controller
    public User getUser(){
        return user;
    }
    public MainFrame getMainFrame(){
        return mainFrame;
    }

    //entry point
    public static void main(String[] args){
        SwingUtilities.invokeLater(FlightSimulatorApp::new);
    }
}