package org.faya.sensei;

import org.lwjgl.assimp.AIMesh;

public interface IGraphBuilder {

    /**
     * Build the graph based on mesh.
     *
     * @param mesh the mesh instance.
     * @return the graph instance.
     */
    IGraph build(final AIMesh mesh);

    /**
     * Build the grid graph based on size.
     *
     * @param width the width of the grid graph.
     * @param height the height of the grid graph.
     * @return the graph instance.
     */
    IGraph build(final int width, final int height);

    /**
     * Build the grid graph based on size.
     *
     * @param width the width of the grid graph.
     * @param height the height of the grid graph.
     * @param depth the depth of the grid graph.
     * @return the graph instance.
     */
    IGraph build(final int width, final int height, final int depth);
}
