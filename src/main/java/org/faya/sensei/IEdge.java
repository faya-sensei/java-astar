package org.faya.sensei;

public interface IEdge {

    /**
     * Retrieves the source node.
     *
     * @return The source node.
     */
    INode getSource();

    /**
     * Retrieves the destination node.
     *
     * @return The destination node.
     */
    INode getDestination();

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
    }
}
