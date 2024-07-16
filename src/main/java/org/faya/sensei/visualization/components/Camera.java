package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.mathematics.Quaternion;
import org.faya.sensei.mathematics.Vector3;
import org.faya.sensei.mathematics.Vector4;

public class Camera extends Component {

    private static final float fov = (float) Math.toRadians(60.0f);
    private static final float zFar = 1000.f;
    private static final float zNear = 0.01f;

    private Matrix4x4 projectionMatrix;
    private Transform transform;
    private float width;
    private float height;

    /** @inheritDoc */
    @Override
    public void start() {
        transform = (Transform) entity.getComponent(Transform.class);
    }

    /**
     * Get the camera view matrix.
     *
     * @return The view matrix.
     */
    public Matrix4x4 getViewMatrix() {
        final Vector3 forward = Quaternion.multiply(transform.getWorldRotation(), new Vector3(0.0f, 0.0f, -1.0f));
        final Vector3 target = Vector3.add(transform.getWorldPosition(), forward);

        final Matrix4x4 lookMatrix = Matrix4x4.lookAt(transform.getWorldPosition(), target, new Vector3(0.0f, 1.0f, 0.0f));

        final Matrix4x4 inverseRotation = Matrix4x4.transpose(
                new Matrix4x4(
                        lookMatrix.c0(),
                        lookMatrix.c1(),
                        lookMatrix.c2(),
                        new Vector4(0.0f, 0.0f, 0.0f, 1.0f)
                )
        );

        final Vector4 translation = Matrix4x4.multiply(inverseRotation, Vector4.multiply(lookMatrix.c3(), new Vector4(-1.0f)));

        return new Matrix4x4(
                inverseRotation.c0(),
                inverseRotation.c1(),
                inverseRotation.c2(),
                new Vector4(translation.x(), translation.y(), translation.z(), 1.0f)
        );
    }

    /**
     * Get the camera projection matrix.
     *
     * @return The projection matrix.
     */
    public Matrix4x4 getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Set the camera width.
     *
     * @param width The width of the screen.
     */
    public void setWidth(final float width) {
        this.width = width;
        updateProjectionMatrix();
    }

    /**
     * Set the camera height.
     *
     * @param height The height of the screen.
     */
    public void setHeight(final float height) {
        this.height = height;
        updateProjectionMatrix();
    }

    /**
     * Recalculate the projection matrix.
     */
    private void updateProjectionMatrix() {
        projectionMatrix = Matrix4x4.perspective(fov, width / height, zNear, zFar);
    }
}
