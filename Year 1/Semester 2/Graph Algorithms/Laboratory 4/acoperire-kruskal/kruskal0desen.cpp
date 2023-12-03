#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <set>
#include <chrono>
#include <cmath>
#include <iomanip>

using namespace std;

ifstream f("desen.in");  // argv[1]
ofstream g("desen.out"); // argv[2]

struct arc
{
    int u, v;
    double w;

    bool operator<(const arc &a) const
    {
        return w < a.w;
    }
};

int V;
vector<int> sett;
vector<arc> arbore;

int findSetRoot(int x)
{
    if (sett[x] == x)
        return x;
    int aux = findSetRoot(sett[x]);
    sett[x] = aux;
    return aux;
}

void Kruskal(vector<arc> &arce, int V, int E)
{
    sort(arce.begin(), arce.end());

    double Cost = 0;

    sett = vector<int>(V);
    for (int i = 0; i < V; i++)
    {
        sett[i] = i;
    }

    for (int i = 0; i < E; i++)
    {
        arc a = arce[i];
        int x = findSetRoot(a.u);
        int y = findSetRoot(a.v);
        if (x != y)
        {
            Cost += a.w;
            arbore.emplace_back(a);
            sett[y] = x;
        }
    }

    g << fixed << setprecision(6) << Cost << '\n';
}

int main(int argc, char const *argv[])
{
    auto start = chrono::high_resolution_clock::now();
    f >> V;
    vector<pair<int, int>> coordonate(V);
    g << fixed << setprecision(6) << (double)0 << '\n';
    f >> coordonate[0].first >> coordonate[0].second;

    vector<arc> arce;
    arce.reserve(V * (V - 1) / 2);
    arbore.reserve(V - 1);

    for (int j = 1; j < V; j++)
    {
        f >> coordonate[j].first >> coordonate[j].second;
        for (int i = 0; i < j; i++)
        {
            double x = coordonate[i].first - coordonate[j].first;
            double y = coordonate[i].second - coordonate[j].second;
            double w = sqrt(x * x + y * y);
            arce.emplace_back(arc{i, j, w});
        }
        Kruskal(arce, j + 1, (j * (j + 1)) / 2);
    }

    auto end = chrono::high_resolution_clock::now();

    // cout << "Time: " << chrono::duration_cast<chrono::milliseconds>(end - start).count() << " ms\n";
    return 0;
}
