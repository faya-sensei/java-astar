package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.mathematics.Quaternion;
import org.faya.sensei.mathematics.Vector3;
import org.faya.sensei.visualization.InputSystem;

import static org.lwjgl.glfw.GLFW.*;

public class Camera extends Component {

    private final InputSystem inputSystem = InputSystem.getInstance();
    private final float lookSpeed = 0.05f;
    private final float moveSpeed = 1.0f;

    private Transform transform;
    private float lastX = -1.0f;
    private float lastY = -1.0f;

    @Override
    public void start() {
        transform = (Transform) entity.getComponent(Transform.class);
        transform.setLocalPosition(new Vector3(0.0f, 0.0f, 3.0f));
        transform.setLocalRotation(new Quaternion(0.0f, 0.0f, 0.0f, 1.0f));

        inputSystem.addEventListener(InputSystem.KeyEvent.class, this::handleKeyEvent);
        inputSystem.addEventListener(InputSystem.MouseEvent.class, this::handleMouseEvent);
    }

    /**
     * Callback when receive key event.
     *
     * @param event The key event info.
     */
    private void handleKeyEvent(InputSystem.KeyEvent event) {
        if (event.action() == GLFW_PRESS || event.action() == GLFW_RELEASE) {
            final boolean isPressed = event.action() == GLFW_PRESS;

            final Vector3 direction = switch (event.key()) {
                case GLFW_KEY_W -> new Vector3(0.0f, 0.0f, -1.0f);
                case GLFW_KEY_S -> new Vector3(0.0f, 0.0f, 1.0f);
                case GLFW_KEY_A -> new Vector3(-1.0f, 0.0f, 0.0f);
                case GLFW_KEY_D -> new Vector3(1.0f, 0.0f, 0.0f);
                case GLFW_KEY_LEFT_SHIFT -> new Vector3(0.0f, -1.0f, 0.0f);
                case GLFW_KEY_SPACE -> new Vector3(0.0f, 1.0f, 0.0f);
                default -> null;
            };

            if (direction != null && isPressed) move(direction);
        }
    }

    /**
     * Callback when receive mouse event.
     *
     * @param event The mouse event info.
     */
    private void handleMouseEvent(InputSystem.MouseEvent event) {
        float xoffset = lastX >= 0.0f && lastY >= 0.0f ? (float) event.xpos() - lastX : 0.0f;
        float yoffset = lastX >= 0.0f && lastY >= 0.0f ? lastY - (float) event.ypos() : 0.0f; // reversed since y-coordinates go from bottom to top
        lastX = (float) event.xpos();
        lastY = (float) event.ypos();

        xoffset *= lookSpeed;
        yoffset *= lookSpeed;

        rotate(xoffset, yoffset);
    }

    /**
     * Move the camera by direction vector under move speed.
     *
     * @param direction The direction vector in unit.
     */
    public void move(final Vector3 direction) {
        transform.setLocalPosition(Vector3.add(transform.getLocalPosition(), Vector3.multiply(direction, new Vector3(moveSpeed))));
    }

    /**
     * Rotate the camera by yaw and pitch.
     *
     * @param yawOffset   The yaw of the camera.
     * @param pitchOffset The pitch of the camera.
     */
    public void rotate(final float yawOffset, final float pitchOffset) {
        final Vector3 euler = Quaternion.toEuler(transform.getLocalRotation(), Quaternion.RotationOrder.ZYX);
        float newPitch = (float) Math.toRadians(pitchOffset) + euler.x();
        if (newPitch > Math.toRadians(89.0f)) newPitch = (float) Math.toRadians(89.0f);
        if (newPitch < Math.toRadians(-89.0f)) newPitch = (float) Math.toRadians(-89.0f);

        transform.setLocalRotation(Quaternion.fromEuler(new Vector3(newPitch, euler.y() + (float) Math.toRadians(yawOffset), 0.0f), Quaternion.RotationOrder.ZYX));
    }

    /**
     * Get the camera view matrix.
     *
     * @return The view matrix.
     */
    public Matrix4x4 getViewMatrix() {
        final Vector3 forward = Quaternion.multiply(transform.getWorldRotation(), new Vector3(0.0f, 0.0f, -1.0f));
        final Vector3 target = Vector3.add(transform.getWorldPosition(), forward);

        return Matrix4x4.lookAt(transform.getWorldPosition(), target, new Vector3(0.0f, 1.0f, 0.0f));
    }
}
