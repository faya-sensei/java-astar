package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Engine {

    private static final int FIXED_UPDATE_PER_SECOND = 60;

    private final List<Component> components = new ArrayList<>();
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final Window window;
    private Renderer renderer;

    public Engine(Window window) {
        this.window = window;
    }

    public void start() {
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

            initialTime = now;
        }

        components.forEach(Component::dispose);
    }

    public void stop() {
        running.set(false);
    }
}
