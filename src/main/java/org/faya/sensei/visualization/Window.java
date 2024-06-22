package org.faya.sensei.visualization;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long handle;

    public Window(final String title, final int width, final int height) {
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) throw new RuntimeException("Failed to create the GLFW window.");

        GLFWErrorCallback.createPrint(System.err).set();

        glfwShowWindow(handle);

        InputSystem.getInstance().init(handle);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(handle, keyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void dispose() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();

        try (GLFWErrorCallback callback = glfwSetErrorCallback(null)) {
            if (callback != null) {
                callback.free();
            }
        }
    }
}
