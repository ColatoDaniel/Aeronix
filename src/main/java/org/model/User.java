package org.model;

import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;

/**
 * MODEL: Rappresenta un account utente con impostazioni definite memorizzate in un file.
 */
public class User {
    public static final File ACCOUNT_DATA_FILE = new File("src/main/resources/userData.txt");

    private double milesFlown;
    private boolean completedTraining;
    private String username;
    private String password;
    private GameSettings userSettings;

    /** Creates a new user and writes it to the data file. */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.milesFlown = 0;
        this.completedTraining = false;
        this.userSettings = new GameSettings();
        PrintWriter writer = Utils.makeWriter(ACCOUNT_DATA_FILE, true);
        writer.append("\n--");
        writer.append("\nu " + username);
        writer.append("\np " + password);
        writer.append("\nmilesFlown " + milesFlown);
        writer.append("\ncompletedTraining " + completedTraining);
        writer.append("\nsettings " + userSettings);
        writer.close();
    }

    private User(String username, String password, boolean completedTraining, double milesFlown, GameSettings settings) {
        this.username = username;
        this.password = password;
        this.milesFlown = milesFlown;
        this.completedTraining = completedTraining;
        this.userSettings = settings;
    }

    /** Persists the current user state to the data file. */
    public void saveData() {
        Scanner reader = Utils.makeReader(ACCOUNT_DATA_FILE);
        PrintWriter writer = Utils.makeWriter(ACCOUNT_DATA_FILE, false);
        String line;
        while (reader.hasNextLine()) {
            line = reader.nextLine();
            writer.println(line);
            if (line.equals("u " + username)) {
                reader.nextLine(); writer.println("p " + password);
                reader.nextLine(); writer.println("milesFlown " + milesFlown);
                reader.nextLine(); writer.println("completedTraining " + completedTraining);
                reader.nextLine(); writer.println("settings " + userSettings);
                break;
            }
        }
        StringBuilder rest = new StringBuilder();
        while (reader.hasNextLine()) rest.append(reader.nextLine()).append("\n");
        writer.write(rest.toString());
        writer.close();
    }

    /** Loads a user from the data file by username. Returns null if not found. */
    public static User getUser(String username) {
        if (username == null) return null;
        Scanner reader = Utils.makeReader(ACCOUNT_DATA_FILE);
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.equals("u " + username)) {
                String pass = reader.nextLine().substring(2);
                double miles = Double.parseDouble(reader.nextLine().substring(11));
                boolean ct = Boolean.parseBoolean(reader.nextLine().substring(18));
                String[] s = Utils.split(reader.nextLine(), " ");
                GameSettings gs = new GameSettings(
                        Integer.parseInt(s[1]),  Integer.parseInt(s[2]),
                        Integer.parseInt(s[3]),  Integer.parseInt(s[4]),
                        Integer.parseInt(s[5]),  Integer.parseInt(s[6]),
                        Integer.parseInt(s[7]),  Integer.parseInt(s[8]),
                        Integer.parseInt(s[9]),  Double.parseDouble(s[10]),
                        Double.parseDouble(s[11]), Boolean.parseBoolean(s[12])
                );
                return new User(line.substring(2), pass, ct, miles, gs);
            }
        }
        return null;
    }

    // ── getters / setters ────────────────────────────────────────────────────────
    public String getUsername()                 { return username; }
    public String getPassword()                 { return password; }
    public GameSettings getSettings()           { return userSettings; }
    public boolean getCompletedTraining()       { return completedTraining; }
    public double getMilesFlown()               { return milesFlown; }
    public void setMilesFlown(double miles)     { milesFlown = miles; }
    public void setCompletedTraining(boolean v) { completedTraining = v; saveData(); }
}
