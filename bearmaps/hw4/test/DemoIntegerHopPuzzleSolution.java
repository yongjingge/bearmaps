package bearmaps.hw4.test;

import bearmaps.hw4.AStarSolver;
import bearmaps.hw4.ShortestPathsSolver;

/**
 * Showcases how the AStarSolver can be used for solving integer hop puzzles.
 * NOTE: YOU MUST REPLACE LazySolver WITH AStarSolver OR THIS DEMO WON'T WORK!
 * Created by hug.
 */
public class DemoIntegerHopPuzzleSolution {

    public static void main(String[] args) {
        int start = 254;
        int goal = 4;

        IntegerHopGraph ihg = new IntegerHopGraph();

        ShortestPathsSolver<Integer> mySolver = new AStarSolver<>(ihg, start, goal, 10);
        SolutionPrinter.summarizeSolution(mySolver, "=> ");
    }

}
