package org.faya.sensei.visualization;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long handle;
    private final int width;
    private final int height;

    public Window(final String title, final int width, final int height) {
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        GLFWErrorCallback.createPrint(System.err).set();

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        if (width > 0 && height > 0) {
            this.width = width;
            this.height = height;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            this.width = vidMode != null ? vidMode.width() : 800;
            this.height = vidMode != null ? vidMode.height() : 600;
        }

        handle = glfwCreateWindow(this.width, this.height, title, NULL, NULL);
        if (handle == NULL) throw new RuntimeException("Failed to create the GLFW window.");
    }

    public void init() {
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
