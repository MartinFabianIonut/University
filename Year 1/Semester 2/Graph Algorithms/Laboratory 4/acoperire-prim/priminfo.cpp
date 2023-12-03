#include <fstream>
#include <algorithm>
#include <vector>
#include <queue>
#include <iostream>
#include <climits>
#include <chrono>

using namespace std;

struct Node
{
    int key;
    int parent;
    int nodeId;

    bool operator<(const Node &other) const
    {
        return key > other.key;
    }

    bool inQueue;
};

// Variabile globale
ifstream f("in.txt");
ofstream g("out.txt");
int V, E;

vector<vector<pair<int, int>>> graph;

void addEdge(int u, int v, int weight)
{
    graph[u].emplace_back(v, weight);
    graph[v].emplace_back(u, weight);
}

void mst_prim(int r)
{
    vector<Node> nodes(V + 1);
    for (int i = 1; i <= V; ++i)
    {
        nodes[i] = {INT_MAX, -1, i, true};
    }

    nodes[r].key = 0;
    priority_queue<Node, vector<Node>> pq;
    pq.push(nodes[r]);
    while (!pq.empty())
    {
        int u = pq.top().nodeId;
        pq.pop();
        nodes[u].inQueue = false;

        for (auto &edge : graph[u])
        {
            int v = edge.first;
            int weight = edge.second;

            if (nodes[v].key > weight && nodes[v].inQueue == true)
            {
                nodes[v].key = weight;
                nodes[v].parent = u;
                pq.push(nodes[v]);
            }
        }
    }

    int cost = 0;
    for (int u = 1; u <= V; ++u)
    {
        cost += nodes[u].key;
    }
    g << cost << "\n";
    g << V - 1 << "\n";

    for (int i = 1; i <= V; ++i)
    {
        if (nodes[i].parent != -1)
        {
            g << i << ' ' << nodes[i].parent << '\n';
        }
    }
}

int main()
{
    auto start = chrono::high_resolution_clock::now();
    f >> V >> E;

    graph = vector<vector<pair<int, int>>>(V + 1);

    for (int i = 1; i <= E; ++i)
    {
        int u, v, weight;
        f >> u >> v >> weight;
        addEdge(u, v, weight);
    }

    int startNode = 1; // Nodul de start pentru algoritmul Prim

    mst_prim(startNode);
    auto end = chrono::high_resolution_clock::now();
    auto millis = chrono::duration_cast<chrono::milliseconds>(end - start).count();

    f.close();
    g.close();

    return 0;
}
