package org.faya.sensei.visualization.components;

import org.faya.sensei.visualization.EngineEntity;

public abstract class Component {

    protected EngineEntity entity;

    // Getter and setter

    public EngineEntity getEntity() {
        return entity;
    }

    public void setEntity(final EngineEntity entity) {
        this.entity = entity;
    }

    // Abstract function

    /**
     * Called when the engine begins.
     */
    public void start() { }

    /**
     * Called every frame.
     *
     * @param deltaTime The delta Time.
     */
    public void update(final float deltaTime) { }

    /**
     * Called every physics time-step.
     */
    public void fixedUpdate() { }

    /**
     * Called when the engine ends.
     */
    public void dispose() { }
}
