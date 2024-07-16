package org.faya.sensei.visualization.components;

import org.faya.sensei.visualization.Shader;

public abstract class Renderer extends Component {

    protected final Shader shader;

    public Renderer(Shader shader) {
        this.shader = shader;
    }

    /** @inheritDoc */
    @Override
    public void start() {
        shader.init();
    }

    /**
     * Performs the actual drawing of elements. Implemented in derived classes
     * to handle specific draw calls.
     */
    public abstract void render();
}
