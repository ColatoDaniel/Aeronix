package org.model;

import java.awt.Color;
import java.awt.Image;
import java.io.File;

/**
 * MODEL: Le costanti dell'applicazione centrale sostituiscono le public statics che erano
 * sparse nella classe FlightSimulator originale.
 */
public class AppConfig {
    public static final File RESOURCES_FOLDER = new File("src/main/resources");
    public static final String FONTSTYLE = "Times";
    public static final Color  THEME_COLOR = new Color(50, 110, 184);
    public static final int DEFAULT_WIDTH = 1366;
    public static final int DEFAULT_HEIGHT = 768;

    /** Lazily initialised so tests that never need the image don't pay the I/O cost. */
    public static final Image BACKGROUND_IMAGE = Utils.makeImage(new File(RESOURCES_FOLDER, "airplaneBackground.jpeg"));
}
