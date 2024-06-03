package org.faya.sensei;

import java.util.List;

public interface IPathfinderObserver {

    /**
     * Called when a node is processed during pathfinding.
     *
     * @param node the node that was processed
     */
    void onNode(final INode node);

    /**
     * Called when a path is found.
     *
     * @param path the list of nodes representing the found path
     */
    void onPathFound(final List<INode> path);
}
