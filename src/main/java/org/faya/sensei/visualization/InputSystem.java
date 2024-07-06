package org.faya.sensei.visualization;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;

public class InputSystem {

    private static InputSystem instance;

    private final Map<Class<? extends IEvent>, Set<Consumer<? extends IEvent>>> listeners = new HashMap<>();

    public interface IEvent { }

    public record KeyEvent(int key, int scancode, int action, int mods) implements IEvent { }

    public record MouseEvent(double xpos, double ypos) implements IEvent { }

    public static InputSystem getInstance() {
        if (instance == null) instance = new InputSystem();
        return instance;
    }

    public void init(final Window window) {
        glfwSetKeyCallback(window.getHandle(), (handle, key, scancode, action, mods) -> {
            notifyListeners(new KeyEvent(key, scancode, action, mods));
        });
        glfwSetCursorPosCallback(window.getHandle(), (handle, xpos, ypos) -> {
            notifyListeners(new MouseEvent(xpos, ypos));
        });
        glfwSetInputMode(window.getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public <T extends IEvent> void addEventListener(final Class<T> eventType, final Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(listener);
    }

    public <T extends IEvent> void removeEventListener(final Class<T> eventType, final Consumer<T> listener) {
        Set<Consumer<? extends IEvent>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) eventListeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    private <T extends IEvent> void notifyListeners(final T event) {
        Set<Consumer<? extends IEvent>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (Consumer<? extends IEvent> listener : eventListeners) {
                ((Consumer<T>) listener).accept(event);
            }
        }
    }

    public void dispose() {
        listeners.clear();
        instance = null;
    }
}
