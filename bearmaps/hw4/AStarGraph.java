package bearmaps.hw4;

import java.util.List;

/**
 * Represents a graph of vertices.
 * Created by hug.
 */
public interface AStarGraph<Vertex> {
    /* provides a list of all edges that go out from the given vertex arg to its neighbors. */
    List<WeightedEdge<Vertex>> neighbors(Vertex v);

    /* provides an estimate of the number of moves to reach the goal from the start position.
    * this estimate must be less than or equal to the correct distance. */
    double estimatedDistanceToGoal(Vertex s, Vertex goal);
}
