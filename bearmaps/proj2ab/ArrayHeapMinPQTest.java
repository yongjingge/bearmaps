package bearmaps.proj2ab;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {

    @Rule
    public ExpectedException ex = ExpectedException.none();

    @Test
    public void testGetSmallest() {
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        assertTrue(pq.size() == 0);
        assertTrue(pq.isEmpty());
        pq.add(11, 1);
        pq.add(12, 5);
        pq.add(13, 2);
        pq.add(14, 11);
        pq.add(15, 0);
        assertEquals(5, pq.size());
        assertTrue(pq.getSmallest() == 15);
        pq.add(16,-1);
        assertTrue(pq.getSmallest() == 16);
        assertEquals(6, pq.size());
        assertFalse(pq.isEmpty());
    }

    @Test
    public void testAddDuplicatedItem() {
        ex.expect(IllegalArgumentException.class);
        ex.expectMessage("Item already exists.");

        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        pq.add(11, 1);
        pq.add(12, 5);
        pq.add(13, 2);
        pq.add(14, 11);
        pq.add(15, 0);
        pq.add(12, 3);
    }

    @Test
    public void testRemoveSmallest() {
        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.add("test", 12);
        pq.add("evaluation", 5);
        pq.add("remove", 1);
        pq.add("smallest", 0);
        assertEquals(4, pq.size());
        assertEquals("smallest", pq.removeSmallest());
        assertEquals("remove", pq.getSmallest());
        assertFalse(pq.contains("smallest"));
        pq.add("another", 1);
        assertEquals("remove", pq.getSmallest());
    }

    @Test
    public void testCanNotSinkNull() {
        ex.expect(IllegalArgumentException.class);
        ex.expectMessage("Cannot sink or swim a node with index greater than size.");

        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        pq.add(11, 1);
        pq.add(12, 5);
        pq.add(13, 2);
        pq.removeSmallest();
        pq.removeSmallest();
        pq.removeSmallest(); // all nodes were removed, cannot finish the sink() operation of the last removeSmallest() call
    }

    @Test
    public void testRemoveFromNull() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage("No such items exist.");

        ArrayHeapMinPQ<String> pq = new ArrayHeapMinPQ<>();
        pq.removeSmallest();
    }

    @Test
   public void testChangePriority() {
        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        pq.add(11, 1);
        pq.add(12, 5);
        pq.add(13, 2);
        pq.add(14, 3);
        pq.add(15, 0);
        assertTrue(pq.getSmallest() == 15);
        pq.changePriority(11, -1);
        assertTrue(pq.getSmallest() == 11);
        pq.removeSmallest();
        pq.add(17, -3);
        assertTrue(pq.getSmallest() == 17);
        pq.changePriority(17, 11);
        assertTrue(pq.getSmallest() == 15);
    }

    @Test
    public void testChangePriorityDoesNotExist() {
        ex.expect(NoSuchElementException.class);
        ex.expectMessage("Item does not exist.");

        ArrayHeapMinPQ<Integer> pq = new ArrayHeapMinPQ<>();
        pq.add(11, 1);
        pq.add(12, 5);
        pq.add(13, 2);
        pq.changePriority(14, 3);
    }
}
