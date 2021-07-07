package bearmaps.proj2ab;

import java.util.Comparator;
import java.util.List;

public class KdTree implements PointSet {

    private Node root;

    /* nested class Node:
    * we can either add a compareTo method that compare two Node objects,
    * or write a lambda expression outside the Node class to provide a compare method from the outside. */
    private static class Node implements Comparable<Node> {

        Point point; // key
        Node left; // also down side
        Node right; // also up side
        boolean XBased; // initially true

        public Node(Point point) {
            this.point = point;
            this.left = null;
            this.right = null;
            this.XBased = true; // initially based on the x coordinate
        }

        public void changeComparator() {
            XBased = !XBased;
        }

        @Override
        public int compareTo(Node o) {
            if (XBased) {
                return Double.compare(this.point.getX(), o.point.getX());
            } else { // if XBased is false, we will compare based on the y coordinate.
                return Double.compare(this.point.getY(), o.point.getY());
            }
        }
    }

    /* lambda function of comparator */
    public Comparator<Node> intComparator = (Node i, Node j) -> {
        if (i.XBased) {
            return Double.compare(i.point.getX(), j.point.getX());
        } else {
            return Double.compare(i.point.getY(), j.point.getY());
        }
    };

    /* constructor of KdTree */
    public KdTree(List<Point> points) {
        for (Point point : points) {
            Node cur = new Node(point);
            root = insert(root, cur);
        }
    }

    /* insertion of kd tree */
    private Node insert(Node root, Node n) {
        if (root == null) {
            return n;
        }
        if (root.point.equals(n.point)) {
            return root;
        }

        int cmp = intComparator.compare(root, n);
        // int cmp = root.compareTo(n);
        if (cmp > 0) {
            // inserted node is less than the root node
            n.changeComparator(); // initially true, now changed into false, comparison will be conducted from depth 0 level to depth 1 level
            root.left = insert(root.left, n); // when recursively call insert on root.left and n, n will be compared based on its y coordinate due to the 'XBased' attribute is false now
        } else if (cmp < 0) {
            // inserted node is greater than the root node
            n.changeComparator();
            root.right = insert(root.right, n);
        } else {
            // when either the x coordinate or the y coordinate of inserted node is equal to the root node, we treat equal items as 'greater'
            n.changeComparator();
            root.right = insert(root.right, n);
        }
        return root;
    }

    @Override
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        Node best = root;
        return helper(root, goal, best).point;
    }

    private Node helper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        // update the best node information whenever we find a better one
        double bestDistance = distance(goal, best.point);
        double curDistance = distance(goal, n.point);
        if (Double.compare(curDistance, bestDistance) < 0) { // if current distance is less than the best distance
            best = n;
        }

        Node goalNode = new Node(goal);
        Node goodside, badside;
        if (intComparator.compare(n, goalNode) < 0) { // if n is less than goal
            goodside = n.right;
            badside = n.left;
        } else {
            goodside = n.left;
            badside = n.right;
        }
        best = helper(goodside, goal, best);
        if (Math.abs(n.point.getX() - goal.getX()) < bestDistance ||
        Math.abs(n.point.getY() - goal.getY()) < bestDistance) {
            best = helper(badside, goal, best);
        }
        return best;
    }

    private double distance(Point i, Point j) {
        double pow1 = Math.pow(i.getX() - j.getX(), 2);
        double pow2 = Math.pow(i.getY() - j.getY(), 2);
        return Math.sqrt(pow1 + pow2);
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        KdTree kd = new KdTree(List.of(p1, p2, p3));
        Point res = kd.nearest(-1.0, 4.0);
        System.out.println("The nearest point to Point(-1.0, 4.0) is:");
        System.out.println(res.getX());
        System.out.println(res.getY());
    }
}
