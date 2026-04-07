package org.model;

/** MODEL: Matrice 3x3 immutabile utilizzata per rotazioni e trasformazioni. */
public class Matrix3x3 {
    public final double R1C1, R1C2, R1C3, R2C1, R2C2, R2C3, R3C1, R3C2, R3C3;

    public Matrix3x3(Vector3 c1, Vector3 c2, Vector3 c3) {
        R1C1=c1.x;
        R1C2=c2.x;
        R1C3=c3.x;
        R2C1=c1.y;
        R2C2=c2.y;
        R2C3=c3.y;
        R3C1=c1.z;
        R3C2=c2.z;
        R3C3=c3.z;
    }

    public Matrix3x3(double r1c1,double r1c2,double r1c3,double r2c1,double r2c2,double r2c3,double r3c1,double r3c2,double r3c3) {
        R1C1=r1c1;
        R1C2=r1c2;
        R1C3=r1c3;
        R2C1=r2c1;
        R2C2=r2c2;
        R2C3=r2c3;
        R3C1=r3c1;
        R3C2=r3c2;
        R3C3=r3c3;
    }

    public double getDeterminant() {
        return R1C1*(R2C2*R3C3-R2C3*R3C2)-R1C2*(R2C1*R3C3-R2C3*R3C1)+R1C3*(R2C1*R3C2-R2C2*R3C1);
    }

    public Matrix3x3 getAdjugateMatrix() {
        return new Matrix3x3(R2C2*R3C3-R2C3*R3C2, -(R1C2*R3C3-R1C3*R3C2),  R1C2*R2C3-R1C3*R2C2, -(R2C1*R3C3-R2C3*R3C1), R1C1*R3C3-R1C3*R3C1, -(R1C1*R2C3-R1C3*R2C1), R2C1*R3C2-R2C2*R3C1, -(R1C1*R3C2-R1C2*R3C1),  R1C1*R2C2-R1C2*R2C1
        );
    }

    public Matrix3x3 getInverse() {
        return multiply(getAdjugateMatrix(), 1.0 / getDeterminant());
    }

    // ── static helpers ──────────────────────────────────────────────────────────

    public static Matrix3x3 multiply(Matrix3x3 a, Matrix3x3 b) {
        return new Matrix3x3(
                a.R1C1*b.R1C1+a.R1C2*b.R2C1+a.R1C3*b.R3C1, a.R1C1*b.R1C2+a.R1C2*b.R2C2+a.R1C3*b.R3C2, a.R1C1*b.R1C3+a.R1C2*b.R2C3+a.R1C3*b.R3C3,
                a.R2C1*b.R1C1+a.R2C2*b.R2C1+a.R2C3*b.R3C1, a.R2C1*b.R1C2+a.R2C2*b.R2C2+a.R2C3*b.R3C2, a.R2C1*b.R1C3+a.R2C2*b.R2C3+a.R2C3*b.R3C3,
                a.R3C1*b.R1C1+a.R3C2*b.R2C1+a.R3C3*b.R3C1, a.R3C1*b.R1C2+a.R3C2*b.R2C2+a.R3C3*b.R3C2, a.R3C1*b.R1C3+a.R3C2*b.R2C3+a.R3C3*b.R3C3
        );
    }

    public static Matrix3x3 multiply(Matrix3x3 m, double s) {
        return new Matrix3x3(m.R1C1*s,m.R1C2*s,m.R1C3*s, m.R2C1*s,m.R2C2*s,m.R2C3*s, m.R3C1*s,m.R3C2*s,m.R3C3*s);
    }

    public static Matrix3x3 rotationMatrixAxisX(double a) {
        double c=Math.cos(a), s=Math.sin(a);
        return new Matrix3x3(1,0,0, 0,c,-s, 0,s,c);
    }

    public static Matrix3x3 rotationMatrixAxisY(double a) {
        double c=Math.cos(a), s=Math.sin(a);
        return new Matrix3x3(c,0,s, 0,1,0, -s,0,c);
    }

    public static Matrix3x3 rotationMatrixAxisZ(double a) {
        double c=Math.cos(a), s=Math.sin(a);
        return new Matrix3x3(c,-s,0, s,c,0, 0,0,1);
    }

    public static Matrix3x3 axisAngleMatrix(Vector3 axis, double angle) {
        axis = axis.getNormalized();
        double cos=Math.cos(angle), cos1=1-cos, sin=Math.sin(angle);
        return new Matrix3x3(
                cos+axis.x*axis.x*cos1, axis.x*axis.y*cos1-axis.z*sin, axis.x*axis.z*cos1+axis.y*sin,
                axis.y*axis.x*cos1+axis.z*sin, cos+axis.y*axis.y*cos1, axis.y*axis.z*cos1-axis.x*sin,
                axis.z*axis.x*cos1-axis.y*sin, axis.z*axis.y*cos1+axis.x*sin, cos+axis.z*axis.z*cos1
        );
    }

    public static Matrix3x3 eulerRotation(EulerAngle angle) {
        double ca=Math.cos(angle.z), sa=Math.sin(angle.z);
        double cb=Math.cos(angle.y), sb=Math.sin(angle.y);
        double cy=Math.cos(angle.x), sy=Math.sin(angle.x);
        return new Matrix3x3(
                ca*cb,       ca*sb*sy-sa*cy, ca*sb*cy+sa*sy,
                sa*cb,       sa*sb*sy+ca*cy, sa*sb*cy-ca*sy,
                -sb,          cb*sy,           cb*cy
        );
    }
}
