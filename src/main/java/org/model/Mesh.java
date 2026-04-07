package org.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;

/** MODEL: Un insieme di triangoli che formano una mesh 3D. */
public class Mesh {
    private ArrayList<Triangle> triangles;
    private ArrayList<Vector3> vertices;
    private Color baseColor;
    private boolean shading;
    private Vector3 totalMovement;
    private Lighting lighting;
    private BufferedImage texture;
    private Raster textureRaster;
    private boolean backFaceCull = true;

    /** Textured mesh constructor. */
    public Mesh(String modelFile, String textureFile, Vector3 offset, EulerAngle rotation, double scale, boolean shaded, boolean backFaceCull) {
        texture = null;
        try {
            if (textureFile != null)
                texture = ImageIO.read(new File(AppConfig.RESOURCES_FOLDER, textureFile));
        } catch (IOException e) {
            System.err.println("Error loading texture: " + textureFile);
        }
        if (texture != null) textureRaster = texture.getData();
        init(shaded, backFaceCull);
        baseColor = Color.MAGENTA;
        if (modelFile.endsWith(".obj")) createTriangles(modelFile, offset, rotation, scale);
    }

    /** Solid-color mesh constructor. */
    public Mesh(String modelFile, Color color, Vector3 offset, EulerAngle rotation, double scale, boolean shaded, boolean backFaceCull) {
        texture = null; textureRaster = null;
        init(shaded, backFaceCull);
        baseColor = (color == null) ? Color.MAGENTA : color;
        if (modelFile.endsWith(".obj")) createTriangles(modelFile, offset, rotation, scale);
    }

    /** Protected constructor for subclasses (e.g. Terrain). */
    protected Mesh(boolean shaded, boolean backFaceCull) {
        init(shaded, backFaceCull);
    }

    private void init(boolean shaded, boolean bfc) {
        this.shading = shaded;
        this.backFaceCull = bfc;
        triangles = new ArrayList<>();
        vertices = new ArrayList<>();
        totalMovement = new Vector3();
    }

    public void rotate(Matrix3x3 rotationMatrix, Vector3 center) {
        for (Vector3 v : vertices) {
            v.set(Vector3.add(Vector3.applyMatrix(rotationMatrix, Vector3.subtract(v, center)), center));
        }
    }

    public void translate(Vector3 amount) {
        for (Vector3 v : vertices){
            v.set(Vector3.add(v, amount));
        }
        totalMovement = Vector3.add(totalMovement, amount);
    }

    public void resetPosition() {
        translate(Vector3.negate(totalMovement));
    }

    public void calculateLighting(Lighting l) {
        if (shading) {
            for (Triangle t : triangles) {
                t.calculateLightingColor(l);
            }
        }
        lighting = l;
    }

    public void refreshLighting() {
        if (shading && lighting != null)
            for (Triangle t : triangles) {
                t.calculateLightingColor(lighting);
            }
    }

    // ── getters ──────────────────────────────────────────────────────────────────
    public boolean isShaded() {
        return shading;
    }
    public boolean backFaceCulling() {
        return backFaceCull;
    }
    public Raster getTextureRaster() {
        return textureRaster;
    }
    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }
    public ArrayList<Vector3> getVertices() {
        return vertices;
    }

    // ── OBJ loader ────────────────────────────────────────────────────────────────
    private void createTriangles(String fileName, Vector3 offsetPos, EulerAngle offsetRot, double scale) {
        Matrix3x3 rotMat = Matrix3x3.eulerRotation(offsetRot);
        ArrayList<Vector2> texCoords = new ArrayList<>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(AppConfig.RESOURCES_FOLDER, fileName));
        } catch (FileNotFoundException e) {
            System.err.println("Model file not found: " + fileName);
            return;
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) continue;
            if (line.startsWith("v ")) {
                StringTokenizer t = new StringTokenizer(line);
                t.nextToken();
                Vector3 v = new Vector3(Double.parseDouble(t.nextToken()), Double.parseDouble(t.nextToken()), Double.parseDouble(t.nextToken()));
                v = Vector3.applyMatrix(rotMat, v);
                v = Vector3.add(offsetPos, v);
                v = Vector3.multiply(v, scale);
                vertices.add(v);
            } else if (texture != null && line.startsWith("vt ")) {
                StringTokenizer t = new StringTokenizer(line);
                t.nextToken();
                texCoords.add(new Vector2(Double.parseDouble(t.nextToken()), Double.parseDouble(t.nextToken())));
            } else if (line.startsWith("f ")) {
                StringTokenizer t = new StringTokenizer(line);
                t.nextToken();
                int len = t.countTokens();
                int[] ci = new int[len], ti = new int[len];
                for (int i = 0; i < len; i++) {
                    String[] parts = t.nextToken().split("/");
                    ci[i] = Integer.parseInt(parts[0]) - 1;
                    if (texture != null) ti[i] = Integer.parseInt(parts[1]) - 1;
                }
                for (int i = 0; i < ci.length - 2; i++) {
                    if (texture == null)
                        triangles.add(new Triangle(this, vertices.get(ci[0]), vertices.get(ci[i+1]), vertices.get(ci[i+2]), baseColor));
                    else
                        triangles.add(new Triangle(this, vertices.get(ci[0]), vertices.get(ci[i+1]), vertices.get(ci[i+2]),
                                texCoords.get(ti[0]), texCoords.get(ti[i+1]), texCoords.get(ti[i+2])));
                }
            }
        }
    }
}
