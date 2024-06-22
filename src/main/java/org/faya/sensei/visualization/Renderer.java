package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Camera;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Renderer {

    private InputSystem inputSystem;
    private Camera camera;
    private Shader shader;

    public Renderer() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

    }

    public void render() {
        shader.bind();



        shader.unbind();
    }

    public void dispose() {
        shader.dispose();
    }
}
