package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Component;
import org.faya.sensei.visualization.components.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EngineEntity {

    private final UUID id = UUID.randomUUID();
    private final List<Component> components = new ArrayList<>();
    private final Transform transform;

    public EngineEntity() {
        this(null);
    }

    public EngineEntity(final Transform parent) {
        this.transform = addComponent(new Transform(parent));
    }

    // Getter and setter

    public UUID getId() {
        return id;
    }

    public List<Component> getComponents() {
        return components;
    }

    public Transform getTransform() {
        return transform;
    }

    // Function

    public EngineEntity getParent() {
        final Transform parentTransform = transform.getParent();
        return parentTransform != null ? parentTransform.getEntity() : null;
    }

    public List<EngineEntity> getChildren() {
        final List<EngineEntity> childEntities = new ArrayList<>();
        for (final Transform childTransform : transform.getChildren()) {
            childEntities.add(childTransform.getEntity());
        }
        return childEntities;
    }

    public List<Component> getComponents(final Class<? extends Component> clazz) {
        return components.stream().filter(clazz::isInstance).toList();
    }

    public Component getComponent(final Class<? extends Component> clazz) {
        return components.stream().filter(clazz::isInstance).findFirst().orElse(null);
    }

    public <T extends Component> T addComponent(T component) {
        component.setEntity(this);
        components.add(component);
        return component;
    }
}
