package org.faya.sensei;

public interface IEdge {

    /**
     * Get the source node of the edge.
     *
     * @return The source node.
     */
    INode getSource();

    /**
     * Get the destination node of the edge.
     *
     * @return The destination node.
     */
    INode getDestination();
}
