package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Quaternion;
import org.faya.sensei.mathematics.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Transform extends Component {

    private Vector3 localPosition = new Vector3(0.0f);
    private Quaternion localRotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
    private Vector3 localScale = new Vector3(1.0f);

    private final List<Transform> children = new ArrayList<>();
    private Transform parent = null;

    public Transform(final Transform parent) {
        if (parent != null) parent.addChild(this);
        this.parent = parent;
    }

    // Getter and setter

    public Vector3 getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(final Vector3 localPosition) {
        this.localPosition = localPosition;
    }

    public Quaternion getLocalRotation() {
        return localRotation;
    }

    public void setLocalRotation(final Quaternion localRotation) {
        this.localRotation = localRotation;
    }

    public Vector3 getLocalScale() {
        return localScale;
    }

    public void setLocalScale(final Vector3 localScale) {
        this.localScale = localScale;
    }

    public Transform getParent() {
        return parent;
    }

    public void setParent(final Transform parent) {
        if (this.parent == parent) return;
        if (this.parent != null) this.parent.removeChild(this);
        this.parent = parent;
        if (this.parent != null) this.parent.addChild(this);
    }

    public List<Transform> getChildren() {
        return children;
    }

    // Function

    public Vector3 getWorldPosition() {
        return parent != null
                ? Vector3.add(parent.getWorldPosition(), localPosition)
                : localPosition;
    }

    public Quaternion getWorldRotation() {
        return parent != null
                ? Quaternion.multiply(parent.getWorldRotation(), localRotation)
                : localRotation;
    }

    public Vector3 getWorldScale() {
        return parent != null
                ? Vector3.multiply(parent.getWorldScale(), localScale)
                : localScale;
    }

    private void addChild(final Transform child) {
        if (!children.contains(child)) {
            children.add(child);
            child.parent = this;
        }
    }

    private void removeChild(final Transform child) {
        if (children.remove(child)) {
            child.parent = null;
        }
    }
}
