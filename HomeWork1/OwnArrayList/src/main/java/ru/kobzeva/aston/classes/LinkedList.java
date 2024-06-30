package ru.kobzeva.aston.classes;

import ru.kobzeva.aston.interfaces.Deque;
import ru.kobzeva.aston.interfaces.List;
import ru.kobzeva.aston.interfaces.Queue;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * This class is designed for an ordered collection. Doubly-linked list.
 * Each element (Class Node<E>) contains a value, a link to the previous element, and a link to the next element.
 * Double-linked list stores the first node and the last node.
 * This class has methods for adding, removing, getting elements, sorting and clearing a collection.
 * @param <E> - the type of elements in this doubly-linked list.
 */
public class LinkedList<E> implements List<E>, Queue<E>, Deque<E> {
    /**
     * Default size
     */
    private final int DEFAULT_SIZE = 0;
    /**
     * The size of the double-linked list
     * */
    private int size = DEFAULT_SIZE;
    /**
     * The first node of the double-linked list
     */
    private Node<E> first;
    /**
     * The last node of the double-linked list
     */
    private Node<E> last;

    /**
     * Node class. A node contains an element (item), a link to the next node (Node<E> next)
     * and a link to the previous node (Node<E> prev)
     * @param <E> the type of elements in this doubly-linked list.
     */
    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        /**
         * Default constructor Node
         * @param prev the previous node
         * @param element value of the element
         * @param next the next node
         */
        Node(Node<E> prev, E element, Node<E> next) {
            item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Default constructor the double-linked list.
     * first and last node are null
     */
    public LinkedList() {
        first = null;
        last = null;
    }

    /**
     * Inserts an element at the end of the doubly-linked list
     *
     * @param e element to insert at the end of the doubly-linked list
     */
    public void add(E e) {
        if(first == null && last == null) {
            first = new Node<>(null, e, null);
            last = first;
        } else if (first == last) {
            last = new Node<>(first, e, null);
            first.next = last;
        } else {
            Node<E> temp = last;
            last = new Node<>(temp, e, null);
            temp.next = last;
        }
        size++;
    }

    /**
     * Inserts the specified element at the specified position in this doubly-linked list
     *
     * @param index position to insert element
     * @param e element to insert at the specified position of the doubly-linked list
     */
    public void add(int index, E e) {
        if(correctIndex(index)) {
            Node<E> temp = searchElementByIndex(index);
            Node<E> tempPrev = temp.prev;

            Node<E> newE = new Node<>(temp.prev, e, temp);
            if(tempPrev != null) {
                tempPrev.next = newE;
            }
            temp.prev = newE;

            if(index == 0) {
                first = newE;
            }
            size++;
        }
    }

    /**
     * Inserts an element at the front of the doubly-linked list
     *
     * @param e element to insert at the front of the doubly-linked list
     */
    public void addFirst(E e) {
        Node<E> newE = new Node<>(null, e, first);
        first.prev = newE;
        first = newE;
        size++;
    }

    /**
     * Inserts an element at the end of the doubly-linked list
     *
     * @param e element to insert at the end of the doubly-linked list
     */
    public void addLast(E e) {
        Node<E> newE = new Node<>(last, e, null);
        last.next = newE;
        last = newE;
        size++;
    }

    /**
     * Method to clear the list
     */
    public void clear() {
        size = 0;
        first = null;
        last = null;
    }

    /**
     * Method for getting an element by index
     *
     * @param index position in doubly-linked list
     * @return returns the element at the specified position in this doubly-linked list
     * @throws IndexOutOfBoundsException if(index >= size || index < 0)
     */
    public E get(int index) {
        if(correctIndex(index)) {
            return searchElementByIndex(index).item;
        }

        return null;
    }

    /**
     * Method for getting the first element
     *
     * @return returns the element at the front of this doubly-linked list
     * @throws NoSuchElementException if(size <= 0)
     */
    public E getFirst() {
        if(size <= 0) {
            throw new NoSuchElementException();
        }

        return first.item;
    }

    /**
     * Method for getting the last element
     *
     * @return returns the element at the end of this doubly-linked list
     * @throws NoSuchElementException if(size <= 0)
     */
    public E getLast() {
        if(size <= 0) {
            throw new NoSuchElementException();
        }

        return last.item;
    }

    /**
     * Removes an element from the front of the double-linked list.
     *
     * @return element from the front of the double-linked list that has been removed
     * @throws NoSuchElementException if size == 0
     */
    public E remove() {
        if(size > 0) {
            Node<E> temp = first;

            if(first.next != null) {
                first = first.next;
                first.prev = null;
            } else {
                first = null;
                last = null;
            }

            size--;

            return temp.item;
        }
        throw new NoSuchElementException();
    }

    /**
     * Method for getting an element by index
     *
     * @param index position in double-linked list
     * @return returns the element at the specified position in double-linked list
     * @throws IndexOutOfBoundsException if(index >= size || index < 0)
     */
    public E remove(int index) {
        if(index == size - 1) {
            return removeLast();
        }
        if(index == 0) {
            return removeFirst();
        }
        if(correctIndex(index)) {
            Node<E> temp = searchElementByIndex(index);

            temp.next.prev = temp.prev;
            temp.prev.next = temp.next;
            temp.prev = null;
            temp.next = null;
            size--;
            return temp.item;
        }

        throw new IndexOutOfBoundsException();
    }
    /**
     * Removes the first element from the front of the double-linked list that is equal to the given element
     *
     * @param e element that will be removed if found
     * @return returns true if the element is found in the double-linked list and has been removed
     */
    public boolean remove(E e) {
        Node<E> temp = first;

        for(int i = 0; i < size; i++) {
            if(temp.item.equals(e)) {
                remove(i);
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    /**
     * see method remove()
     *
     * @return element from the front of the double-linked list that has been removed
     */
    public E removeFirst() {
        return remove();
    }

    /**
     * Removes an element from the end of the double-linked list.
     *
     * @return element from the end of the double-linked list that has been removed
     * @throws NoSuchElementException if size == 0
     */
    public E removeLast() {
        if(size > 0) {
            Node<E> temp = last;
            if(last.prev != null) {
                last = last.prev;
                last.next = null;
            } else {
                first = null;
                last = null;
            }

            size--;

            return temp.item;
        }
        throw new NoSuchElementException();
    }
    /**
     * @return returns the size of the double-linked list
     */
    public int size() {
        return size;
    }
    /**
     * Method for sorting the dynamic array
     *
     * @param c custom comparator for sorting
     */
    public void sort(Comparator<? super E> c) {
        quickSort(c, size, first, last);
    }
    /**
     * Method for check correct index (index < size && index >= 0)
     * @param index - index to check
     * @return true if the condition (index < size && index >= 0) is true
     * @throws IndexOutOfBoundsException if(index >= size || index < 0)
     */
    private boolean correctIndex(int index) {
        if(index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return true;
    }

    /**
     * Method for searching element by index in doubly-linked sublist
     * @param index - position in doubly-linked sublist
     * @param first - first node of doubly-linked sublist
     * @param last - last node of doubly-linked sublist
     * @param size - size this doubly-linked sublist
     * @return Node from doubly-linked sublist in position
     */
    private Node<E> searchElementByIndex(int index, Node<E> first, Node<E> last, int size) {
        int k;
        Node<E> temp;

        if(index >= size / 2) {
            k = index;
            temp = first;
            while(k > 0) {
                temp = temp.next;
                k--;
            }
        } else {
            k = size - index - 1;
            temp = last;
            while(k > 0) {
                temp = temp.prev;
                k--;
            }
        }
        return temp;
    }

    /**
     * Method for searching element by index in all doubly-linked list
     * @param index - position in doubly-linked list
     * @return Node from doubly-linked list in position
     */
    private Node<E> searchElementByIndex(int index) {
        return searchElementByIndex(index, first, last, size);
    }
    /**
     * quicksort method for double-linked list using custom comparator
     * @param c custom comparator
     * @param size - size double-linked list
     * @param first first node of double-linked list
     * @param last last node of double-linked list
     */
    private void quickSort(Comparator<? super E> c, int size, Node<E> first, Node<E> last) {
        if(size <= 1) {
            return;
        }

        int middle = size / 2;
        E border = searchElementByIndex(middle, first, last, size).item;

        int i = 0, j = size - 1;
        Node<E> tempI = first, tempJ = last;

        while(i <= j) {
            while(c.compare(border, tempI.item) > 0) {
                tempI = tempI.next;
                i++;
            }

            while(c.compare(tempJ.item, border) > 0) {
                tempJ = tempJ.prev;
                j--;
            }

            if(i <= j) {
                E temp = tempI.item;
                tempI.item = tempJ.item;
                tempJ.item = temp;
                tempI = tempI.next;
                tempJ = tempJ.prev;
                i++;
                j--;
            }

        }
        if(0 < j) quickSort(c, j + 1, first, tempJ);
        if(size > i) quickSort(c, size - i, tempI, last);
    }

}
