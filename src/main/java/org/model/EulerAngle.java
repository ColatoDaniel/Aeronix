package org.model;

/**
 * MODEL: Rappresenta una rotazione nello spazio 3D utilizzando gli angoli di Eulero.
 * Tutte le rotazioni sono intrinseche e applicate nell'ordine x-y-z.
 */
public class EulerAngle {
    public double y; // yaw
    public double x; // pitch
    public double z; // roll

    public EulerAngle() {
        x = 0;
        y = 0;
        z = 0;
    }
    public EulerAngle(double xIn, double yIn, double zIn) {
        x = xIn;
        y = yIn;
        z = zIn;
    }

    public String toString() {
        return String.format("[%.2f, %.2f, %.2f]", x, y, z);
    }

    public static EulerAngle subtract(EulerAngle a, EulerAngle b) {
        return new EulerAngle(a.x - b.x, a.y - b.y, a.z - b.z);
    }
}
