package org.faya.sensei;

public interface IEdge {

    /**
     * Retrieves the source node.
     *
     * @return the source node
     */
    INode getSource();

    /**
     * Retrieves the destination node.
     *
     * @return the destination node
     */
    INode getDestination();

    /**
     * Retrieves the cost of the edge.
     *
     * @return the cost of the edge
     */
    double getCost();

    abstract class Decorator implements IEdge {

        protected final IEdge decoratedEdge;

        public Decorator(IEdge decoratedEdge) {
            this.decoratedEdge = decoratedEdge;
        }

        @Override
        public INode getSource() {
            return decoratedEdge.getSource();
        }

        @Override
        public INode getDestination() {
            return decoratedEdge.getDestination();
        }

        @Override
        public double getCost() {
            return decoratedEdge.getCost();
        }
    }
}
