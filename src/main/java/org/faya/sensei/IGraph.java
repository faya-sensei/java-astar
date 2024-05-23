package org.faya.sensei;

import java.util.List;

/**
 * The Graph interface represents a graph structure. It provides methods to get the neighbors of a node
 * and to get the cost between two nodes.
 */
public interface IGraph {
    /**
     * Returns a list of neighboring nodes for a given node.
     *
     * @param node The node for which to get the neighbors.
     * @return A list of Node objects representing the neighbors of the given node.
     */
    List<INode> getNeighbors(INode node);

    /**
     * Returns the cost of moving from one node to another.
     *
     * @param from The starting node.
     * @param to The target node.
     * @return The cost as a double of moving from the `from` node to the `to` node.
     */
    double getCost(INode from, INode to);
}