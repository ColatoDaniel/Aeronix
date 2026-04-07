package org.model;

/** MODEL: Rappresenta un piano nello spazio 3D definito da un punto e da un vettore normale. */
public class Plane {
    public static final Plane XZ_PLANE = new Plane(new Vector3(), new Vector3(0, 1, 0));
    public static final Plane XY_PLANE = new Plane(new Vector3(), new Vector3(0, 0, 1));
    public static final Plane ZY_PLANE = new Plane(new Vector3(), new Vector3(1, 0, 0));

    public Vector3 normal;
    public Vector3 pointOnPlane;

    public Plane(Vector3 point, Vector3 normal) {
        this.normal = normal;
        this.pointOnPlane = point;
    }

    public Plane(Vector3 p1, Vector3 p2, Vector3 p3) {
        normal = Vector3.crossProduct(Vector3.subtract(p1, p2), Vector3.subtract(p2, p3)).getNormalized();
        pointOnPlane = p1;
    }

    public double getDValue() {
        return -normal.x * pointOnPlane.x - normal.y * pointOnPlane.y - normal.z * pointOnPlane.z;
    }
}
