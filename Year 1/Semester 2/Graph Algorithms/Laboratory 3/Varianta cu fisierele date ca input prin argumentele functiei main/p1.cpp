#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
#include <set>
#include <iterator>

using namespace std;

#define inf 100000

int V,E,S;

struct dispar {
    int dist, par;
};

struct varf {
    dispar d;
};

void remove(int t, queue<int>& q)
{
    queue<int> ref;
    int s = q.size();
    int cnt = 0;
    while (q.front() != t and !q.empty()) {
        ref.push(q.front());
        q.pop();
        cnt++;
    }
    if (q.empty()) {
        while (!ref.empty()) {
            q.push(ref.front());
            ref.pop();
        }
    }
    else {
        q.pop();
        while (!ref.empty()) {
            q.push(ref.front());
            ref.pop();
        }
        int k = s - cnt - 1;
        while (k--) {
            int p = q.front();
            q.pop();
            q.push(p);
        }
    }
}

int extract_min(queue <int> q, struct varf varf[]) {
    int min = inf, min_index=-1;
    queue <int> copie = q;
    while (!copie.empty()) {
        int v = copie.front();
        if (varf[v].d.dist <= min) {
            min = varf[v].d.dist;
            min_index = v;
        }
        copie.pop();
    }
    return min_index;
}


void Dijkstra(struct varf varf[], vector<pair<int, int> > Adj[], int sursa) {
    int i, u, v, w;
    for (i = 0; i < V; i++) {
        varf[i].d.dist = inf;
        varf[i].d.par = inf;
    }
    varf[sursa].d.dist = 0;
    set <int> s;
    s.clear();
    queue <int> q;
    for (int i = 0; i < V; i++) {
        q.push(i);
    }
    while (!q.empty()) {
        u = extract_min(q,varf);
        remove(u, q);
        s.insert(u);
        vector <pair<int, int> >::iterator it;
        for (it = Adj[u].begin(); it != Adj[u].end(); it++) {
            v = it->first;
            w = it->second;
            if (varf[v].d.dist > varf[u].d.dist + w) {
                varf[v].d.dist = varf[u].d.dist + w;
                varf[v].d.par = u;
            }
        }
    }
}


void ada(vector <pair<int, int> > Adj[], int u, int v, int w)
{
    Adj[u].push_back(make_pair(v, w));
}

int main(int argc, char* argv[])
{
    std::ifstream f(argv[1]);
    std::ofstream g(argv[2]);
    int i,u,v,w;
    f>>V>>E>>S;
    vector<pair<int, int> > Adj[E];
   
    for(i=0;i<E;i++){
        f>>u>>v>>w;
        ada(Adj,u,v,w);
    } 
    varf varf[10001];
    Dijkstra(varf,Adj,S);
    for(i=0;i<V;i++){
        if(varf[i].d.dist==inf){
            g<<"INF ";
        }
        else{
            g<<varf[i].d.dist<<" ";
        }
    }
    f.close();
    g.close();
    return 0;
}
