package org.faya.sensei;

public interface INode {

    /**
     * Retrieves the position of the node.
     *
     * @return the position of the node as a float array
     */
    double[] getPosition();

    /**
     * Retrieves the G cost of the node.
     *
     * @return the G cost of the node
     */
    double getGCost();

    /**
     * Sets the G cost of the node.
     *
     * @param gCost the G cost to be set
     */
    void setGCost(double gCost);

    /**
     * Retrieves the H cost of the node.
     *
     * @return the H cost of the node
     */
    double getHCost();

    /**
     * Sets the H cost of the node.
     *
     * @param hCost the H cost to be set
     */
    void setHCost(double hCost);


    /**
     * Retrieves the F cost of the node, typically calculated as the sum of G
     * cost and H cost.
     *
     * @return the F cost of the node
     */
    double getFCost();

    /**
     * Retrieves the parent node of this node.
     *
     * @return the parent node, or null if this node has no parent
     */
    INode getParent();

    /**
     * Sets the parent node of this node.
     *
     * @param parent the parent node to be set
     */
    void setParent(INode parent);
}
