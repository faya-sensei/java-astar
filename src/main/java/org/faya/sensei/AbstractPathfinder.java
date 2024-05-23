package org.faya.sensei;

import java.util.*;

public abstract class AbstractPathfinder {

    protected PriorityQueue<INode> openList;
    protected Set<INode> closedList;

    public AbstractPathfinder() {
        openList = new PriorityQueue<>(Comparator.comparingDouble(INode::getCost));
        closedList = new HashSet<>();
    }

    /**
     * Finds a path from the start node to the goal node within the given graph.
     *
     * @param graph the graph will be performed
     * @param start the starting node of the path
     * @param goal the goal node of the path
     * @return a list of nodes representing the path from start to goal
     */
    public List<INode> findPath(IGraph graph, INode start, INode goal) {
        start.setCost(0);
        openList.add(start);

        while (!openList.isEmpty()) {
            INode current = openList.poll();

            if (current.equals(goal)) return reconstructPath(current);

            closedList.add(current);

            for (INode neighbor : graph.getNeighbors(current)) {
                if (closedList.contains(neighbor)) continue;

                double tentativeG = current.getCost() + graph.getCost(current, neighbor);

                if (!openList.contains(neighbor) || tentativeG < neighbor.getCost()) {
                    neighbor.setCost(tentativeG);
                    neighbor.setParent(current);

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Reconstructs the path from the goal node to the start node by following
     * the parent nodes.
     *
     * @param node the goal node from which the path reconstruction begins
     * @return a list of nodes representing the path from start to goal
     */
    protected List<INode> reconstructPath(INode node) {
        List<INode> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Calculates the heuristic cost between two nodes.
     *
     * @param a the first node
     * @param b the second node
     * @return the heuristic cost between the two nodes
     */
    protected abstract double heuristic(INode a, INode b);
}
