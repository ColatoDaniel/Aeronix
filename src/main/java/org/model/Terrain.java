package org.model;

import java.awt.Color;

/** MODEL: Una mesh del terreno generata proceduralmente utilizzando il simplex noise. */
public class Terrain extends Mesh {
    public Terrain(double height, double waterLevel, double snowLevel, double gridInterval, int gridLength, int gridWidth, double frequency, double amplitude, Color waterColor, Color mountainColor, Color snowColor) {
        super(true, false);
        Vector3[][] verts = new Vector3[gridWidth][gridLength];
        for (int x = 0; x < gridWidth; x++)
            for (int z = 0; z < gridLength; z++)
                verts[x][z] = new Vector3(
                        (x - gridWidth/2.0) * gridInterval,
                        Math.max(height + Math.pow(SimplexNoise.noise(x*frequency, z*frequency)*amplitude, 3), height+waterLevel),
                        (z - gridLength/2.0) * gridInterval + 300000);

        for (int i = 0; i < gridWidth-1; i++) {
            for (int j = 0; j < gridLength-1; j++) {
                Triangle t = new Triangle(this, verts[i][j], verts[i][j+1], verts[i+1][j], mountainColor);
                if (t.getCenter().y <= height+waterLevel)       t.setBaseColor(waterColor);
                else if (t.getCenter().y >= height+snowLevel)   t.setBaseColor(snowColor);
                getTriangles().add(t);
            }
            for (int j = 0; j < gridLength-1; j++) {
                Triangle t = new Triangle(this, verts[i+1][j], verts[i][j+1], verts[i+1][j+1], mountainColor);
                if (t.getCenter().y <= height+waterLevel)       t.setBaseColor(waterColor);
                else if (t.getCenter().y >= height+snowLevel)   t.setBaseColor(snowColor);
                getTriangles().add(t);
            }
        }
    }
}
