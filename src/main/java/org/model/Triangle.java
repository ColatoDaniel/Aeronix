package org.model;

import java.awt.Color;

/** MODEL: Un triangolo 3D definito da tre vertici, con texture e illuminazione opzionali. */
public class Triangle {
    public Vector3 vertex1, vertex2, vertex3;
    public Vector2 textureCoord1, textureCoord2, textureCoord3;
    private Color color;
    private Mesh parentMesh;
    private Color colorWithLighting;
    public Triangle(Mesh mesh, Vector3 v1, Vector3 v2, Vector3 v3) {
        vertex1=v1; vertex2=v2; vertex3=v3; color=Color.MAGENTA; parentMesh=mesh;
    }
    public Triangle(Mesh mesh, Vector3 v1, Vector3 v2, Vector3 v3, Color c) {
        vertex1=v1; vertex2=v2; vertex3=v3; color=c; parentMesh=mesh;
    }
    public Triangle(Mesh mesh, Vector3 v1, Vector3 v2, Vector3 v3, Vector2 t1, Vector2 t2, Vector2 t3) {
        vertex1=v1;
        vertex2=v2;
        vertex3=v3;
        textureCoord1=t1; textureCoord2=t2; textureCoord3=t3;
        parentMesh=mesh;
        color = calculateTextureColor();
    }
    public Plane getPlane() {
        return new Plane(vertex1, vertex2, vertex3);
    }
    public Vector3 getCenter() {
        return new Vector3((vertex1.x+vertex2.x+vertex3.x)/3,
                (vertex1.y+vertex2.y+vertex3.y)/3,
                (vertex1.z+vertex2.z+vertex3.z)/3);
    }
    public Mesh getMesh() {
        return parentMesh;
    }
    public Color getBaseColor() {
        return color;
    }
    public Color getColorWithLighting() {
        return colorWithLighting;
    }
    public void setBaseColor(Color c) {
        color = c;
    }

    public void calculateLightingColor(Lighting lighting) {
        double angle = Vector3.getAngleBetween(lighting.lightDirection, Vector3.crossProduct(Vector3.subtract(vertex1, vertex2), Vector3.subtract(vertex2, vertex3)));
        int brightness = 0, darkness = 0;
        if (angle > Math.PI/2) brightness = (int)(Math.abs(angle/Math.PI - 0.5) * (lighting.lightIntensity/100) * 255);
        if (angle < Math.PI/2) darkness  = (int)(Math.abs(angle/Math.PI - 0.5) * (lighting.shadowIntensity/100) * 255);
        int r = Math.max(0, Math.min(255, color.getRed()   + brightness - darkness));
        int g = Math.max(0, Math.min(255, color.getGreen() + brightness - darkness));
        int b = Math.max(0, Math.min(255, color.getBlue()  + brightness - darkness));
        colorWithLighting = new Color(r, g, b);
    }

    private Color calculateTextureColor() {
        double cx = (textureCoord1.x + textureCoord2.x + textureCoord3.x) / 3;
        double cy = (textureCoord1.y + textureCoord2.y + textureCoord3.y) / 3;
        int[] px = new int[4];
        px = parentMesh.getTextureRaster().getPixel((int)(cx * parentMesh.getTextureRaster().getWidth()), parentMesh.getTextureRaster().getHeight() - (int)(cy * parentMesh.getTextureRaster().getHeight()), px);
        return new Color(px[0], px[1], px[2]);
    }
}
