package org.view;

import org.model.*;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * VIEW: Esegue proiezioni e rasterizzazioni da 3D a 2D.
 * Legge dagli oggetti modello (Camera, Mesh, Triangle) ma non modifica nulla.
 */
public class RenderingPanel extends JPanel implements Runnable {
    private ArrayList<Mesh> meshes = new ArrayList<>();
    private ArrayList<Triangle> triangles = new ArrayList<>();
    private ArrayList<Triangle2D> drawQueue = new ArrayList<>();
    private BufferedImage renderImage;
    private int[] blankPixels;
    private Color backgroundColor = new Color(91, 215, 252);
    private Camera  camera;
    private Lighting lightingObject;
    // fog
    private double fogStart, fogFull;
    private Color fogColor;
    private boolean fogEnabled = false;
    // thread
    private Thread  renderThread;
    private boolean threadRunning;
    private int fpsLimit = -1;
    private long lastFrameTime = System.nanoTime();
    public RenderingPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        renderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        blankPixels = new int[width * height];
        Arrays.fill(blankPixels, toIntRGB(backgroundColor));
    }
    @Override
    public void paintComponent(Graphics g) {
        if (!meshes.isEmpty() && camera != null) {
            computeTriangles();
            Collections.sort(drawQueue);
            drawBufferedImage();
            g.drawImage(renderImage, 0, 0, this);
        }
        g.drawString("fps: " + (int)(1_000_000_000L / (System.nanoTime() - lastFrameTime)), 30, 30);
        lastFrameTime = System.nanoTime();
    }

    public void setFPSlimit(int limit) {
        fpsLimit = Math.max(0, limit);
    }
    public void setCamera(Camera cam) {
        camera = cam;
    }
    public void setLighting(Lighting l) {
        lightingObject = l;
        if (l != null)
            l.update(meshes);
    }

    public void setFog(double start, double full, Color color) {
        fogStart = start;
        fogFull = full;
        fogColor = color;
        fogEnabled = true;
    }

    public void enableFog() {
        fogEnabled = true;
    }
    public void disableFog() {
        fogEnabled = false;
    }
    public void addMesh(Mesh mesh) {
        if (mesh == null) return;
        meshes.add(mesh);
        if (lightingObject != null) lightingObject.update(meshes);
        triangles.addAll(mesh.getTriangles());
    }

    public void start() {
        validate(); revalidate();
        if (renderThread == null) {
            threadRunning = true;
            renderThread = new Thread(this, "Rendering");
            renderThread.start();
        }
    }
    public void stopThread() {
        threadRunning = false;
        if (renderThread != null) { renderThread.interrupt(); renderThread = null; }
    }

    @Override
    public void run() {
        while (threadRunning) {
            if (fpsLimit > 0) {
                try { Thread.sleep(1000 / fpsLimit); } catch (InterruptedException ignored) {}
            }
            repaint();
        }
    }

    // ── rendering pipeline ────────────────────────────────────────────────────

    private void computeTriangles() {
        double renderPlaneWidth = camera.getRenderPlaneWidth();
        double pixelsPerUnit = getWidth() / renderPlaneWidth;
        Vector3 camPos = camera.getPosition();
        Vector3 camDir = camera.getDirectionVector();
        Vector3 camCenter = Vector3.add(Vector3.multiply(camDir, camera.getRenderPlaneDistance()), camPos);
        Plane renderPlane = new Plane(camCenter, camDir);
        Matrix3x3 pointRot = Matrix3x3.multiply(Matrix3x3.rotationMatrixAxisX(camera.getVorientation() * 0.017453292519943295), Matrix3x3.rotationMatrixAxisY(-camera.getHorientation() * 0.017453292519943295));
        drawQueue.clear();
        for (Triangle tri : triangles)
            projectTriangle(tri, camPos, camDir, camCenter, renderPlane, pointRot, pixelsPerUnit, renderPlaneWidth);
    }
    private void projectTriangle(Triangle tri, Vector3 camPos, Vector3 camDir, Vector3 camCenter, Plane renderPlane, Matrix3x3 pointRot, double ppu, double rpw) {
        Vector3 center = tri.getCenter();
        double dist = Vector3.subtract(center, camPos).getMagnitude();
        boolean facingCam  = Vector3.dotProduct(Vector3.subtract(center, camPos), camDir) > 0;
        boolean inRange = dist < camera.getFarClipDistance();
        boolean frontFacing = Vector3.dotProduct(tri.getPlane().normal, Vector3.subtract(center, camPos)) < 0;
        if (!facingCam || !inRange || !frontFacing) return;
        // near-clip check
        Plane nearClip = new Plane(Vector3.add(camPos, Vector3.multiply(camDir, camera.getNearClipDistance())), camDir);
        if (Vector3.dotProduct(nearClip.normal, Vector3.subtract(tri.vertex1, nearClip.pointOnPlane)) < 0) return;
        double hw = rpw / 2 * 1.2;
        double hh = rpw * ((double) getHeight() / getWidth()) / 2 * 1.2;

        Point p1 = project(tri.vertex1, camPos, renderPlane, camCenter, pointRot, ppu);
        Point p2 = project(tri.vertex2, camPos, renderPlane, camCenter, pointRot, ppu);
        Point p3 = project(tri.vertex3, camPos, renderPlane, camCenter, pointRot, ppu);

        Vector3 rp1 = rotated(tri.vertex1, camPos, renderPlane, camCenter, pointRot);
        Vector3 rp2 = rotated(tri.vertex2, camPos, renderPlane, camCenter, pointRot);
        Vector3 rp3 = rotated(tri.vertex3, camPos, renderPlane, camCenter, pointRot);

        boolean visible = (Math.abs(rp1.x)<hw && Math.abs(rp1.y)<hh) || (Math.abs(rp2.x)<hw && Math.abs(rp2.y)<hh) || (Math.abs(rp3.x)<hw && Math.abs(rp3.y)<hh);
        if (!visible) return;

        Color color = resolveColor(tri, dist);
        drawQueue.add(new Triangle2D(p1, p2, p3, color, dist));
    }

    private Vector3 rotated(Vector3 v, Vector3 camPos, Plane rp, Vector3 camCenter, Matrix3x3 rot) {
        Vector3 proj = Vector3.getIntersectionPoint(Vector3.subtract(v, camPos), camPos, rp);
        return Vector3.applyMatrix(rot, Vector3.subtract(proj, camCenter));
    }

    private Point project(Vector3 v, Vector3 camPos, Plane rp, Vector3 camCenter, Matrix3x3 rot, double ppu) {
        Vector3 r = rotated(v, camPos, rp, camCenter, rot);
        return new Point((int)(getWidth()/2 + r.x*ppu), (int)(getHeight()/2 - r.y*ppu));
    }

    private Color resolveColor(Triangle tri, double dist) {
        if (tri.getMesh() != null && tri.getMesh().isShaded()) {
            Color lit = tri.getColorWithLighting();
            if (lit == null) return Color.MAGENTA;
            if (fogEnabled && dist > fogStart) {
                if (dist >= fogFull) return fogColor;
                double t = (dist - fogStart) / (fogFull - fogStart);
                int r = clamp(lit.getRed()   + (int)((fogColor.getRed()   - lit.getRed())   * t * t));
                int g = clamp(lit.getGreen() + (int)((fogColor.getGreen() - lit.getGreen()) * t * t));
                int b = clamp(lit.getBlue()  + (int)((fogColor.getBlue()  - lit.getBlue())  * t * t));
                return new Color(r, g, b);
            }
            return lit;
        }
        return tri.getBaseColor() != null ? tri.getBaseColor() : Color.MAGENTA;
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }

    private void drawBufferedImage() {
        renderImage.getRaster().setDataElements(0, 0, renderImage.getWidth(), renderImage.getHeight(), blankPixels);
        for (Triangle2D t : drawQueue) paintTriangle(t.p1, t.p2, t.p3, t.color);
    }

    private void paintTriangle(Point p1, Point p2, Point p3, Color color) {
        int rgb = toIntRGB(color);
        // bubble-sort by Y
        Point tmp;
        if (p1.y > p2.y) { tmp=p1; p1=p2; p2=tmp; }
        if (p2.y > p3.y) { tmp=p2; p2=p3; p3=tmp; }
        if (p1.y > p2.y) { tmp=p1; p1=p2; p2=tmp; }
        if (p2.y > p3.y) { tmp=p2; p2=p3; p3=tmp; }
        int W = renderImage.getWidth(), H = renderImage.getHeight();
        // top half
        if (p2.y-p1.y != 0 && p3.y-p1.y != 0) {
            for (int y = p1.y; y < p2.y && y < H; y++) {
                if (y < 0) continue;
                int e1 = (p2.x-p1.x == 0) ? clampX(p1.x, W)
                        : clampX((int)((y-p1.y)/((double)(p2.y-p1.y)/(p2.x-p1.x))+p1.x), W);
                int e2 = (p3.x-p1.x == 0) ? clampX(p1.x, W)
                        : clampX((int)((y-p1.y)/((double)(p3.y-p1.y)/(p3.x-p1.x))+p1.x), W);
                hLine(Math.min(e1,e2), Math.max(e1,e2), y, rgb);
            }
        }
        // bottom half
        if (p3.y-p2.y != 0 && p3.y-p1.y != 0) {
            for (int y = p2.y; y < p3.y && y < H; y++) {
                if (y < 0) continue;
                int e1 = (p3.x-p2.x == 0) ? clampX(p2.x, W)
                        : clampX((int)((y-p3.y)/((double)(p3.y-p2.y)/(p3.x-p2.x))+p3.x), W);
                int e2 = (p3.x-p1.x == 0) ? clampX(p3.x, W)
                        : clampX((int)((y-p3.y)/((double)(p3.y-p1.y)/(p3.x-p1.x))+p3.x), W);
                hLine(Math.min(e1,e2), Math.max(e1,e2), y, rgb);
            }
        }
    }

    private int clampX(int x, int w) {
        return Math.max(0, Math.min(w, x));
    }
    private void hLine(int x1, int x2, int y, int rgb) {
        int len = Math.abs(x2 - x1);
        if (len == 0) return;
        int[] pixels = new int[len];
        Arrays.fill(pixels, rgb);
        renderImage.getRaster().setDataElements(x1, y, len, 1, pixels);
    }
    private int toIntRGB(Color c) {
        return 65536*c.getRed() + 256*c.getGreen() + c.getBlue();
    }
    // ── inner record ─────────────────────────────────────────────────────────
    private static class Triangle2D implements Comparable<Triangle2D> {
        Point p1, p2, p3;
        Color color;
        double dist;
        Triangle2D(Point p1, Point p2, Point p3, Color color, double dist) {
            this.p1=p1; this.p2=p2; this.p3=p3; this.color=color; this.dist=dist;
        }
        public int compareTo(Triangle2D o) { return Double.compare(o.dist, dist); }
    }
}
