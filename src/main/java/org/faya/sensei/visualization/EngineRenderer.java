package org.faya.sensei.visualization;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.visualization.components.Camera;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class EngineRenderer {

    private static final float fov = (float) Math.toRadians(60.0f);
    private static final float zFar = 1000.f;
    private static final float zNear = 0.01f;

    private final ShaderSystem shader = ShaderSystem.getInstance();
    private final Window window;

    private Matrix4x4 projectionMatrix;
    private Camera camera;

    public EngineRenderer(final Window window) {
        this.window = window;

        this.shader.registerGlobalUniform("projectionMatrix");
        this.shader.registerGlobalUniform("viewMatrix");

        updateProjectionMatrix();

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void render() {
        System.out.println("Render");
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        shader.setUniform("projectionMatrix", projectionMatrix);
        shader.setUniform("viewMatrix", camera.getViewMatrix());
    }

    public void updateProjectionMatrix() {
        projectionMatrix = Matrix4x4.perspective(fov, (float) window.getWidth() / window.getHeight(), zNear, zFar);
    }
}
