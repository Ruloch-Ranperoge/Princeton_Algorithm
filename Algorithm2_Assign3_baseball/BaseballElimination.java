/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaseballElimination {
    private int teamNum;
    private int [] wins;
    private int [] loses;
    private int [] remain;
    private int [][] remainMap;
    private boolean [] eliminated;
    private HashMap<String, Integer> teams;
    private String [] names;
    private List<Set<String>> certs;

    public BaseballElimination(String filename) {
        init(filename);
        compute();
    }

    private void init(String filename) {
        In fin = new In(filename);
        teamNum = Integer.parseInt(fin.readLine());
        // StdOut.print("Team num:" + teamNum + "\n");
        wins = new int[teamNum];
        loses = new int[teamNum];
        remain = new int[teamNum];
        remainMap = new int[teamNum][teamNum];
        eliminated = new boolean[teamNum];
        teams = new HashMap<>();
        names = new String[teamNum];
        certs = new ArrayList<>();
        int cnt = 0;
        while (fin.hasNextLine()) {
            String line = fin.readLine();
            // StdOut.print("Line:" + line + "\n");
            String [] tokens = line.trim().split("\\s+");
            // for (int x = 0; x < tokens.length; x++) StdOut.print(tokens[x] + ", "); StdOut.print("\n");

            // StdOut.print("Team name:" + tokens[0] + "\n");
            teams.put(tokens[0], cnt);
            names[cnt] = tokens[0];
            certs.add(null);
            wins[cnt] = Integer.parseInt(tokens[1]);
            loses[cnt] = Integer.parseInt(tokens[2]);
            remain[cnt] = Integer.parseInt(tokens[3]);
            // StdOut.print("Win:" + tokens[1]);
            // StdOut.print("Lose:" + tokens[2]);
            // StdOut.print("Remain:" + tokens[3]);
            for (int i = 0; i < teamNum; i++) {
                // StdOut.print("Map:" + tokens[i]);
                remainMap[cnt][i] = Integer.parseInt(tokens[i + 4]);
            }
            cnt++;
        }
    }

    // 0(team 0), 1(team 1) ... n-1(team n-1), n(team 0-1), n+1(team 0-2) ... V-2(s), V-1(t)
    private FlowNetwork getFlowNetwork(int i) {
        int matches = (teamNum - 1) * teamNum / 2;
        int vv = teamNum + matches + 2;
        FlowNetwork G = new FlowNetwork(vv);
        int v = 0;
        int w = 1;
        // for all matches
        for (int j = teamNum; j < teamNum + matches; j++) {
            G.addEdge(new FlowEdge(j, v, Double.POSITIVE_INFINITY));
            G.addEdge(new FlowEdge(j, w, Double.POSITIVE_INFINITY));
            // for source node
            if (v != i && w != i) {
                G.addEdge(new FlowEdge(vv - 2, j, remainMap[v][w]));
            }
            v += (w + 1) / teamNum;
            w = ((w + 1) / teamNum > 0) ? v + 1 : w + 1;
        }
        // for terminal node
        for (int j = 0; j < teamNum; j++) {
            if (j != i)
                G.addEdge(new FlowEdge(j, vv - 1, wins[i] + remain[i] - wins[j]));
        }
        return G;
    }

    private int getSumCapacity(int i) {
        int sum = 0;
        for (int v = 0; v < teamNum; v++) {
            for (int w = v + 1; w < teamNum; w++) {
                if (v != i && w != i) {
                    sum += remainMap[v][w];
                }
            }
        }
        return sum;
    }

    private void eliminate(int i) {
        // Trival case
        for (int j = 0; j < teamNum; j++) {
            if (wins[i] + remain[i] < wins[j]) {
                eliminated[i] = true;
                certs.set(i, new HashSet<>(Collections.singletonList(names[j])));
                return;
            }
        }
        // Nontrival case
        FlowNetwork G = getFlowNetwork(i);
        // StdOut.print(G.toString());
        FordFulkerson ff = new FordFulkerson(G, G.V() - 2, G.V() - 1);
        if (ff.value() < getSumCapacity(i)) {
            eliminated[i] = true;
            Set<String> s = new HashSet<>();
            for (int j = 0; j < teamNum; j++) {
                if (j != i && ff.inCut(j)) {
                    s.add(names[j]);
                }
            }
            certs.set(i, s);
        }
    }
    private void compute() {
        for (int i = 0; i < teamNum; i++) {
            eliminate(i);
        }
    }

    public int numberOfTeams() {
        return teamNum;
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    public int wins(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return wins[teams.get(team)];
    }

    public int losses(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return loses[teams.get(team)];
    }

    public int remaining(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return remain[teams.get(team)];
    }

    public int against(String team1, String team2) {
        if (team1 == null || !teams.containsKey(team1)) throw new IllegalArgumentException();
        if (team2 == null || !teams.containsKey(team2)) throw new IllegalArgumentException();
        return remainMap[teams.get(team1)][teams.get(team2)];
    }

    public boolean isEliminated(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return eliminated[teams.get(team)];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !teams.containsKey(team)) throw new IllegalArgumentException();
        return certs.get(teams.get(team));
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
