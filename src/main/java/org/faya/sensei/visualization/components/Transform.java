package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Quaternion;
import org.faya.sensei.mathematics.Vector3;

public class Transform extends Component {

    public Vector3 position = new Vector3(0.0);

    public Quaternion rotation = new Quaternion(0.0, 0.0, 0.0, 1.0);

    public float scale = 1.0f;

    public final Transform parent;

    public Transform() {
        this(null);
    }

    public Transform(final Transform parent) {
        this.parent = parent;
    }
}
