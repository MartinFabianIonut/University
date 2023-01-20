#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;
using std::sort;

ifstream f("in.txt");
ofstream g("out.txt");

#define inf 100000

int V, E;

struct prim {
    int parinte;
    int cheie;
    int valoare;
};

struct Compara {
    bool operator()(prim const& p1, prim const& p2)
    {
        return p1.cheie > p2.cheie;
    }
};


int main()
{
    f >> V >> E;
    vector<pair<int, int> > Adj[500];
    int i, j, u, v, w;
    for (i = 0; i < E; i++) {
        f >> u >> v >> w;
        Adj[u].push_back(make_pair(v, w));
        Adj[v].push_back(make_pair(u, w));
    }
    prim p[5000];
    for (i = 0; i < V; i++) {
        p[i].cheie = inf;
        p[i].parinte = -1;
        p[i].valoare = i;
    }
    p[0].cheie = 0;
    priority_queue<prim, vector<prim>, Compara>q;
    vector<prim>coada;
    for (i = 0; i < V; i++) {
        q.push(p[i]);
        coada.push_back(p[i]);
    }
    std::sort(coada.begin(), coada.end(),[](const prim& a, const prim& b){return a.cheie < b.cheie;});
    while (!coada.empty()) {
        prim u = *coada.begin();
        coada.erase(coada.begin());
        vector <pair<int, int> >::iterator it;
        for (it = Adj[u.valoare].begin(); it != Adj[u.valoare].end(); it++) {
            v = it->first;
            w = it->second;
            vector<prim>coada2 = coada;
            bool exista = false;
            while (!coada2.empty()) {
                prim x = *coada2.begin();
                if (x.valoare == v) {
                    exista = true;
                }
                coada2.erase(coada2.begin());
            }
            if (p[v].cheie > w && exista == true) {
                p[v].parinte = u.valoare;
                p[v].cheie = w;
                vector<prim>::iterator it2;
                int k = 0;
                for (it2 = coada.begin(); it2 != coada.end(); it2++) {
                    prim verif = *it2;
                    if (verif.valoare == p[v].valoare) {
                        coada[k] = p[v];
                    }
                    k++;
                }
            }
        }
        std::sort(coada.begin(), coada.end(),
            [](const prim& a, const prim& b)
            {
                return a.cheie < b.cheie;
            });
    }
    int cost = 0, muchii = 0;
    for (i = 0; i < V; i++) {
        cost += p[i].cheie;
        if (p[i].parinte != -1) {
            muchii++;
        }
    }
    g << cost << '\n' << muchii << '\n';
    vector<int> adj[500];
    for (i = 0; i < V; i++) {
        for (j = 0; j < V; j++) {
            if (p[j].parinte == i) {
                g << i << " " << j << '\n';
            }
        }

    }
    f.close();
    g.close();
    return 0;
}
