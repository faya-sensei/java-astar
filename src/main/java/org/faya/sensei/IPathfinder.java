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
}
