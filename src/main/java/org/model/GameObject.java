package org.model;

/** MODEL: Un oggetto 3D denominato costituito da una mesh e una trasformazione. */
public class GameObject {
    private Mesh mesh;
    private Transform transform;
    private String name;

    public GameObject(String name, Mesh mesh, Transform transform) {
        this.name = name;
        this.mesh = mesh;
        transform.setGameObject(this);
        this.transform = transform;
    }

    public Mesh getMesh() {
        return mesh;
    }
    public Transform getTransform() {
        return transform;
    }
    public String getName() {
        return name;
    }
    public void setTransform(Transform t) {
        transform = t;
        t.setGameObject(this);
    }
}
