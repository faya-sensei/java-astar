package org.faya.sensei;

public interface INode {

    /**
     * Retrieves the position of the node.
     *
     * @return The position of the node as a double array.
     */
    double[] getPosition();

    /**
     * Retrieves the cost from the start node to the current node (G cost).
     *
     * @return The G cost of the node.
     */
    double getGCost();

    /**
     * Sets the cost from the start node to the current node (G cost).
     *
     * @param gCost The G cost to be set.
     */
    void setGCost(final double gCost);

    /**
     * Retrieves the heuristic cost estimate from the current node to the target
     * node.
     *
     * @return The H cost of the node.
     */
    double getHCost();

    /**
     * Sets the heuristic cost estimate from the current node to the target node.
     *
     * @param hCost The H cost to be set.
     */
    void setHCost(final double hCost);

    /**
     * Retrieves the F cost of the node, is used to determine the priority of
     * the node.
     *
     * @return The F cost of the node.
     */
    double getFCost();

    /**
     * Retrieves the parent node of this node.
     *
     * @return The parent node, or null if this node has no parent.
     */
    INode getParent();

    /**
     * Sets the parent node of this node.
     *
     * @param parent The parent node to be set.
     */
    void setParent(final INode parent);

    abstract class Decorator implements INode {

        protected final INode decoratedNode;

        public Decorator(final INode decoratedNode) {
            this.decoratedNode = decoratedNode;
        }

        @Override
        public double[] getPosition() {
            return decoratedNode.getPosition();
        }

        @Override
        public double getGCost() {
            return decoratedNode.getGCost();
        }

        @Override
        public void setGCost(final double gCost) {
            decoratedNode.setGCost(gCost);
        }

        @Override
        public double getHCost() {
            return decoratedNode.getHCost();
        }

        @Override
        public void setHCost(final double hCost) {
            decoratedNode.setHCost(hCost);
        }

        @Override
        public double getFCost() {
            return decoratedNode.getFCost();
        }

        @Override
        public INode getParent() {
            return decoratedNode.getParent();
        }

        @Override
        public void setParent(final INode parent) {
            decoratedNode.setParent(parent);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            return decoratedNode.equals(((Decorator) obj).decoratedNode);
        }

        @Override
        public int hashCode() {
            return decoratedNode.hashCode();
        }
    }
}
