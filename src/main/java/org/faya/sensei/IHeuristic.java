package org.faya.sensei;

public interface IHeuristic {

    /**
     * Calculates the heuristic cost between the start and goal nodes.
     *
     * @param start the start node
     * @param goal the goal node
     * @return the calculated heuristic cost
     */
    double calculate(final INode start, final INode goal);
}
