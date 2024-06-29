package org.faya.sensei.visualization;

import org.faya.sensei.visualization.components.Component;

import java.util.*;

public class EngineScene {

    private final Map<UUID, EngineEntity> entityPool = new HashMap<>();

    public List<Component> getComponents() {
        final List<Component> allComponents = new ArrayList<>();
        for (final EngineEntity entity : entityPool.values()) {
            allComponents.addAll(entity.getComponents());
        }
        return allComponents;
    }

    public void addEntity(final EngineEntity entity) {
        final UUID entityId = entity.getId();
        entityPool.put(entityId, entity);

        for (final EngineEntity child : entity.getChildren()) {
            addEntity(child);
        }
    }
}
