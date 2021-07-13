package bearmaps.hw4.test;

import bearmaps.hw4.AStarSolver;
import bearmaps.hw4.ShortestPathsSolver;

/**
 * Showcases how the AStarSolver can be used for solving sliding puzzles.
 * NOTE: YOU MUST REPLACE LazySolver WITH AStarSolver OR THIS DEMO WON'T WORK!
 * Created by hug.
 */
public class DemoSlidingPuzzleSolution {
    public static void main(String[] args) {
        Board start = Board.readBoard("BasicPuzzle4.txt");
        System.out.println(start);
        int N = start.size();
        Board goal = Board.solved(N);

        BoardGraph spg = new BoardGraph();

        ShortestPathsSolver<Board> solver = new AStarSolver<>(spg, start, goal, 20);
        SolutionPrinter.summarizeSolution(solver, "\n");
    }
}
