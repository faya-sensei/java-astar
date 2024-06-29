package org.faya.sensei.visualization;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long handle;
    private int width, height;

    public Window(final String title, final int width, final int height) {
        this.width = width;
        this.height = height;

        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) throw new RuntimeException("Failed to create the GLFW window.");

        GLFWErrorCallback.createPrint(System.err).set();

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        glfwShowWindow(handle);
    }

    public long getHandle() {
        return handle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void dispose() {
        glfwSetWindowShouldClose(handle, true);
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
