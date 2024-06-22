package org.faya.sensei.visualization.components;

import org.faya.sensei.mathematics.Quaternion;
import org.faya.sensei.mathematics.Vector3;
import org.faya.sensei.visualization.InputSystem;

import static org.lwjgl.glfw.GLFW.*;

public class Camera extends Component {

    private final double lookSpeed = 0.05;
    private final double moveSpeed = 1.0;

    private final Transform transform;

    private double lastX = -1.0;
    private double lastY = -1.0;

    public Camera() {
        transform = (Transform) engineObject.getComponent(Transform.class);

        transform.position = new Vector3(0.0, 0.0, 3.0);
        transform.rotation = new Quaternion(0.0, 0.0, 0.0, 1.0);
    }

    @Override
    public void start() {
        InputSystem inputSystem = InputSystem.getInstance();
        inputSystem.addEventListener(InputSystem.KeyEvent.class, this::handleKeyEvent);
        inputSystem.addEventListener(InputSystem.MouseEvent.class, this::handleMouseEvent);
    }

    private void handleKeyEvent(InputSystem.KeyEvent event) {
        if (event.action() == GLFW_PRESS || event.action() == GLFW_RELEASE) {
            final boolean isPressed = event.action() == GLFW_PRESS;

            final Vector3 direction = switch (event.key()) {
                case GLFW_KEY_W -> new Vector3(0.0, 0.0, -1.0);
                case GLFW_KEY_S -> new Vector3(0.0, 0.0, 1.0);
                case GLFW_KEY_A -> new Vector3(-1.0, 0.0, 0.0);
                case GLFW_KEY_D -> new Vector3(1.0, 0.0, 0.0);
                case GLFW_KEY_LEFT_SHIFT -> new Vector3(0.0, -1.0, 0.0);
                case GLFW_KEY_SPACE -> new Vector3(0.0, 1.0, 0.0);
                default -> null;
            };

            if (direction != null && isPressed) move(direction);
        }
    }

    private void handleMouseEvent(InputSystem.MouseEvent event) {
        double xoffset = lastX >= 0.0 && lastY >= 0.0 ? event.xpos() - lastX : 0.0;
        double yoffset = lastX >= 0.0 && lastY >= 0.0 ? lastY - event.ypos() : 0.0; // reversed since y-coordinates go from bottom to top
        lastX = event.xpos();
        lastY = event.ypos();

        xoffset *= lookSpeed;
        yoffset *= lookSpeed;

        rotate(xoffset, yoffset);
    }

    public void move(Vector3 direction) {
        transform.position = Vector3.add(transform.position, Vector3.multiply(direction, new Vector3(moveSpeed)));
    }

    public void rotate(double yawOffset, double pitchOffset) {
        final Vector3 euler = Quaternion.toEuler(transform.rotation);
        double newPitch = pitchOffset + euler.x();
        if (newPitch > 89.0) newPitch = 89.0;
        if (newPitch < -89.0) newPitch = -89.0;

        transform.rotation = Quaternion.fromEuler(new Vector3(newPitch, euler.y() + yawOffset, 0.0), Quaternion.RotationOrder.ZYX);
    }
}
