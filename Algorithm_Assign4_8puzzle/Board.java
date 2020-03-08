import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[][] mat;
    private final int n;
    private final int hammingDis;
    private final int manhattanDis;
    private final int zeroIdxX;
    private final int zeroIdxY;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        if (n < 2) {
            throw new IllegalArgumentException("Array size should be greater than 1");
        }
        if (n != tiles[0].length) {
            throw new IllegalArgumentException("Row should be equal to column.");
        }

        mat = new int[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, mat[i], 0, tiles[i].length);
        }

        int tmpHammingDis = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] != 0 && i * n + j + 1 != mat[i][j])
                    tmpHammingDis++;
            }
        }
        hammingDis = tmpHammingDis;

        int tmpTarX, tmpTarY;
        int tmpManhattanDis = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) continue;
                tmpTarX = (mat[i][j] - 1) / n;
                tmpTarY = (mat[i][j] - 1) % n;
                tmpManhattanDis += Math.abs(tmpTarX - i) + Math.abs(tmpTarY - j);
            }
        }
        manhattanDis = tmpManhattanDis;

        int tmpZeroIdxX = 0;
        int tmpZeroIdxY = 0;

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (mat[i][j] == 0) {
                    tmpZeroIdxX = i;
                    tmpZeroIdxY = j;
                }

        zeroIdxX = tmpZeroIdxX;
        zeroIdxY = tmpZeroIdxY;
    }

    // string representation of this board
    public String toString() {
        StringBuilder expBuffer = new StringBuilder();
        expBuffer.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                expBuffer.append(" ").append(mat[i][j]);
            expBuffer.append("\n");
        }

        return expBuffer.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDis;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDis;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (mat[i][j] != 0 && i * n + j + 1 != mat[i][j])
                    return false;
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board tmpY;
        if (y.getClass() == Board.class)
            tmpY = (Board) y;
        else
            return false;
        if (n != tmpY.dimension())
            return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (mat[i][j] != tmpY.mat[i][j])
                    return false;
        return true;
    }

    private boolean inBound(int x, int y) {
        return x < n && x >= 0 && y < n && y >= 0;
    }
    private void exchangeElement(int x1, int y1, int x2, int y2) {
        int tmp = mat[x1][y1];
        mat[x1][y1] = mat[x2][y2];
        mat[x2][y2] = tmp;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighborBoards = new ArrayList<>();
        int tmpX, tmpY;
        int [] tmpXs = { zeroIdxX - 1, zeroIdxX, zeroIdxX + 1, zeroIdxX };
        int [] tmpYs = { zeroIdxY, zeroIdxY + 1, zeroIdxY, zeroIdxY - 1 };
        for (int i = 0; i < tmpXs.length; i++) {
            tmpX = tmpXs[i];
            tmpY = tmpYs[i];
            if (inBound(tmpX, tmpY)) {
                exchangeElement(zeroIdxX, zeroIdxY, tmpX, tmpY);
                neighborBoards.add(new Board(mat));
                exchangeElement(zeroIdxX, zeroIdxY, tmpX, tmpY);
            }
        }
        return neighborBoards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x1 = 0, y1 = 0;
        int x2 = 0, y2 = 0;
        int cnt = 0;
        int x, y;
        for (int i = 0; i < n * n; i++) {
           x = i / n;
           y = i % n;
           if (cnt == 0 && mat[x][y] != 0) {
               x1 = x;
               y1 = y;
               cnt = 1;
           }
           else if (cnt == 1 && mat[x][y] != 0) {
               x2 = x;
               y2 = y;
               cnt = 2;
           }
           else if (cnt == 2) break;
        }
        exchangeElement(x1, y1, x2, y2);
        Board tmp = new Board(mat);
        exchangeElement(x1, y1, x2, y2);
        return tmp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        System.out.println("Board Unitest");
        int [][] testMat = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        int [][] testMat2 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board1 = new Board(testMat);
        Board board2 = new Board(testMat2);
        System.out.println("Dimension:" + board1.dimension());
        System.out.println("Expression:\n" + board1.toString());
        System.out.println("Hamming:" + board1.hamming());
        System.out.println("Manhattan:" + board1.manhattan());
        System.out.println("Board1 IsGoal:" + board1.isGoal());
        System.out.println("Board2 IsGoal:" + board2.isGoal());
        System.out.println("Board1 == Board2:" + board1.equals(board2));
        System.out.println("Board1 twin:\n" + board1.twin().toString());

    }

}