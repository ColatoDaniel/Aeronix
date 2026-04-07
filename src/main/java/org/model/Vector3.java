package org.model;

/** MODEL: Rappresenta un vettore o un punto 3D e fornisce utilità matematiche 3D statiche. */
public class Vector3 {
    public double x;
    public double y;
    public double z;

    public Vector3(double xIn, double yIn, double zIn) {
        x = xIn;
        y = yIn;
        z = zIn;
    }
    public Vector3(Vector3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }
    public Vector3() {
        x = 0;
        y = 0;
        z = 0;
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    public double getSqrMagnitude() {
        return x * x + y * y + z * z;
    }

    public Vector3 getNormalized() {
        if (getSqrMagnitude() != 1) {
            double mag = getMagnitude();
            return new Vector3(x / mag, y / mag, z / mag);
        }
        return this;
    }

    public Vector3 add(Vector3 v) {
        x += v.x; y += v.y; z += v.z;
        return this;
    }
    public Vector3 multiply(double m) {
        x *= m; y *= m; z *= m;
        return this;
    }
    public void set(Vector3 v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public String toString() {
        return String.format("[%.2f, %.2f, %.2f]", x, y, z);
    }

    // ── static helpers ──────────────────────────────────────────────────────────

    public static double dotProduct(Vector3 a, Vector3 b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }

    public static Vector3 crossProduct(Vector3 a, Vector3 b) {
        return new Vector3(a.y*b.z - a.z*b.y, a.z*b.x - a.x*b.z, a.x*b.y - a.y*b.x);
    }

    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(a.x+b.x, a.y+b.y, a.z+b.z);
    }
    public static Vector3 subtract(Vector3 a, Vector3 b) {
        return new Vector3(a.x-b.x, a.y-b.y, a.z-b.z);
    }
    public static Vector3 multiply(Vector3 v, double s) {
        return new Vector3(v.x*s, v.y*s, v.z*s);
    }
    public static Vector3 multiply(Vector3 a, Vector3 b) {
        return new Vector3(a.x*b.x, a.y*b.y, a.z*b.z);
    }
    public static Vector3 negate(Vector3 v) {
        return new Vector3(-v.x, -v.y, -v.z);
    }

    public static Vector3 angleToVector(double yaw, double pitch) {
        double cosPitch = Math.cos(pitch);
        return new Vector3(Math.sin(yaw)*cosPitch, Math.sin(pitch), Math.cos(yaw)*cosPitch);
    }

    public static Vector3 getIntersectionPoint(Vector3 dir, Vector3 point, Plane plane) {
        return add(point, multiply(dir, dotProduct(subtract(plane.pointOnPlane, point), plane.normal) / dotProduct(dir, plane.normal)));
    }

    public static double getAngleBetween(Vector3 a, Vector3 b) {
        return Math.acos(dotProduct(a, b) / (a.getMagnitude() * b.getMagnitude()));
    }

    public static double distanceToLine(Vector3 point, Vector3 p1, Vector3 p2) {
        return crossProduct(subtract(point, p1), subtract(p1, p2)).getMagnitude() / subtract(p2, p1).getMagnitude();
    }

    public static double distanceToPlane(Vector3 point, Plane plane) {
        Vector3 n = plane.normal;
        Vector3 p = plane.pointOnPlane;
        return Math.abs(n.x*point.x + n.y*point.y + n.z*point.z - n.x*p.x - n.y*p.y - n.z*p.z) / Math.sqrt(n.x*n.x + n.y*n.y + n.z*n.z);
    }

    public static Vector3 rotateAroundXaxis(Vector3 pt, double a) {
        double cos = Math.cos(a), sin = Math.sin(a);
        return new Vector3(dotProduct(new Vector3(1,0,0),pt), dotProduct(new Vector3(0,cos,-sin),pt), dotProduct(new Vector3(0,sin,cos),pt));
    }

    public static Vector3 rotateAroundYaxis(Vector3 pt, double a) {
        double cos = Math.cos(a), sin = Math.sin(a);
        return new Vector3(dotProduct(new Vector3(cos,0,sin),pt), dotProduct(new Vector3(0,1,0),pt), dotProduct(new Vector3(-sin,0,cos),pt));
    }

    public static Vector3 rotateAroundZaxis(Vector3 pt, double a) {
        double cos = Math.cos(a), sin = Math.sin(a);
        return new Vector3(dotProduct(new Vector3(cos,-sin,0),pt), dotProduct(new Vector3(sin,cos,0),pt), dotProduct(new Vector3(0,0,1),pt));
    }

    public static Vector3 projectToPlane(Vector3 v, Vector3 normal) {
        return subtract(v, multiply(normal, dotProduct(v, normal) / normal.getSqrMagnitude()));
    }

    public static Vector3 projectToVector(Vector3 a, Vector3 b) {
        return multiply(b, dotProduct(b, a) / b.getSqrMagnitude());
    }

    public static Vector3 lerp(Vector3 start, Vector3 end, double t) {
        return add(start, multiply(subtract(end, start), t));
    }

    public static Vector3 axisAngleRotation(Vector3 axis, double angle, Vector3 v) {
        axis = axis.getNormalized();
        double cos = Math.cos(angle);
        return add(add(multiply(v, cos), multiply(multiply(axis, dotProduct(v, axis)), 1-cos)), multiply(crossProduct(axis, v), Math.sin(angle)));
    }

    public static Vector3 applyMatrix(Matrix3x3 m, Vector3 v) {
        return new Vector3(v.x*m.R1C1 + v.y*m.R1C2 + v.z*m.R1C3, v.x*m.R2C1 + v.y*m.R2C2 + v.z*m.R2C3, v.x*m.R3C1 + v.y*m.R3C2 + v.z*m.R3C3
        );
    }
}
