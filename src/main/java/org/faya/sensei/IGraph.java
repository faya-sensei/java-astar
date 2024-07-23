package org.faya.sensei;

import java.util.List;

public interface IGraph {

    /**
     * Retrieves a node based on its id in the graph.
     *
     * @param id The id of the node.
     * @return The node at the specified id.
     */
    default INode getNode(final int id) {
        return null;
    }

    /**
     * Retrieves a node based on its position in the graph.
     *
     * @param position The position of the node.
     * @return The node at the specified position.
     */
    default INode getNode(final double[] position) {
        return null;
    }

    /**
     * Retrieves an edge based on its starting node and ending node in the graph.
     *
     * @param from The starting node.
     * @param to   The ending node.
     * @return The edge connected the two node.
     */
    default IEdge getEdge(final INode from, final INode to) {
        return null;
    }

    /**
     * Retrieves the neighbors of a given node.
     *
     * @param node The node for which neighbors are to be found.
     * @return A list of neighboring nodes.
     */
    List<INode> getNeighbors(final INode node);

    /**
     * Retrieves the linked edges of a given node.
     *
     * @param node The node for which edges are to be found.
     * @return A list of linked edges.
     */
    default List<IEdge> getEdges(final INode node) {
        return List.of();
    }
}
