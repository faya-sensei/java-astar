package org.faya.sensei;

public interface IHeuristic {

    /**
     * Calculates the heuristic cost between the start and goal nodes.
     *
     * @param start The start node.
     * @param goal  The goal node.
     * @return The calculated heuristic cost.
     */
    double calculate(final INode start, final INode goal);
}
