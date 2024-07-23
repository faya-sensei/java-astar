package org.faya.sensei;

import java.util.List;

public interface IPathfinderObserver {

    /**
     * Called when a node is processed during pathfinding.
     *
     * @param node The node that was processed.
     */
    void onNode(final INode node);

    /**
     * Called when a path is found or failure.
     *
     * @param path The list of nodes representing the found path or empty.
     */
    void onFinish(final List<INode> path);
}
