package ru.kobzeva.aston.interfaces;

import java.util.Comparator;

/**
 * This interface is designed for an ordered collection.
 * There are two methods for adding elements: by index and at the end of the list.
 * There is a method to clear the list.
 * There is a method for getting an element by index.
 * There are two methods to remove an element: by element and by index.
 * There is a method for getting the size of the list.
 * There is a method for sorting a list (you need a comparator).
 * @param <E> - the type of elements in this list
 */

public interface List<E> {

    /**
     * Inserts an element at the end of a list
     *
     * @param e element to insert at the end of the list
     */
    void add(E e);
    /**
     * Inserts the specified element at the specified position in this list
     *
     * @param index position to insert element
     * @param e element to insert at the specified position of the list
     */
    void add(int index, E e);
    /**
     * Method to clear the list
     */
    void clear();
    /**
     * Method for getting an element by index
     *
     * @param index position in list
     * @return returns the element at the specified position in this list.
     */
    E get(int index);
    /**
     * Removes the first element from the front of the list that is equal to the given element
     *
     * @param e element that will be removed if found
     * @return returns true if the element is found in the list and has been removed
     */
    boolean remove(E e);

    /**
     * Removes an element from the list by index
     *
     * @param index position in list
     * @return returns the element that was removed
     */
    E remove(int index);

    /**
     * @return returns the size of the list
     */
    int size();
    /**
     * Method for sorting a list
     *
     * @param c comparator for sorting
     */
    void sort(Comparator<? super E> c);


}
