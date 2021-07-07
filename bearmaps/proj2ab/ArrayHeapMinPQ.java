package bearmaps.proj2ab;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of the ExtrinsicMinPQ
 *
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private Node[] minPQ;
    private int size;
    private HashMap<T, Integer> map; // record a mapping from T item to index
    private static final double RATIO = 0.25;
    private static final int INIT_CAPACITY = 16;

    /* class that is nested and non-static, it will be a bit more complex if we set it to an inner class. */
    private class Node implements Comparable<Node> {
        T item;
        double priority;
        Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        public T getItem() {
            return item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        public double getPriority() {
            return priority;
        }

        public void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(Node that) {
            return Double.compare(this.priority, that.priority);
        }

        @Override
        public int hashCode() {
            return this.item.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            Node that = (Node) o;
            return this.item.equals(that.item);
        }

    }

    /* an empty constructor of ArrayHeapMinPQ */
    public ArrayHeapMinPQ() {
        minPQ = new ArrayHeapMinPQ.Node[INIT_CAPACITY];
        minPQ[0] = null;
        size = 0;
        map = new HashMap<T, Integer>(INIT_CAPACITY);
    }

    public int size() {
        return size;
    }

    private static int leftIndex(int x) {
        return x * 2;
    }

    private static int rightIndex(int x) {
        return x * 2 + 1;
    }

    private static int parentIndex(int x) {
        return x / 2;
    }

    private boolean inBounds(int x) {
        if (x > size || x < 1) {
            return false;
        }
        return true;
    }

    private Node getNode(int index) {
        if (! inBounds(index)) {
            return null;
        }
        return minPQ[index];
    }

    /* swap nodes at two given indices */
    private void swap(int i, int j) {
        Node inode = getNode(i);
        Node jnode = getNode(j);
        minPQ[i] = jnode;
        minPQ[j] = inode;
        map.replace(inode.getItem(), j);
        map.replace(jnode.getItem(), i);
    }

    /* return index of the Node with smaller priority of its own.
    * not both nodes are null. */
    private int getMinIndex(int i, int j) {
        Node inode = getNode(i);
        Node jnode = getNode(j);
        if (inode == null) {
            return j;
        } else if (jnode == null) {
            return i;
        } else if (inode.compareTo(jnode) < 0) {
            return i;
        } else {
            return j;
        }
    }

    /* resize the minPQ with given capacity */
    private void resize(int capacity) {
        Node[] tmp = new ArrayHeapMinPQ.Node[capacity];
        for (int i = 1; i < this.minPQ.length; i += 1) {
            tmp[i] = this.minPQ[i];
        }
        this.minPQ = tmp;
    }

    /* throw exceptions if the given argument index is invalid for swim/sink operations. */
    private void validateSinkSwimArg(int index) {
        if (! inBounds(index)) {
            throw new IllegalArgumentException("Cannot sink or swim a node with invalid index.");
        }
        if (getNode(index) == null) {
            throw new IllegalArgumentException("Cannot sink or swim a null node.");
        }
    }

    /* Insertion step 2
    * swim: swim up inserted node until it is larger than its parent node,
    * or until it is the new root. */
    private void swim(int i) {
        validateSinkSwimArg(i);
        int parent = parentIndex(i);
        /* as long as the inserted node is smaller than its parent,
        * it should be swam up to its parent's position. */
        if (parent >= 1 && getMinIndex(i, parent) == i) {
            swap(i, parent);
            swim(parent); // recursion
        }
    }

    /* DeleteMin step 2
    * sink: sink down the node at given index until it is smaller than both of its children. */
    private void sink(int i) {
        validateSinkSwimArg(i);
        int leftIndex = leftIndex(i);
        int rightIndex = rightIndex(i);
        if (leftIndex > size) {
            return;
        }
        // when left child node is smaller than the node at given index
        if (getMinIndex(i, leftIndex) == leftIndex) {
            if (rightIndex <= size && getMinIndex(leftIndex, rightIndex) == rightIndex) {
                //swap with the right child
                swap(i, rightIndex);
                sink(rightIndex);
            } else {
                // swap with the left child
                swap(i, leftIndex);
                sink(leftIndex);
            }
        } else { // if node at given index is smaller than its left child, we check its right child
            if (rightIndex <= size && getMinIndex(i, rightIndex) == rightIndex) {
                swap(i, rightIndex);
                sink(rightIndex);
            }
            // if node at given index is also smaller than its right child, we do nothing
        }
    }

    /* insert an item with given priority value.
    * given T argument item is never null. */
    @Override
    public void add(T item, double priority) {

        if (contains(item)) {
            throw new IllegalArgumentException("Item already exists.");
        }

        /* expand the minPQ if it is full */
        if (size + 1 == minPQ.length) {
            resize(minPQ.length * 2);
        }
        Node inserted = new Node(item, priority);
        int insertedIndex = size + 1; // initially add new item into the end of the heap
        minPQ[insertedIndex] = inserted;
        size += 1;
        map.put(inserted.getItem(), insertedIndex); // update the map!
        swim(insertedIndex);
    }

    /* return true if the heap is empty. */
    boolean isEmpty() {
        return size() == 0;
    }

    /* return the item with smallest priority. If no such items exist,
    * throw a NoSuchElementException. */
    @Override
    public T getSmallest() {
        if (isEmpty()) {
            throw new NoSuchElementException("No such items exist.");
        }
        return getNode(1).getItem();
    }

    /* return true if minPQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    /* remove and return the item with smallest priority. If no such items exist, throw an exception. */
    @Override
    public T removeSmallest() {
        T smallest = getSmallest();
        swap(1, size); // swap the smallest node with the last node in the minPQ
        // smallest one should be removed, after swapping, smallest item is at the end
        minPQ[size] = null;
        size -= 1;
        map.remove(smallest);
        sink(1); // sink the newly swapped node at index 1

        // consider need to resize
        if ((double) size / minPQ.length < RATIO && minPQ.length > INIT_CAPACITY) {
            resize(minPQ.length / 2);
        }

        return smallest;
    }

    /* set the priority of the given item to the given value. */
    @Override
    public void changePriority(T item, double priority) {
        if (! map.containsKey(item)) {
            throw new NoSuchElementException("Item does not exist.");
        }
        // get the index of the Node with given item
        int index = map.get(item);
        Node target = getNode(index);
        double oldPriority = target.getPriority();
        target.setPriority(priority);
        if (oldPriority < priority) {
            // if new priority is greater than the old one, we need to sink down the node
            sink(index);
        } else if (oldPriority > priority) {
            // if new priority is smaller than the old one, we need to swim up the node
            swim(index);
        } else {
            // if new priority is equal to the old one, we do nothing and return
            return;
        }
    }

}
