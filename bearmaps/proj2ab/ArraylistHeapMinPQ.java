package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * ArrayList-based implementation of the ExtinsicMinPQ
 */
public class ArraylistHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private List<Node> minPQ;
    private HashMap<T, Integer> map;

    /* class that is nested and non-static */
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

    /* an empty constructor of ArraylistHeapMinPQ */
    public ArraylistHeapMinPQ() {
        minPQ = new ArrayList<>();
        map = new HashMap<>();
        minPQ.add(null); // the arraylist's size will increment by 1, the size of the priority queue will be 1-1=0!
    }

    @Override
    public int size() {
        return minPQ.size() - 1; // minus the null object, we use 'this.size()' inside this class to denote the difference between methods that cause ambiguity.
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item already exists.");
        }
        Node inserted = new Node(item, priority);
        minPQ.add(inserted);
        map.put(inserted.getItem(), this.size()); // insert new node at the end of the queue, size() will return the result of "minPQ.size()-1", more concisely, the arraylist.size() - 1 == the priority queue's correct size!
        swim(this.size());
    }

    private void swim(int i) {
        int parent = i / 2;
        if (parent >= 1 && isLess(i, parent)) {
            swap(i, parent);
            swim(parent);
        }
    }

    private void swap(int i, int j) {
        Node inode = minPQ.get(i);
        Node jnode = minPQ.get(j);
        minPQ.set(i, jnode);
        minPQ.set(j, inode);
        map.replace(inode.getItem(), j);
        map.replace(jnode.getItem(), i);
    }

    private boolean isLess(int i, int j) {
        return (minPQ.get(i).compareTo(minPQ.get(j))) < 0;
    }

    @Override
    public T getSmallest() {
        if (isEmpty()) {
            throw new NoSuchElementException("No such items exist.");
        }
        return minPQ.get(1).getItem();
    }

    boolean isEmpty() {
        return this.size() == 0; // this.size() will return the size of the ArraylistHeapMinPQ class instance, not the size of the arraylist object!
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item);
    }

    @Override
    public T removeSmallest() {
        if (isEmpty()) {
            throw new NoSuchElementException("No such items exist.");
        }
        T smallest = getSmallest();
        swap(1, this.size());
        minPQ.remove(this.size());
        map.remove(smallest);
        sink(1);
        return smallest;
    }

    private void sink(int i) {
        int left = i * 2;
        if (left > this.size()) {
            return;
        }
        if (left + 1 <= this.size() && isLess(left + 1, left)) {
            left += 1; // turn to right childIndex
        } // otherwise, we stick to the left childIndex
        if (isLess(i, left)) {
            return;
        }
        swap(i, left);
        sink(left);
    }

    @Override
    public void changePriority(T item, double priority) {
        if (! contains(item)) {
            throw new NoSuchElementException("Item does not exist.");
        }
        int index = map.get(item);
        Node target = minPQ.get(index);
        double oldPriority = target.getPriority();
        target.setPriority(priority);
        if (oldPriority < priority) {
            sink(index);
        } else if (oldPriority > priority) {
            swim(index);
        } else {
            return;
        }
    }

}
