package org.faya.sensei;

import java.util.Optional;

public interface IPathfinderIterator {

    /**
     * Returns {@code true} if the iteration has more elements.
     *
     * @return {@code true} if the iteration has more elements
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     */
    Optional<INode> next();
}
