package org.model;

import java.util.ArrayList;

/** MODEL: Descrive la direzione e l'intensità dell'illuminazione della scena. */
public class Lighting {
    public Vector3 lightDirection;
    public double lightIntensity;
    public double shadowIntensity;

    public Lighting(Vector3 direction, double lightIntensity, double shadowIntensity) {
        this.lightDirection = direction;
        this.lightIntensity = lightIntensity;
        this.shadowIntensity = shadowIntensity;
    }

    /** Recalculates lighting for every mesh in the list. */
    public void update(ArrayList<Mesh> meshes) {
        for (Mesh m : meshes) {
            m.calculateLighting(this);
        }
    }
}
