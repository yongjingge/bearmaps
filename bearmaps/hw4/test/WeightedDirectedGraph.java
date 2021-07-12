package bearmaps.hw4.test;

import bearmaps.hw4.AStarGraph;
import bearmaps.hw4.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/* Created by hug.
 * @source cs61b_sp19_lecture provided */
public class WeightedDirectedGraph implements AStarGraph<Integer> {

    /* nested class EdgeList */
    private class EdgeList {
        private List<WeightedEdge<Integer>> list;
        private EdgeList() {
            list = new ArrayList<>();
        }
    }

    private EdgeList[] adjacent; // an array of arraylists
    public WeightedDirectedGraph(int V) {
        adjacent = new EdgeList[V];
        for (int i = 0; i < V; i += 1) {
            adjacent[i] = new EdgeList();
        }
    }

    @Override
    public List<WeightedEdge<Integer>> neighbors(Integer v) {
        return adjacent[v].list;
    }

    @Override
    public double estimatedDistanceToGoal(Integer start, Integer end) {
        List<WeightedEdge<Integer>> edges = neighbors(start);
        double estimate = Double.POSITIVE_INFINITY;
        for (WeightedEdge<Integer> e : edges) {
            if (e.weight() < estimate) {
                estimate = e.weight();
            }
        }
        return estimate;
    }

    public void addEdge(int p, int q, double w) {
        WeightedEdge<Integer> e = new WeightedEdge<>(p, q, w);
        adjacent[p].list.add(e);
    }
}
