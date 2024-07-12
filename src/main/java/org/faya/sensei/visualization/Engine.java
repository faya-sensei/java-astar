package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Camera;
import org.faya.sensei.visualization.components.Component;
import org.faya.sensei.visualization.components.MeshRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {

    private static final int FIXED_UPDATE_PER_SECOND = 60;
    private static final int FRAME_PER_SECOND = 60;

    private final List<Component> components = new ArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final EngineRenderer renderer;
    private final EngineScene scene;

    private final InputSystem inputSystem = InputSystem.getInstance();
    private final Window window;

    public Engine(final EngineScene scene, final Window window) {
        this.scene = scene;
        this.window = window;
        this.renderer = new EngineRenderer(window);
    }

    public void start() {
        window.init();
        inputSystem.init(window);
        renderer.init();

        components.addAll(scene.getComponents());
        renderer.setCamera(
                scene.getComponents().stream()
                        .filter(component -> component instanceof Camera)
                        .map(component -> (Camera) component)
                        .findFirst()
                        .orElse(null)
        );
        renderer.setRenderer(
                scene.getComponents().stream()
                        .filter(component -> component instanceof MeshRenderer)
                        .map(component -> (MeshRenderer) component)
                        .toList()
        );

        inputSystem.addEventListener(InputSystem.KeyEvent.class, event -> {
            if (event.key() == GLFW_KEY_ESCAPE && event.action() == GLFW_RELEASE)
                stop();
        });

        running.set(true);

        components.forEach(Component::start);

        long previousTime = System.nanoTime();
        final float fixedDeltaTime = 1.0f / FIXED_UPDATE_PER_SECOND;
        final float frameDeltaTime = 1.0f / FRAME_PER_SECOND;
        float accumulator = 0;

        while (running.get() && !window.windowShouldClose()) {
            final long currentTime = System.nanoTime();
            final float deltaTime = (currentTime - previousTime) / 1e9f;
            previousTime = currentTime;
            accumulator += deltaTime;

            window.pollEvents();

            while (accumulator >= fixedDeltaTime) {
                components.forEach(Component::fixedUpdate);
                accumulator -= fixedDeltaTime;
            }

            components.forEach(component -> component.update(deltaTime));

            renderer.render();

            glfwSwapBuffers(window.getHandle());

            double elapsedTime = (System.nanoTime() - currentTime) / 1e9;
            while (elapsedTime < frameDeltaTime) {
                elapsedTime = (System.nanoTime() - currentTime) / 1e9;
            }
        }

        components.forEach(Component::dispose);
        renderer.dispose();
        window.dispose();
        inputSystem.dispose();
    }

    public void stop() {
        running.set(false);
    }
}
