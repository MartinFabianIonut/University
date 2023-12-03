#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <iterator>
#include <cmath>

using namespace std;

ifstream f("in.txt");
ofstream g("out.txt");

struct arc
{
    int u, v;
    double w;
} arce[26000];

int V, E, sett[26000];
int N, Cost;
arc arbore[5001];

void sort_muchii()
{
    bool ok = false;
    while (!ok)
    {
        ok = true;
        for (int i = 0; i < E; i++)
        {
            if (arce[i].w > arce[i + 1].w)
            {
                arc aux = arce[i];
                arce[i] = arce[i + 1];
                arce[i + 1] = aux;
                ok = false;
            }
        }
    }
}

void Kruskal()
{
    Cost = 0;
    N = 0;
    for (int i = 0; i < V; i++)
    {
        sett[i] = i;
    }
    sort_muchii();
    for (int i = 0; i < E; i++)
    {
        arc a = arce[i];
        if (sett[a.u] != sett[a.v])
        {
            Cost += a.w;
            arbore[N] = a;
            N++;
            int set_v = sett[a.v];
            for (int j = 0; j < V; j++)
            {
                if (sett[j] == set_v)
                {
                    sett[j] = sett[a.u];
                }
            }
        }
    }
}

int main()
{
    f >> V;
    E = (V * (V - 1)) / 2;
    int x, y;
    int i = 0, j = 0, k = 0;
    vector<pair<int, int>> noduri;
    vector<pair<int, int>>::iterator it, it2;
    while (f >> x >> y)
    {
        noduri.push_back(make_pair(x, y));
    }
    for (it = noduri.begin(); it != noduri.end(); it++)
    {
        i++;
        for (it2 = noduri.begin(); it2 != noduri.end(); it2++)
        {
            if (it != it2)
            {
                int x1, x2, y1, y2;
                x1 = it->first;
                y1 = it->second;
                x2 = it2->first;
                y2 = it2->second;
                j++;
                double w = sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                arce[k].u = i;
                arce[k].v = j;
                arce[k].w = w;
                k++;
            }
        }

        j = 0;
    }
    f.close();
    Kruskal();
    g << Cost << "\n";
    g << N << "\n";
    vector<pair<int, int>> acoperire;
    for (int i = 0; i < N; i++)
    {
        acoperire.push_back(make_pair(arbore[i].u, arbore[i].v));
    }
    std::sort(acoperire.begin(), acoperire.end());

    for (it = acoperire.begin(); it != acoperire.end(); it++)
    {
        g << it->first << " " << it->second << '\n';
    }
    g.close();
    return 0;
}
