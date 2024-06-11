package edu.ncsu.csc316.cleaning.factory;

import java.util.Comparator;

import edu.ncsu.csc316.dsa.data.Identifiable;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.list.SinglyLinkedList;
import edu.ncsu.csc316.dsa.list.positional.PositionalLinkedList;
import edu.ncsu.csc316.dsa.list.positional.PositionalList;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.SkipListMap;
import edu.ncsu.csc316.dsa.queue.ArrayBasedQueue;
import edu.ncsu.csc316.dsa.queue.Queue;
import edu.ncsu.csc316.dsa.sorter.CountingSorter;
import edu.ncsu.csc316.dsa.sorter.QuickSorter;
import edu.ncsu.csc316.dsa.sorter.Sorter;
import edu.ncsu.csc316.dsa.stack.LinkedStack;
import edu.ncsu.csc316.dsa.stack.Stack;

/**
 * Factory for creating new data structure and algorithm instances
 * 
 * @author Dr. King
 * @author Ethan Treece
 *
 */
public class DSAFactory {

    /**
     * Returns a data structure that implements a map
     * 
     * @param <K>
     *            - the key type
     * @param <V>
     *            - the value type
     * @return a data structure that implements a map
     */
    public static <K extends Comparable<K>, V> Map<K, V> getMap() {
        // return a type of Map
        return getSkipListMap(null);
    }

    /**
     * Returns a data structure that implements an index-based list
     * 
     * @param <E>
     *            - the element type
     * @return an index-based list
     */
    public static <E> List<E> getIndexedList() {
        // return a type of List
        return getSinglyLinkedList();
    }

    /**
     * Returns a data structure that implements an positional list
     * 
     * @param <E>
     *            - the element type
     * @return a positional list
     */
    public static <E> PositionalList<E> getPositionalList() {
        return getPositionalLinkedList();
    }

    /**
     * Returns a comparison based sorter that uses a specified Comparator
     * 
     * @param <E>
     *            - the element type
     * @param comparator comparator
     * @return a comparison based sorter
     */
    public static <E extends Comparable<E>> Sorter<E> getComparisonSorter(Comparator<E> comparator) {
        // return a type of Sorter
        return getQuickSorter(comparator);
    }
    /**
     * Returns a non-comparison based sorter
     * 
     * @param <E>
     *            - the element type
     * @return a non-comparison based sorter
     */
    public static <E extends Identifiable> Sorter<E> getNonComparisonSorter() {
        // return a type of Sorter
        return getCountingSorter();
    }

    /**
     * Returns a data structure that implements a stack
     * 
     * @param <E>
     *            - the element type
     * @return a stack
     */
    public static <E> Stack<E> getStack() {
        return getLinkedStack();
    }

    /**
     * Returns a data structure that implements a queue
     * 
     * @param <E>
     *            - the element type
     * @return a stack
     */
    public static <E> Queue<E> getQueue() {
        return getArrayBasedQueue();
    }

    /**
     * Returns a skip list map
     * 
     * @param comparator a comparator to use when ordering entries by key
     * @return a skip list map
     */
    private static <K extends Comparable<K>, V> SkipListMap<K, V> getSkipListMap(Comparator<K> comparator) {
        return new SkipListMap<K, V>(comparator);
    }

    /**
     * Returns a singly linked list with front pointer
     * 
     * @return a singly linked list with front pointer
     */
    private static <E> SinglyLinkedList<E> getSinglyLinkedList() {
        return new SinglyLinkedList<E>();
    }

    /**
     * Returns a positional linked list with a front pointer
     * 
     * @return a positional linked list with a front pointer
     */
    private static <E> PositionalLinkedList<E> getPositionalLinkedList() {
        return new PositionalLinkedList<E>();
    }
    
    /**
     * Returns a quicksorter
     * 
     * @param comparator a comparator to use when sorting
     * @return a quicksorter
     */
    private static <E extends Comparable<E>> Sorter<E> getQuickSorter(Comparator<E> comparator) {
        return new QuickSorter<E>(comparator);
    }
    
    /**
     * Returns a counting sorter
     * 
     * @return a counting sorter
     */
    private static <E extends Identifiable> Sorter<E> getCountingSorter() {
        return new CountingSorter<E>();
    }


    /**
     * Returns a linked stack
     * 
     * @return a linked stack
     */
    private static <E> LinkedStack<E> getLinkedStack() {
        return new LinkedStack<E>();
    }

    /**
     * Returns a linked queue
     * 
     * @return a linked queue
     */
    private static <E> ArrayBasedQueue<E> getArrayBasedQueue() {
        return new ArrayBasedQueue<E>();
    }
}