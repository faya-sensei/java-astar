package org.faya.sensei.visualization;

import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.visualization.components.Camera;
import org.faya.sensei.visualization.components.Renderer;
import org.lwjgl.opengl.GL;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class EngineRenderer {

    private static final float fov = (float) Math.toRadians(60.0f);
    private static final float zFar = 1000.f;
    private static final float zNear = 0.01f;

    private final ShaderSystem shaderSystem = ShaderSystem.getInstance();
    private final Window window;

    private Matrix4x4 projectionMatrix;
    private List<Renderer> renderer;
    private Camera camera;

    public EngineRenderer(final Window window) {
        this.window = window;

        shaderSystem.registerGlobalUniform("projectionMatrix");
        shaderSystem.registerGlobalUniform("viewMatrix");

        updateProjectionMatrix();
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setRenderer(List<Renderer> renderer) {
        this.renderer = renderer;
    }

    public void init() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    public void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        shaderSystem.setUniform("projectionMatrix", projectionMatrix);
        shaderSystem.setUniform("viewMatrix", camera.getViewMatrix());

        renderer.forEach(Renderer::render);
    }

    public void updateProjectionMatrix() {
        projectionMatrix = Matrix4x4.perspective(fov, (float) window.getWidth() / window.getHeight(), zNear, zFar);
    }

    public void dispose() {
        shaderSystem.dispose();
    }
}
