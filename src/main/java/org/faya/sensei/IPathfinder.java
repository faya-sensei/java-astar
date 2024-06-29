package org.faya.sensei;

import java.util.List;

public interface IPathfinder {

    /**
     * Finds a path from the start node to the goal node using the implemented
     * pathfinding algorithm.
     *
     * @param start the start node
     * @param goal the goal node
     * @return a list of nodes representing the path from start to goal
     */
    List<INode> findPath(final INode start, final INode goal);

    /**
     * Registers an observer to receive pathfinding events.
     *
     * @param observer the observer to be registered
     */
    void registerObserver(final IPathfinderObserver observer);

    /**
     * On search node callback, trigger when access to a node.
     *
     * @param node The access node.
     */
    void onNode(INode node);

    /**
     * On search finish callback, trigger when search success or failure.
     *
     * @param path The path of this search.
     */
    void onFinish(List<INode> path);
}
