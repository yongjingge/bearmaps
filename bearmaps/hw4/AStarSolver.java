package bearmaps.hw4;

import bearmaps.hw4.test.WeightedDirectedGraph;
import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.ArraylistHeapMinPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

/* This class provides a shortest-path solver that implements the A* algorithm which reflects
* three improvements. */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private List<Vertex> solution;
    private double solutionWeight;
    private int numDequeued;
    private double timeSpent;
    private ExtrinsicMinPQ<Vertex> fringe;
    private Map<Vertex, Double> distTo;
    private Map<Vertex, Vertex> edgeTo;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {

        solution = new ArrayList<>();
        fringe = new ArraylistHeapMinPQ<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        Stopwatch sw = new Stopwatch();

        distTo.put(start, 0.0);
        edgeTo.put(start, null);
        numDequeued = 0;
        fringe.add(start, input.estimatedDistanceToGoal(start, end));

        while(fringe.size() != 0) {
            if (fringe.getSmallest().equals(end)) {
                outcome = SolverOutcome.SOLVED;
                Vertex cur = fringe.getSmallest();
                solutionWeight = distTo.get(cur);
                while(! cur.equals(start)) {
                    solution.add(cur);
                    cur = edgeTo.get(cur);
                }
                solution.add(start);
                Collections.reverse(solution);
                return;
            }
            Vertex cur = fringe.removeSmallest();
            numDequeued += 1;
            for (WeightedEdge<Vertex> edge : input.neighbors(cur)) {
                relax(edge, input, end);
            }
            timeSpent = sw.elapsedTime();
            if (timeSpent >= timeout) {
                outcome = SolverOutcome.TIMEOUT;
            }
        }
        outcome = SolverOutcome.UNSOLVABLE;
    }

    private void relax(WeightedEdge<Vertex> edge, AStarGraph<Vertex> input, Vertex end) {
        Vertex f = edge.from();
        Vertex t = edge.to();
        double weight = edge.weight();

        if (!distTo.containsKey(t)) {
            distTo.put(t, distTo.get(f) + weight);
            edgeTo.put(t, f);
            fringe.add(t, distTo.get(t) + input.estimatedDistanceToGoal(t, end));
            return;
        }
        if (distTo.get(t) > distTo.get(f) + weight) {
            distTo.replace(t, distTo.get(f) + weight);
            edgeTo.replace(t, f);
            if (fringe.contains(t)) {
                fringe.changePriority(t, distTo.get(t) + input.estimatedDistanceToGoal(t, end));
            } else {
                fringe.add(t, distTo.get(t) + input.estimatedDistanceToGoal(t, end));
            }
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

   @Override
   public double solutionWeight() {
        return solutionWeight;
   }

   @Override
   public int numStatesExplored() {
        return numDequeued;
   }

   @Override
   public double explorationTime() {
        return timeSpent;
   }
}