package org.faya.sensei;

import java.util.List;

public interface IGraph {

    /**
     * Retrieves a node based on its id in the graph.
     *
     * @param id the id of the node.
     * @return The node at the specified id.
     */
    default INode getNode(final int id) {
        return null;
    }

    /**
     * Retrieves a node based on its position in the graph.
     *
     * @param position the position of the node.
     * @return the node at the specified position.
     */
    default INode getNode(final double[] position) {
        return null;
    }

    /**
     * Retrieves the neighbors of a given node.
     *
     * @param node the node for which neighbors are to be found.
     * @return a list of neighboring nodes.
     */
    default List<INode> getNeighbors(final INode node) {
        return List.of();
    }

    /**
     * Retrieves the linked edges of a given node.
     *
     * @param node the node for which edges are to be found.
     * @return a list of linked edges.
     */
    default List<IEdge> getEdges(final INode node) {
        return List.of();
    }
}
