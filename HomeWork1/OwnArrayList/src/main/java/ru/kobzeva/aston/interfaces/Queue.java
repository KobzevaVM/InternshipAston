package ru.kobzeva.aston.interfaces;

/**
 * A linear collection that supports deleting of elements at the front .
 * @param <E> - the type of elements in this queue
 */
public interface Queue<E> {
    /**
     * Removes an element from the front of the queue.
     *
     * @return element from the front of the queue that has been removed
     */
    E remove();
}
