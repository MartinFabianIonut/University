#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <set>
#include <chrono>

using namespace std;

struct arc
{
    int u, v;
    double w;

    bool operator<(const arc &a) const
    {
        return w < a.w;
    }
};

int V, E, *sett;
int N, Cost;
vector<arc> arbore;

int findSetRoot(int x)
{
    if (sett[x] == x)
        return x;
    int aux = findSetRoot(sett[x]);
    sett[x] = aux;
    return aux;
}

int main(int argc, char const *argv[])
{
    if (argc < 3)
    {
        cout << "Expected 2 arguments: input file and output file\n";
        return 1;
    }
    ifstream f(argv[1]);
    ofstream g(argv[2]);

    cout << "Reading from " << argv[1] << " and writing to " << argv[2] << '\n';

    f >> V >> E;
    vector<arc> arce(E);
    for (int i = 0; i < E; i++)
    {
        f >> arce[i].u >> arce[i].v >> arce[i].w;
    }
    auto start = chrono::high_resolution_clock::now();
    sort(arce.begin(), arce.end());

    sett = new int[V];
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
            arbore.push_back(a);
            sett[y] = x;
        }
    }

    g << Cost << '\n';
    g << arbore.size() << '\n';
    auto custom_sort = [](const arc &a, const arc &b)
    {
        if (a.u != b.u)
            return a.u < b.u;
        return a.v < b.v;
    };
    sort(arbore.begin(), arbore.end(), custom_sort);
    auto end = chrono::high_resolution_clock::now();
    for (auto &a : arbore)
    {
        g << a.u << ' ' << a.v << '\n';
    }
    cout << "Time: " << chrono::duration_cast<chrono::milliseconds>(end - start).count() << " ms\n";
    return 0;
}
