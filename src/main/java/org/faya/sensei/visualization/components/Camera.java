package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.mathematics.Quaternion;
import org.faya.sensei.mathematics.Vector3;
import org.faya.sensei.mathematics.Vector4;
import org.faya.sensei.visualization.InputSystem;

import java.util.function.BiConsumer;

import static org.lwjgl.glfw.GLFW.*;

public class Camera extends Component {

    private static final int MOVE_FORWARD = 1 << 0;
    private static final int MOVE_BACKWARD = 1 << 1;
    private static final int MOVE_LEFT = 1 << 2;
    private static final int MOVE_RIGHT = 1 << 3;
    private static final int MOVE_UP = 1 << 4;
    private static final int MOVE_DOWN = 1 << 5;

    private final InputSystem inputSystem = InputSystem.getInstance();
    private final float lookSpeed = 0.05f;
    private final float moveSpeed = 1.0f;

    private Transform transform;
    private float yaw = 90.0f, pitch = 0.0f;
    private float lastX = Float.NaN;
    private float lastY = Float.NaN;

    private int moveFlags;

    @Override
    public void start() {
        transform = (Transform) entity.getComponent(Transform.class);
        transform.setLocalPosition(new Vector3(0.0f, 0.0f, -3.0f));
        transform.setLocalRotation(new Quaternion(0.0f, 0.0f, 0.0f, 1.0f));

        inputSystem.addEventListener(InputSystem.KeyEvent.class, this::handleKeyEvent);
        inputSystem.addEventListener(InputSystem.MouseEvent.class, this::handleMouseEvent);
    }

    @Override
    public void update(float delta) {
        Vector3 moveDirection = new Vector3(0.0f);

        final Vector3 forward = Quaternion.multiply(transform.getLocalRotation(), new Vector3(0.0f, 0.0f, 1.0f));
        final Vector3 right = Quaternion.multiply(transform.getLocalRotation(), new Vector3(-1.0f, 0.0f, 0.0f));
        final Vector3 up = new Vector3(0.0f, 1.0f, 0.0f);

        if ((moveFlags & MOVE_FORWARD) != 0) moveDirection = Vector3.add(moveDirection, forward);
        if ((moveFlags & MOVE_BACKWARD) != 0) moveDirection = Vector3.subtract(moveDirection, forward);
        if ((moveFlags & MOVE_RIGHT) != 0) moveDirection = Vector3.add(moveDirection, right);
        if ((moveFlags & MOVE_LEFT) != 0) moveDirection = Vector3.subtract(moveDirection, right);
        if ((moveFlags & MOVE_UP) != 0) moveDirection = Vector3.add(moveDirection, up);
        if ((moveFlags & MOVE_DOWN) != 0) moveDirection = Vector3.subtract(moveDirection, up);

        if (!moveDirection.equals(new Vector3(0.0f))) {
            move(Vector3.multiply(Vector3.normalize(moveDirection), new Vector3(moveSpeed * delta)));
        }
    }

    /**
     * Callback when receive key event.
     *
     * @param event The key event info.
     */
    private void handleKeyEvent(InputSystem.KeyEvent event) {
        if (event.action() == GLFW_PRESS || event.action() == GLFW_RELEASE) {
            final boolean isPressed = event.action() == GLFW_PRESS;

            BiConsumer<Integer, Boolean> setMoveFlag = (flag, pressed) -> {
                if (pressed) moveFlags |= flag; else moveFlags &= ~flag;
            };

            switch (event.key()) {
                case GLFW_KEY_W -> setMoveFlag.accept(MOVE_FORWARD, isPressed);
                case GLFW_KEY_S -> setMoveFlag.accept(MOVE_BACKWARD, isPressed);
                case GLFW_KEY_A -> setMoveFlag.accept(MOVE_LEFT, isPressed);
                case GLFW_KEY_D -> setMoveFlag.accept(MOVE_RIGHT, isPressed);
                case GLFW_KEY_LEFT_SHIFT -> setMoveFlag.accept(MOVE_DOWN, isPressed);
                case GLFW_KEY_SPACE -> setMoveFlag.accept(MOVE_UP, isPressed);
            }
        }
    }

    /**
     * Callback when receive mouse event.
     *
     * @param event The mouse event info.
     */
    private void handleMouseEvent(InputSystem.MouseEvent event) {
        final float xoffset = !Float.isNaN(lastX) && !Float.isNaN(lastY) ? (float) event.xpos() - lastX : 0.0f;
        final float yoffset = !Float.isNaN(lastX) && !Float.isNaN(lastY) ? lastY - (float) event.ypos() : 0.0f; // reversed since y-coordinates go from bottom to top
        lastX = (float) event.xpos();
        lastY = (float) event.ypos();

        if (xoffset != 0.0f || yoffset != 0.0f) {
            rotate(-xoffset * lookSpeed, -yoffset * lookSpeed);
        }
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
        yaw += yawOffset;
        pitch += pitchOffset;

        pitch = Math.max(-89.0f, Math.min(89.0f, pitch));

        float yawRadians = (float) Math.toRadians(yaw);
        float pitchRadians = (float) Math.toRadians(pitch);

        final Quaternion yawRotation = Quaternion.rotateY(yawRadians);
        final Quaternion pitchRotation = Quaternion.rotateX(pitchRadians);

        transform.setLocalRotation(Quaternion.multiply(yawRotation, pitchRotation));
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
}
