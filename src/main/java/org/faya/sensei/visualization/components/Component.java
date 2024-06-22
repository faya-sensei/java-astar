package org.faya.sensei.visualization.components;

import org.faya.sensei.visualization.EngineObject;

public abstract class Component {

    public EngineObject engineObject;

    public void start() { }

    public void update(final float deltaTime) { }

    public void fixedUpdate() { }

    public void dispose() { }
}
