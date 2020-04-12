# Princeton_Algorithm
 Coursera Princeton Algorithms

## Graph problems

1. Connected components.
 - BFS/DFS numbering
2. Judge a Biparite Graph
 - BFS/DFS coloring
3. Judge cyclic graph
 - DFS with recStack
4. Euler tour
 - Euler tour judge: Connected grapth & all even degree.
 - Cycle finding algorithm: DFS
 - Fleury's algorithm: Find non-bridge edge.
5. Hamilton Cycle(NP-hard)
6. Graph isomorphism(Unknown)
7. Lay out a graph in plane without crossing edges
8. Diameter of graph
 - Two DFS/BFS
9. Center of a graph
 - Floyd algorithm, find minimum maxDis.
10. Topology sort
11. Strong Componets
 - Topology sort
 - Reverse Graph
12. Shortest directed cycle
 - G BFS/Dijkstra, G.reverse() BFS/Dijkstra
13. Hamilton path in DAG
 - Topology sort -> topological sequence. Hamilton path exists iff continuous vertexes in sequence are connected.
14. Reachable vertex in DAG
 - Only one zero-outdegree vertex.
15. Reachable vertex in Digragh
 - Topology sort
 16. MST
 - Kruskal
 - Prim
 17. Minmun Bottleneck Spanning Tree <- MST
 18. Is edge in SOME MST
 - DFS(Judge cycle)
 19. Minimum-weight feedback edge set. A feedback edge set of a graph is a subset of edges that contains at least one edge from every cycle in the graph. If the edges of a feedback edge set are removed, the resulting graph is acyclic. Given an edge-weighted graph, design an efficient algorithm to find a feedback edge set of minimum weight. Assume the edge weights are positive.
 - Run Kruskal with Max edge. All edges detected to form a loop will be added to minimum-weight feedback edge set.
 20. Monotonic shortest path
 - Dijkstra (relax vertices with edeg-weight-ascending/descending order).
 21. Second shorest path
 - Dijkstra. (store second shortest path and distance when relaxing)
 - Floyd. Try all intermediate nodes one by one.
 22. Shortest path with one skippable edge
 - For each edge (v,w), Min(SP(s,v)+SP(w,t))
