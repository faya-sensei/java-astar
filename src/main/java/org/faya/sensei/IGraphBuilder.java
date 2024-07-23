package org.faya.sensei;

import org.lwjgl.assimp.AIMesh;

public interface IGraphBuilder {

    /**
     * Build the graph based on mesh.
     *
     * @param mesh The mesh instance.
     * @return The graph instance.
     */
    IGraph build(final AIMesh mesh);

    /**
     * Build the grid graph based on size.
     *
     * @param width  The width of the grid graph.
     * @param height The height of the grid graph.
     * @return The graph instance.
     */
    IGraph build(final int width, final int height);

    /**
     * Build the grid graph based on size.
     *
     * @param width  The width of the grid graph.
     * @param height The height of the grid graph.
     * @param depth  The depth of the grid graph.
     * @return The graph instance.
     */
    IGraph build(final int width, final int height, final int depth);
}
