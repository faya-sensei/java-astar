package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Component;

import java.util.ArrayList;
import java.util.List;

public class EngineBuilder {

    public static class EngineSceneBuilder {

        private final List<EngineEntity> entities = new ArrayList<>();

        public EngineSceneBuilder addEntity(final EngineEntityBuilder builder) {
            entities.add(builder.build());
            return this;
        }

        public EngineScene build() {
            final EngineScene scene = new EngineScene();
            entities.forEach(scene::addEntity);
            return scene;
        }
    }

    public static class EngineEntityBuilder {

        private final List<Component> components = new ArrayList<>();
        private final List<EngineEntityBuilder> children = new ArrayList<>();

        public <T extends Component> EngineEntityBuilder addComponent(final T component) {
            components.add(component);
            return this;
        }

        public EngineEntityBuilder addChild(final EngineEntityBuilder builder) {
            children.add(builder);
            return this;
        }

        public EngineEntity build() {
            final EngineEntity entity = new EngineEntity();
            components.forEach(entity::addComponent);
            children.forEach(builder -> {
                final EngineEntity child = builder.build();
                child.getTransform().setParent(entity.getTransform());
            });
            return entity;
        }
    }

    private EngineScene scene;
    private Window window;

    public EngineBuilder window(final Window window) {
        this.window = window;
        return this;
    }

    public EngineBuilder scene(final EngineSceneBuilder builder) {
        this.scene = builder.build();
        return this;
    }

    public Engine build() {
        return new Engine(scene, window);
    }
}
