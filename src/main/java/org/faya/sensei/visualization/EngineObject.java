package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Component;
import org.faya.sensei.visualization.components.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EngineObject {

    private final List<Component> components = new ArrayList<>();

    public EngineObject(final Transform parent) {
        Transform transform = new Transform(parent);
        transform.engineObject = this;

        components.add(transform);
    }

    public List<Component> getComponents() {
        return components;
    }

    public List<Component> getComponents(Class<? extends Component> clazz) {
        return components.stream().filter(clazz::isInstance).toList();
    }

    public Component getComponent(Class<? extends Component> clazz) {
        Optional<Component> component = components.stream().filter(clazz::isInstance).findFirst();

        return component.orElse(null);
    }

    public <T extends Component> T addComponent(T component) {
        component.engineObject = this;

        components.add(component);

        return component;
    }
}
