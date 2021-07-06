package bearmaps.proj2ab;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/* test the result generated using two different 'nearest' methods */
public class NearestPointTest {

    @Test
    public void testNearest() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        Point p4 = new Point(11.3, 6);

        KdTree kd = new KdTree(List.of(p1, p2, p3, p4));
        NaivePointSet naiveSet = new NaivePointSet(List.of(p1, p2, p3, p4));
        WeirdPointSet weirdSet = new WeirdPointSet(List.of(p1, p2, p3, p4));

        Point nearestKD = kd.nearest(-1.0, 4.0);
        Point nearestNaive = naiveSet.nearest(-1.0, 4.0);
        Point nearestWeird = weirdSet.nearest(-1.0, 4.0);
        assertTrue(nearestKD.equals(nearestNaive));
        assertEquals(p3, nearestKD);
        assertEquals(p3, nearestWeird);

        nearestKD = kd.nearest(10.3, 5.7);
        nearestNaive = naiveSet.nearest(10.3, 5.7);
        nearestWeird = weirdSet.nearest(10.3, 5.7);
        assertTrue(nearestKD.equals(nearestNaive));
        assertEquals(p4, nearestNaive);
        assertEquals(p4, nearestWeird);
    }
}
