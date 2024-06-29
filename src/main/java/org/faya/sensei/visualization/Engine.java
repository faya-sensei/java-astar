package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Camera;
import org.faya.sensei.visualization.components.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {

    private static final int FIXED_UPDATE_PER_SECOND = 60;

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
        components.addAll(scene.getComponents());
        renderer.setCamera(
                scene.getComponents().stream()
                        .filter(component -> component instanceof Camera)
                        .map(component -> (Camera) component)
                        .findFirst()
                        .orElse(null)
        );

        inputSystem.init(window);
        inputSystem.addEventListener(InputSystem.KeyEvent.class, event -> {
            if (event.key() == GLFW_KEY_ESCAPE && event.action() == GLFW_RELEASE)
                stop();
        });

        running.set(true);

        components.forEach(Component::start);

        long initialTime = System.currentTimeMillis();
        final float fixedDeltaTime = 1.0f / FIXED_UPDATE_PER_SECOND;
        float accumulator = 0;

        while (running.get() && !window.windowShouldClose()) {
            window.pollEvents();

            final long now = System.currentTimeMillis();
            final float deltaTime = (now - initialTime) / 1000.0f;
            accumulator += deltaTime;

            while (accumulator >= fixedDeltaTime) {
                components.forEach(Component::fixedUpdate);
                accumulator -= fixedDeltaTime;
            }

            components.forEach(component -> component.update(deltaTime));

            renderer.render();

            initialTime = now;
        }

        components.forEach(Component::dispose);
        window.dispose();
    }

    public void stop() {
        running.set(false);
    }
}
