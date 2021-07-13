package bearmaps.hw4.test;

import bearmaps.hw4.AStarGraph;
import bearmaps.hw4.WeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* The Integer Hop puzzle implemented as a graph.
* created by hug. */
public class IntegerHopGraph implements AStarGraph<Integer> {

    @Override
    public List<WeightedEdge<Integer>> neighbors(Integer v) {
        List<WeightedEdge<Integer>> neighbors = new ArrayList<>();
        WeightedEdge<Integer> edge1 = new WeightedEdge<>(v, v * v, 10);
        WeightedEdge<Integer> edge2 = new WeightedEdge<>(v, v * 2, 5);
        WeightedEdge<Integer> edge3 = new WeightedEdge<>(v, v / 2, 5);
        WeightedEdge<Integer> edge4 = new WeightedEdge<>(v, v - 1, 1);
        WeightedEdge<Integer> edge5 = new WeightedEdge<>(v, v + 1, 1);
        Collections.addAll(neighbors, edge1, edge2, edge3, edge4, edge5);
        return neighbors;
    }

    @Override
    public double estimatedDistanceToGoal(Integer s, Integer goal) {
        return 0;
    }
}
