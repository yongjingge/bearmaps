package bearmaps.hw4;

import bearmaps.proj2ab.ArraylistHeapMinPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

/* This class provides a shortest-path solver that implements the A* algorithm which reflects
* three improvements. */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private SolverOutcome outcome;
    private List<Vertex> solution;
    private int numStatesExplored;
    private double timeSpent;
    private double solutionWeight;

    private ExtrinsicMinPQ<Vertex> fringe;
    private List<WeightedEdge<Vertex>> neighbors;
    private Map<Vertex, Double> distTo;
    private Map<Vertex, Vertex> edgeTo;

    /* constructor which finds the solution */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        solution = new ArrayList<>();
        fringe = new ArraylistHeapMinPQ<>();
        neighbors = new ArrayList<>(); // a list of adjacent edges of the given Vertex
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        distTo.put(start, 0.0);
        edgeTo.put(start, null);
        fringe.add(start, distTo.get(start) + input.estimatedDistanceToGoal(start, end));
        numStatesExplored = 0;

        Stopwatch sw = new Stopwatch();
        boolean isEmpty = fringe.size() == 0;
        boolean reachesGoal = fringe.getSmallest().equals(end);
        boolean timeOut = sw.elapsedTime() >= timeout;

        while(!isEmpty && !reachesGoal && !timeOut) {
            Vertex cur = fringe.removeSmallest();
            numStatesExplored += 1;
            neighbors = input.neighbors(cur);
            for (WeightedEdge<Vertex> edge : neighbors) {
                relax(edge, input, end);
            }
            isEmpty = fringe.size() == 0;
        }
        timeSpent = sw.elapsedTime();

        if (fringe.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
        } else if (reachesGoal) {
            outcome = SolverOutcome.SOLVED;
            Vertex cur = fringe.getSmallest();
            solutionWeight = distTo.get(cur);
            while(! cur.equals(start)) {
                solution.add(cur);
                cur = edgeTo.get(cur);
            }
            solution.add(start);
            Collections.reverse(solution);
        } else {
            outcome = SolverOutcome.TIMEOUT;
        }
    }

    private void relax(WeightedEdge<Vertex> edge, AStarGraph<Vertex> input, Vertex end) {
        // edge: from v to w
        Vertex v = edge.from();
        Vertex w = edge.to();
        double vwWeight = edge.weight();
        double throughVtoW = distTo.get(v) + vwWeight;

        if (distTo.containsKey(w)) {
            if (distTo.get(w) < throughVtoW) {
                return;
            }
            distTo.replace(w, throughVtoW);
            edgeTo.replace(w, v);
        } else {
            distTo.put(w, throughVtoW);
            edgeTo.put(w, v);
        }
        if (fringe.contains(w)) {
            fringe.changePriority(w, distTo.get(w) + input.estimatedDistanceToGoal(w, end));
        } else {
            fringe.add(w, distTo.get(w) + input.estimatedDistanceToGoal(w, end));
        }
    }

    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    /* return a list of vertices corresponding to a solution */
    @Override
    public List<Vertex> solution() {
        return solution;
    }

    /* total weight of the given solution */
    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    /* total number of pq dequeue operations */
    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    /* total time spent in seconds by the constructor */
    @Override
    public double explorationTime() {
        return timeSpent;
    }
}
