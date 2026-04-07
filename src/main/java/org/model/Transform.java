package org.model;

/** MODEL: Memorizza e manipola la posizione e l'orientamento di un GameObject. */
public class Transform {
    private GameObject gameObject;
    private Vector3 position;
    private Vector3 forward;
    private Vector3 right;
    private Vector3 up;
    private EulerAngle rotation;

    public Transform(Vector3 position) {
        this.position = position;
        rotation = new EulerAngle();
        forward = new Vector3(0, 0, 1);
        right = new Vector3(1, 0, 0);
        up = new Vector3(0, 1, 0);
    }

    public void setPosition(Vector3 pos) {
        gameObject.getMesh().translate(Vector3.subtract(pos, position));
        position = pos;
    }

    public void move(Vector3 amount) {
        gameObject.getMesh().translate(amount);
        position = Vector3.add(position, amount);
    }

    public void setPitch(double angle) {
        Matrix3x3 m = Matrix3x3.axisAngleMatrix(right, angle - rotation.x);
        up = Vector3.applyMatrix(m, up);
        forward = Vector3.applyMatrix(m, forward);
        if (gameObject.getMesh() != null) gameObject.getMesh().rotate(m, position);
        rotation.x = angle;
    }

    public void setYaw(double angle) {
        Matrix3x3 m = Matrix3x3.axisAngleMatrix(up, angle - rotation.y);
        forward = Vector3.applyMatrix(m, forward);
        right = Vector3.applyMatrix(m, right);
        if (gameObject.getMesh() != null) gameObject.getMesh().rotate(m, position);
        rotation.y = angle;
    }

    public void setRoll(double angle) {
        Matrix3x3 m = Matrix3x3.axisAngleMatrix(forward, angle - rotation.z);
        up = Vector3.applyMatrix(m, up);
        right = Vector3.applyMatrix(m, right);
        if (gameObject.getMesh() != null) gameObject.getMesh().rotate(m, position);
        rotation.z = angle;
    }

    public Vector3 transformToWorld(Vector3 point) {
        return Vector3.applyMatrix(new Matrix3x3(right.getNormalized(), up.getNormalized(), forward.getNormalized()), point);
    }

    public Vector3 transformToLocal(Vector3 point) {
        return Vector3.applyMatrix(new Matrix3x3(right.getNormalized(), up.getNormalized(), forward.getNormalized()).getInverse(), point);
    }

    public Matrix3x3 toLocalMatrix() {
        return new Matrix3x3(right.getNormalized(), up.getNormalized(), forward.getNormalized()).getInverse();
    }

    public Matrix3x3 toWorldMatrix() {
        return new Matrix3x3(right.getNormalized(), up.getNormalized(), forward.getNormalized());
    }

    public Vector3 getForward() {
        return (forward = forward.getNormalized());
    }
    public Vector3 getUp() {
        return (up = up.getNormalized());
    }
    public Vector3 getRight() {
        return (right = right.getNormalized());
    }
    public Vector3 getPosition() {
        return position;
    }
    public EulerAngle getRotation() {
        return rotation;
    }
    public GameObject getGameObject(){
        return gameObject;
    }
    public void setGameObject(GameObject g) {
        gameObject = g;
    }
}
