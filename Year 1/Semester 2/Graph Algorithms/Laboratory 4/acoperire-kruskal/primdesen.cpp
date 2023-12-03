// #include <fstream>
// #include <algorithm>
// #include <vector>
// #include <queue>
// #include <iostream>
// #include <climits>
// #include <chrono>
// #include <iomanip>

// using namespace std;

// struct Node
// {
//     int key;
//     int parent;
//     int nodeId;

//     bool operator<(const Node &other) const
//     {
//         return key > other.key;
//     }

//     bool inQueue;
// };

// ifstream f("desen.in");  // argv[1]
// ofstream g("desen.out"); // argv[2]

// // Variabile globale
// int V, E;

// vector<vector<pair<int, int>>> graph;

// void addEdge(int u, int v, int weight)
// {
//     graph[u].emplace_back(v, weight);
//     graph[v].emplace_back(u, weight);
// }

// void mst_prim(int r)
// {
//     vector<Node> nodes(V);
//     for (int i = 0; i < V; ++i)
//     {
//         nodes[i] = {INT_MAX, -1, i, true};
//     }

//     nodes[r].key = 0;
//     priority_queue<Node, vector<Node>> pq;
//     pq.push(nodes[r]);
//     while (!pq.empty())
//     {
//         int u = pq.top().nodeId;
//         pq.pop();
//         nodes[u].inQueue = false;

//         for (auto &edge : graph[u])
//         {
//             int v = edge.first;
//             int weight = edge.second;

//             if (nodes[v].key > weight && nodes[v].inQueue == true)
//             {
//                 nodes[v].key = weight;
//                 nodes[v].parent = u;
//                 pq.push(nodes[v]);
//             }
//         }
//     }

//     int cost = 0;
//     for (int u = 0; u < V; ++u)
//     {
//         cost += nodes[u].key;
//     }
//     g << cost << "\n";
//     g << V - 1 << "\n";

//     // sort nodes[u].parent and u alphabetically
//     // vector<pair<int, int>> edges;
//     // for (int i = 0; i < V; ++i)
//     // {
//     //     if (nodes[i].parent != -1)
//     //     {
//     //         edges.emplace_back(min(i, nodes[i].parent), max(i, nodes[i].parent));
//     //     }
//     // }
//     // sort(edges.begin(), edges.end());
//     // // print edges
//     // for (const auto &edge : edges)
//     // {
//     //     g << edge.first << ' ' << edge.second << '\n';
//     // }
//     vector<pair<int, int>> edges;
//     for (int i = 0; i < V; ++i)
//     {
//         if (nodes[i].parent != -1)
//         {
//             edges.emplace_back(min(i, nodes[i].parent), max(i, nodes[i].parent));
//         }
//     }
//     sort(edges.begin(), edges.end());
//     // print edges
//     for (const auto &edge : edges)
//     {
//         g << edge.first << ' ' << edge.second << '\n';
//     }
// }

// int main(int argc, char **argv)
// {

//     auto start = chrono::high_resolution_clock::now();
//     f >> V;

//     E = V * (V - 1) / 2;
//     vector<pair<int, int>> coordonate(V);
//     g << fixed << setprecision(6) << (double)0 << '\n';
//     f >> coordonate[0].first >> coordonate[0].second;

//     vector<arc> arce;
//     for (int j = 1; j < V; j++)
//     {
//         f >> coordonate[j].first >> coordonate[j].second;
//         for (int i = 0; i < j; i++)
//         {
//             double x = coordonate[i].first - coordonate[j].first;
//             double y = coordonate[i].second - coordonate[j].second;
//             double w = sqrt(x * x + y * y);
//             arce.push_back({i, j, w});
//         }
//         Kruskal(arce, j + 1, arce.size());
//     }

//     graph = vector<vector<pair<int, int>>>(V);

//     for (int i = 0; i < E; ++i)
//     {
//         int u, v, weight;
//         f >> u >> v >> weight;
//         addEdge(u, v, weight);
//     }

//     f.close();

//     int startNode = 0; // Nodul de start pentru algoritmul Prim

//     mst_prim(startNode);
//     auto end = chrono::high_resolution_clock::now();
//     auto millis = chrono::duration_cast<chrono::milliseconds>(end - start).count();
//     cout << "Time: " << millis << " ms\n";
//     g.close();
//     return 0;
// }
