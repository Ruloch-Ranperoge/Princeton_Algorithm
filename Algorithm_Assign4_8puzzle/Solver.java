import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private boolean solvebale;
    private final int cnt;
    private List<Board> sol;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial board is null");
        }
        Comparator<Board> manhattanComparator = new Comparator<Board>() {
            @Override
            public int compare(final Board b1, final Board b2) {
                return b1.manhattan() - b2.manhattan();
            }
        };
        MinPQ<Board> qMain = new MinPQ<>(manhattanComparator);
        MinPQ<Board> qSub = new MinPQ<>(manhattanComparator);

        qMain.insert(initial);
        qSub.insert(initial.twin());
        Board searchNodeMain;
        Board searchNodeSub;
        Board prevNodeMain = null;
        Board prevNodeSub = null;
        cnt = -1;
        List<Board> solMain = new ArrayList<>();
        List<Board> solSub = new ArrayList<>();
        while (!qMain.isEmpty() && !qSub.isEmpty()) {
            searchNodeMain = qMain.delMin();
            searchNodeSub = qSub.delMin();
            solMain.add(searchNodeMain);
            solSub.add(searchNodeSub);
            // System.out.println(searchNodeMain.toString());
            cnt++;
            if (searchNodeMain.isGoal()) {
                solvebale = true;
                sol = solMain;
                // System.out.println("Main solvable");
                break;
            }
            if (searchNodeSub.isGoal()) {
                solvebale = false;
                sol = solSub;
                // System.out.println("Sub solvable");
                break;
            }
            for (Board neigh:searchNodeMain.neighbors()) {
                if (neigh.equals(prevNodeMain)) continue;
                qMain.insert(neigh);
            }
            for (Board neigh:searchNodeSub.neighbors()) {
                if (neigh.equals(prevNodeSub)) continue;
                qSub.insert(neigh);
            }
            prevNodeMain = searchNodeMain;
            prevNodeSub = searchNodeSub;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvebale;
    }

    // min number of moves to solve initial board
    public int moves() {
        return cnt;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return sol;
    }

    // test client (see below)
    public static void main(String[] args){
        System.out.println("Solver main");
    }

}