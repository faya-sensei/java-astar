package org.faya.sensei;

import java.util.List;

public interface IGraph {

    /**
     * Retrieves a node based on its position in the graph.
     *
     * @param position the position of the node, typically represented as an
     *                array of coordinates
     * @return the node at the specified position
     */
    INode getNode(double[] position);

    /**
     * Retrieves the neighbors of a given node.
     *
     * @param node the node for which neighbors are to be found
     * @return a list of neighboring nodes
     */
    List<INode> getNeighbors(INode node);
}
