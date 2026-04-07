package org.model;

/** MODEL: Camera data — posizione, orientation, FOV, clipping distances. */
public class Camera {
    private double fov;
    private Vector3 position;
    private double hAngle;
    private double vAngle;
    private double renderPlaneDistance;
    private double farClipDistance;
    private double nearClipDistance;
    private double renderPlaneWidth;
    private double sensitivity;

    public Camera(Vector3 position, double farClip, double nearClip, double fov) {
        this.renderPlaneDistance = 10;
        this.hAngle = 0;
        this.vAngle = 0;
        this.position = position;
        this.farClipDistance  = farClip;
        this.nearClipDistance = nearClip;
        this.sensitivity = 10;
        setFov(fov);
    }

    public void lookAt(Vector3 pos) {
        hAngle = (pos.x - position.x < 0) ? -Math.toDegrees(Math.atan((pos.z-position.z)/(pos.x-position.x))) - 90 :  90 - Math.toDegrees(Math.atan((pos.z-position.z)/(pos.x-position.x)));
        vAngle = Math.toDegrees(Math.atan((pos.y-position.y) / Math.sqrt((pos.x-position.x)*(pos.x-position.x)+(pos.z-position.z)*(pos.z-position.z))));
        hAngle %= 360;
        vAngle %= 360;
    }

    private double calculateRenderPlaneWidth() {
        return Math.tan(fov * 0.017453292519943295 / 2) * renderPlaneDistance * 2;
    }

    // ── getters / setters ────────────────────────────────────────────────────────
    public void setFov(double fovIn) {
        fov = fovIn; renderPlaneWidth = calculateRenderPlaneWidth();
    }
    public void setSensitivity(double s) {
        sensitivity = s;
    }
    public double getSensitivity() {
        return sensitivity;
    }
    public double getFarClipDistance() {
        return farClipDistance;
    }
    public double getNearClipDistance() {
        return nearClipDistance;
    }
    public double getRenderPlaneDistance() {
        return renderPlaneDistance;
    }
    public double getRenderPlaneWidth() {
        return renderPlaneWidth;
    }
    public double getHorientation() {
        return hAngle;
    }
    public double getVorientation() {
        return vAngle;
    }
    public Vector3 getPosition() {
        return position;
    }
    public void setPosition(Vector3 pos) {
        position = pos;
    }
    public void setHAngle(double a) {
        hAngle = a;
    }
    public void setVAngle(double a) {
        vAngle = a;
    }
    public Vector3 getDirectionVector() {
        return Vector3.angleToVector(hAngle * 0.017453292519943295, vAngle * 0.017453292519943295);
    }
}
