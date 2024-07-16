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

    /**
     * Gets the local position of this {@code Transform}.
     *
     * @return The local position as a {@code Vector3}.
     */
    public Vector3 getLocalPosition() {
        return localPosition;
    }

    /**
     * Sets the local position of this {@code Transform}.
     *
     * @param localPosition The local position to set as a {@code Vector3}.
     */
    public void setLocalPosition(final Vector3 localPosition) {
        this.localPosition = localPosition;
    }

    /**
     * Gets the local rotation of this {@code Transform}.
     *
     * @return The local rotation as a {@code Quaternion}.
     */
    public Quaternion getLocalRotation() {
        return localRotation;
    }

    /**
     * Sets the local rotation of this {@code Transform}.
     *
     * @param localRotation The local rotation to set as a {@code Quaternion}.
     */
    public void setLocalRotation(final Quaternion localRotation) {
        this.localRotation = localRotation;
    }

    /**
     * Gets the local scale of this {@code Transform}.
     *
     * @return The local scale as a {@code Vector3}.
     */
    public Vector3 getLocalScale() {
        return localScale;
    }

    /**
     * Sets the local scale of this {@code Transform}.
     *
     * @param localScale The local scale to set as a {@code Vector3}.
     */
    public void setLocalScale(final Vector3 localScale) {
        this.localScale = localScale;
    }

    /**
     * Gets the parent {@code Transform} of this {@code Transform}.
     *
     * @return The parent {@code Transform}, or {@code null}.
     */
    public Transform getParent() {
        return parent;
    }

    /**
     * Sets the parent {@code Transform} of this {@code Transform}.
     *
     * @param parent The new parent {@code Transform} or {@code null} to set.
     */
    public void setParent(final Transform parent) {
        if (this.parent == parent) return;
        if (this.parent != null) this.parent.removeChild(this);
        this.parent = parent;
        if (this.parent != null) this.parent.addChild(this);
    }

    /**
     * Gets the list of child {@code Transform}s attached to this
     * {@code Transform}.
     *
     * @return The list of child transforms as a {@code List<Transform>}.
     */
    public List<Transform> getChildren() {
        return children;
    }

    // Function

    /**
     * Gets the world position of this {@code Transform}.
     *
     * @return The world position as a {@code Vector3}.
     */
    public Vector3 getWorldPosition() {
        return parent != null
                ? Vector3.add(parent.getWorldPosition(), localPosition)
                : localPosition;
    }

    /**
     * Gets the world rotation of this {@code Transform}.
     *
     * @return The world rotation as a {@code Quaternion}.
     */
    public Quaternion getWorldRotation() {
        return parent != null
                ? Quaternion.multiply(parent.getWorldRotation(), localRotation)
                : localRotation;
    }

    /**
     * Gets the world scale of this {@code Transform}.
     *
     * @return The world scale as a {@code Vector3}.
     */
    public Vector3 getWorldScale() {
        return parent != null
                ? Vector3.multiply(parent.getWorldScale(), localScale)
                : localScale;
    }

    /**
     * Adds a {@code Transform} as a child of this {@code Transform}.
     *
     * @param child The {@code Transform} to be added as a child.
     */
    private void addChild(final Transform child) {
        if (!children.contains(child)) {
            children.add(child);
            child.parent = this;
        }
    }

    /**
     * Removes a {@code Transform} from the children of this {@code Transform}.
     *
     * @param child The {@code Transform} to be removed from the child list.
     */
    private void removeChild(final Transform child) {
        if (children.remove(child)) {
            child.parent = null;
        }
    }
}
