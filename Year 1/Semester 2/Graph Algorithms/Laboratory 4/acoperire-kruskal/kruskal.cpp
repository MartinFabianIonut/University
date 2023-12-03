#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <set>
#include <chrono>

using namespace std;

ifstream f("inkruskal.txt");
ofstream g("out.txt");

struct arc
{
    int u, v;
    double w;

    bool operator<(const arc &a) const
    {
        return w < a.w;
    }
};

int V, E;
int N, Cost;
vector<arc> arbore;

int main()
{
    f >> V >> E;

    // Use vector instead of dynamic array
    vector<arc> arce(E);
    for (int i = 0; i < E; i++)
    {
        f >> arce[i].u >> arce[i].v >> arce[i].w;
    }

    auto start = chrono::high_resolution_clock::now();
    sort(arce.begin(), arce.end());

    vector<set<int>> sett(V);
    for (int i = 0; i < V; i++)
    {
        sett[i].insert(i);
        // get sett[i]
        auto it = sett[i].begin();
        int set_i = *it;
    }

    for (int i = 0; i < E; i++)
    {
        arc a = arce[i];
        if (sett[a.u] != sett[a.v])
        {
            Cost += a.w;
            arbore.push_back(a);
            int set_v = *sett[a.v].begin();
            for (int j = 0; j < V; j++)
            {
                if (*sett[j].begin() == set_v)
                {
                    sett[j] = sett[a.u];
                }
            }
        }
    }

    g << Cost << '\n';
    g << arbore.size() << '\n';

    auto customSort = [](const arc &a, const arc &b)
    {
        if (a.u != b.u)
            return a.u < b.u;
        return a.v < b.v;
    };

    // Resort arbore using the custom sorting function
    sort(arbore.begin(), arbore.end(), customSort);
    auto end = chrono::high_resolution_clock::now();
    for (auto &a : arbore)
    {
        g << a.u << ' ' << a.v << '\n';
    }

    cout << "Time taken: " << chrono::duration_cast<chrono::milliseconds>(end - start).count() << " ms\n";

    return 0;
}
