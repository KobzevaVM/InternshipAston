package ru.kobzeva.aston.classes;

import ru.kobzeva.aston.interfaces.List;

import java.util.Comparator;

/**
 * This class is designed for an ordered collection. Dynamic array.
 * This class has methods for adding, removing, getting elements, sorting and clearing a collection.
 * @param <E> - the type of elements in this dynamic array.
 */

public class ArrayList<E> implements List<E> {
    /**
     * Default Capacity
     */
    private final int DEFAULT_CAPACITY = 10;
    /**
     * Dynamic array size
     */
    private int size;
    /**
     * Dynamic array capacity. If the size becomes larger than the capacity, then the capacity increases by 1.5 times
     */
    private int capacity;
    /**
     * Array of elements
     */
    private Object[] array;

    /**
     * Default constructor
     */
    public ArrayList() {
        size = 0;
        capacity = DEFAULT_CAPACITY;
        array = new Object[capacity];
    }

    /**
     * Inserts an element at the end of the dynamic array
     *
     * @param e element to insert at the end of the dynamic array
     */
    public void add(E e) {
        expendArray();
        array[size++] = e;
    }

    /**
     * Inserts the specified element at the specified position in this dynamic array
     *
     * @param index position to insert element
     * @param e element to insert at the specified position of the dynamic array
     */
    public void add(int index, E e) {
        if(index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        expendArray();

        Object temp = e, temp2;
        for(int i = index; i <= size; i++) {
            temp2 = array[i];
            array[i] = temp;
            temp = temp2;
        }
        size++;
    }
    /**
     * Method to clear the list
     */
    public void clear() {
        for(int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }
    /**
     * Method for getting an element by index
     *
     * @param index position in dynamic array
     * @return returns the element at the specified position in this dynamic array.
     * @throws IndexOutOfBoundsException if(index >= size || index < 0)
     */
    public E get(int index) {
        if(index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return (E) array[index];
    }
    /**
     * Removes the first element from the front of the dynamic array that is equal to the given element
     *
     * @param e element that will be removed if found
     * @return returns true if the element is found in the dynamic array and has been removed
     */
    public boolean remove(E e) {
        for(int i = 0; i < size; i++) {
            if(array[i].equals(e)) {
                remove(i);
                return true;
            }
        }
        return false;
    }
    /**
     * Removes an element from the dynamic array by index
     *
     * @param index position in dynamic array
     * @return returns the element that was removed
     * @throws IndexOutOfBoundsException if(index >= size || index < 0)
     */
    public E remove(int index) {
        if(index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Object temp = array[index];
        for(int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        array[--size] = null;
        return (E) temp;
    }
    /**
     * @return returns the size of the dynamic array
     */
    public int size() {
        return this.size;
    }
    /**
     * Method for sorting the dynamic array
     *
     * @param c custom comparator for sorting
     */
    public void sort(Comparator<? super E> c) {
        quickSort(c, (E[]) array, 0, this.size - 1);
    }

    /**
     * An array extension that is used in the add methods.
     * If size = capacity, then the array is increased by 1.5 times and all elements are copied into a new array.
     */
    private void expendArray() {
        if(size == capacity) {
            capacity = (capacity * 3) / 2 + 1;
            Object[] tempArray = new Object[capacity];
            System.arraycopy(array, 0, tempArray, 0, array.length);
            array = tempArray;
        }
    }

    /**
     * quicksort method using custom comparator
     * @param c custom comparator
     * @param array array to be sorted
     * @param low position in the array starting from which you want to sort
     * @param high the last position of the array including the one to be sorted
     */
    private void quickSort(Comparator<? super E> c, E[] array, int low, int high) {
        if(low >= high || size <= 1) {
            return;
        }

        int middle = (low + high) / 2;
        E border = array[middle];

        int i = low, j = high;

        while(i <= j) {
            while(c.compare(border, array[i]) > 0) i++;
            while(c.compare(array[j], border) > 0) j--;

            if(i <= j) {
                E temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        if(high > i) quickSort(c, array, i, high);
        if(low < j) quickSort(c, array, low, j);
    }

}
