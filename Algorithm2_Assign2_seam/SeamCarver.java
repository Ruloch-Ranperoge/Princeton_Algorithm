/* *****************************************************************************
 *  Name: SeamCarver
 *  Date: 2020-04-14
 *  Description: Assignment
 **************************************************************************** */


import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private double [][] energyMat;
    private Color [][] colorMat;
    private int h;
    private int w;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        pic = picture;
        h = pic.height();
        w = pic.width();
        energyMat = new double[h][w];
        colorMat = new Color[h][w];
        // Load picture to color matrix
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                colorMat[i][j] = pic.get(j, i);
            }
        }
        computeEnergy();
    }
    private void computeEnergy() {
        // Compute energy mat
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (i == 0 || j == 0 || i == h - 1 || j == w - 1) {
                    energyMat[i][j] = 1000.0;
                }
                else {
                    energyMat[i][j] = Math.sqrt(
                            colorDistance(colorMat[i - 1][j], colorMat[i + 1][j]) +
                                    colorDistance(colorMat[i][j - 1], colorMat[i][j + 1])
                    );
                }
            }
        }
    }
    private double colorDistance(Color c1, Color c2) {
        return Math.pow(c1.getRed() - c2.getRed(), 2) +
                Math.pow(c1.getGreen() - c2.getGreen(), 2) +
                Math.pow(c1.getBlue() - c2.getBlue(), 2);
    }
    private EdgeWeightedDigraph getVerticalGraph() {
        // 0: start
        // 1 2 3 4 ... w
        // w+1 w+2 w+3 ... 2w
        // (h-1)*w+1 (h-1)*w+2 ... h*w
        // h*w + 1: end
        EdgeWeightedDigraph gg = new EdgeWeightedDigraph(h * w + 2);
        // Add edge from start to row 1
        for (int j = 0; j < w; j++) gg.addEdge(new DirectedEdge(0, j + 1, energyMat[0][j]));

        // Add edge from row i to row j
        for (int i = 0; i < h - 1; i++) {
            for (int j = 0; j < w; j++) {
                // middle down
                gg.addEdge(new DirectedEdge(i * w + j + 1, (i + 1) * w + j + 1, energyMat[i + 1][j]));
                // left down
                if (j > 0) {
                    gg.addEdge(new DirectedEdge(i * w + j + 1, (i + 1) * w + j, energyMat[i + 1][j - 1]));
                }
                // right down
                if (j < w - 1) {
                    gg.addEdge(new DirectedEdge(i * w + j + 1, (i + 1) * w + j + 2, energyMat[i + 1][j + 1]));
                }
            }
        }

        // Add edge from last row to end
        for (int j = 0; j < w; j++) gg.addEdge(new DirectedEdge((h - 1) * w + j + 1, h * w + 1, 0.0));
        return gg;
    }

    private EdgeWeightedDigraph getHorizontalGraph() {
        // 0: start
        // 1 2 3 4 ... w
        // w+1 w+2 w+3 ... 2w
        // (h-1)*w+1 (h-1)*w+2 ... h*w
        // h*w + 1: end
        EdgeWeightedDigraph gg = new EdgeWeightedDigraph(h * w + 2);
        // Add edge from start to col 1
        for (int i = 0; i < h; i++) gg.addEdge(new DirectedEdge(0, i * w + 1, energyMat[i][0]));

        // Add edge from col i to col j
        for (int j = 0; j < w - 1; j++) {
            for (int i = 0; i < h; i++) {
                // middle right
                gg.addEdge(new DirectedEdge(i * w + j + 1, i * w + j + 2, energyMat[i][j + 1]));
                // upper right
                if (i > 0) {
                    gg.addEdge(new DirectedEdge(i * w + j + 1, (i - 1) * w + j + 2, energyMat[i - 1][j + 1]));
                }
                // down right
                if (i < h - 1) {
                    gg.addEdge(new DirectedEdge(i * w + j + 1, (i + 1) * w + j + 2, energyMat[i + 1][j + 1]));
                }
            }
        }

        // Add edge from last col to end
        for (int i = 0; i < h; i++) gg.addEdge(new DirectedEdge(i * w + w, h * w + 1, 0.0));
        return gg;
    }
    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return w;
    }

    // height of current picture
    public int height() {
        return h;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= pic.width() || y  >= pic.height()) throw new IllegalArgumentException();
        return energyMat[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int [] seam = new int[w];
        EdgeWeightedDigraph g = getHorizontalGraph();
        DijkstraSP sp = new DijkstraSP(g, 0);
        for (DirectedEdge edge:sp.pathTo(h * w + 1)) {
            int idx = edge.to();
            int j = (idx - 1) % w;
            int i = (idx - 1 - j) / w;
            if (i < h) {
                seam[j] = i;
            }
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int [] seam = new int[h];
        EdgeWeightedDigraph g = getVerticalGraph();
        DijkstraSP sp = new DijkstraSP(g, 0);
        for (DirectedEdge edge:sp.pathTo(h * w + 1)) {
            int idx = edge.to();
            int j = (idx - 1) % w;
            int i = (idx - 1 - j) / w;
            if (i < h) {
                seam[i] = j;
            }
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // check parameter
        // null input
        if (seam == null) throw new IllegalArgumentException();
        // check length
        if (seam.length != w) throw new IllegalArgumentException();
        // check indexes
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= h) throw new IllegalArgumentException();
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        if (h <= 1) throw new IllegalArgumentException();
        // update Color matrix
        for (int j = 0; j < w; j++) {
            for (int i = seam[j]; i < h - 1; i++) {
                // shift up
                colorMat[i][j] = colorMat[i + 1][j];
            }
        }
        // update h, w
        h -= 1;
        // update Energy matrix
        computeEnergy();
        pic = new Picture(w, h);
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                pic.set(j, i, colorMat[i][j]);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // check parameter
        // null input
        if (seam == null) throw new IllegalArgumentException();
        // check length
        if (seam.length != h) throw new IllegalArgumentException();
        // check indexes
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= w) throw new IllegalArgumentException();
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) throw new IllegalArgumentException();
        }
        if (w <= 1) throw new IllegalArgumentException();
        // update Color matrix
        for (int i = 0; i < h; i++) {
            for (int j = seam[i]; j < w - 1; j++) {
                // shift left
                colorMat[i][j] = colorMat[i][j + 1];
            }
        }
        // update h, w
        w -= 1;
        // update Energy matrix
        computeEnergy();
        pic = new Picture(w, h);
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                pic.set(j, i, colorMat[i][j]);
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }

}
