import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {
    private boolean solvebale;
    private final int cnt;
    private List<Board> sol;

    private class SearchNode {
        private final int moves;
        private final int priority;
        private Board board;
        private SearchNode prev;

        public SearchNode(int movesPara, Board boardPara, SearchNode prevPara) {
            moves = movesPara;
            board = boardPara;
            priority = moves + board.manhattan();
            prev = prevPara;
        }

        public int getPriority() {
            return priority;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPrev() {
            return prev;
        }

        public Iterable<SearchNode> getNeighborNodes() {
            List<SearchNode> res = new ArrayList<>();
            for (Board neigh:board.neighbors()) {
                if ( prev == null || !neigh.equals(prev.getBoard())) {
                    res.add(new SearchNode(moves + 1, neigh, this));
                }
            }
            return res;
        }
    }
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        int tmpCnt = -1;
        if (initial == null) {
            throw new IllegalArgumentException("initial board is null");
        }
        Comparator<SearchNode> manhattanComparator = new Comparator<SearchNode>() {
            @Override
            public int compare(final SearchNode b1, final SearchNode b2) {
                return b1.getPriority() - b2.getPriority();
            }
        };
        MinPQ<SearchNode> qMain = new MinPQ<>(manhattanComparator);
        MinPQ<SearchNode> qSub = new MinPQ<>(manhattanComparator);

        qMain.insert(new SearchNode(0, initial, null));
        qSub.insert(new SearchNode(0, initial.twin(), null));
        SearchNode searchNodeMain;
        SearchNode searchNodeSub;
        sol = new ArrayList<>();

        while (!qMain.isEmpty() && !qSub.isEmpty()) {
            searchNodeMain = qMain.delMin();
            searchNodeSub = qSub.delMin();
            // System.out.println(searchNodeMain.toString());
            if (searchNodeMain.getBoard().isGoal()) {
                solvebale = true;
                SearchNode tmpNode = searchNodeMain;
                while (tmpNode != null) {
                    tmpCnt++;
                    sol.add(tmpNode.getBoard());
                    tmpNode = tmpNode.getPrev();
                }
                // System.out.println("Main solvable");
                break;
            }
            if (searchNodeSub.getBoard().isGoal()) {
                solvebale = false;
                SearchNode tmpNode = searchNodeSub;
                while (tmpNode != null) {
                    tmpCnt++;
                    sol.add(tmpNode.getBoard());
                    tmpNode = tmpNode.getPrev();
                }
                // System.out.println("Sub solvable");
                break;
            }
            for (SearchNode neigh:searchNodeMain.getNeighborNodes()) {
                // System.out.println(neigh.getPrev().getBoard().toString());
                qMain.insert(neigh);
            }
            for (SearchNode neigh:searchNodeSub.getNeighborNodes()) {
                qSub.insert(neigh);
            }
        }

        cnt = tmpCnt;
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
    public static void main(String[] args) {
        System.out.println("Solver main");
    }

}