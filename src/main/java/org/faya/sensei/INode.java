package org.faya.sensei;

/**
 * The Node interface represents a node in a graph. It provides methods to get the position of the node,
 * get the cost associated with the node, and set the cost of the node.
 */
public interface INode {
    /**
     * Returns the position of the node as an array of integers.
     *
     * @return An array of integers representing the position of the node.
     */
    float[] getPosition();

    /**
     * Returns the cost associated with the node.
     *
     * @return The cost of the node as a double.
     */
    double getCost();

    /**
     * Sets the cost associated with the node.
     *
     * @param cost The cost to be set for the node.
     */
    void setCost(double cost);

    /**
     * Returns the parent node of this node.
     *
     * @return The parent node of this node.
     */
    INode getParent();

    /**
     * Sets the parent node of this node.
     *
     * @param parent The parent node of this node.
     */
    void setParent(INode parent);
}
