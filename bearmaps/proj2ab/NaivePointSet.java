package bearmaps.proj2ab;

import java.util.List;

/* this class is a naive linear-time solution to solve
* the problem of finding the closest point to get a given coordinate.
*
* linear-time solution: will traverse the set of Points. */
public class NaivePointSet implements PointSet {

    private List<Point> points;

    /* constructor */
    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    /* returns the point in the set that is nearest to (x, y) Point */
    @Override
    public Point nearest(double x, double y) {
        Point target = new Point(x, y);
        Point nearest = points.get(0);
        double distance = Point.distance(target, nearest);
        for (int i = 1; i < points.size(); i += 1) {
            if (Double.compare(Point.distance(target, points.get(i)), distance) < 0) {
                nearest = points.get(i);
                distance = Point.distance(target, nearest);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet points = new NaivePointSet(List.of(p1, p2, p3));
        Point res = points.nearest(-1.0, 4.0);
        System.out.println("The nearest point to Point(-1.0, 4.0) is:");
        System.out.println(res.getX());
        System.out.println(res.getY());
    }
}
