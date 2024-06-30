package ru.kobzeva.aston.interfaces;

/**
 * A linear collection that supports insertion and deletion of elements at both ends.
 * There are two methods for inserting elements: at the front and at the end of the list.
 * There are two methods for getting elements: from the front and from the end of the list.
 * There are two methods for removing elements: from the front and from the end of the list.
 * @param <E> - the type of elements in this deque
 */
public interface Deque<E> {
    /**
     * Inserting an element at the front of the deque.
     *
     * @param e element to insert at the front of the deque
     */
    void addFirst(E e);
    /**
     * Inserting an element at the end of the deque.
     *
     * @param e element to insert at the end of the deque
     */
    void addLast(E e);
    /**
     * Returns an element from the front of the deque.
     *
     * @return element from the front of the deque
     */
    E getFirst();
    /**
     * Returns an element from the end of the deque.
     *
     * @return element from the end of the deque
     */
    E getLast();
    /**
     * Removes an element from the front of the deque.
     *
     * @return element from the front of the deque that has been removed
     */
    E removeFirst();
    /**
     * Removes an element from the end of the deque.
     *
     * @return element from the end of the deque that has been removed
     */
    E removeLast();
}
