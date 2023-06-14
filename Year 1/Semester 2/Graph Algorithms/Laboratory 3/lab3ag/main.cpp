#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
#include <set>
#include <iterator>
using namespace std;

ifstream f("1-in.txt");
ofstream g("1-out.txt");

#define inf 100000
#define dim 150001


int V,E,S;

struct dispar {
    int dist, par;
    int valoare;
};

struct Varf {
    dispar d;
};


bool BellmanFord(struct Varf varf[10001], int G[100400][4], int sursa){
    int i,j,u,v,w;
    for(i=0;i<V;i++){
        varf[i].d.dist=inf;
        varf[i].d.par=inf;
    }
    varf[sursa].d.dist=0;
    for(i=1;i<V;i++){
        for(j=0;j<E;j++){
            u=G[j][0];v=G[j][1];w=G[j][2];
            if(varf[v].d.dist > varf[u].d.dist + w){
                varf[v].d.dist = varf[u].d.dist + w;
                varf[v].d.par = u;
            }
        }
    }
    for(j=0;j<E;j++){
        u=G[i][0];v=G[i][1];w=G[i][2];
        if(varf[v].d.dist > varf[u].d.dist + w){
            return false;
        }
    }
    return true;
}

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

int extract_min(queue <int> q, struct Varf varf[20000]) {
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


struct ComparaVarfuri {
    bool operator()(Varf const& v1, Varf const& v2)
    {
        return v1.d.dist > v2.d.dist;
    }
};

void Dijkstra(struct Varf varf[20000], vector<pair<int, int> > Adj[10000], int sursa) {
    int i, j, u, v, w;
    for (i = 0; i < V; i++) {
        varf[i].d.dist = inf;
        varf[i].d.par = inf;
        varf[i].d.valoare = i;
    }
    varf[sursa].d.dist = 0;
    priority_queue<Varf, vector<Varf>,ComparaVarfuri>qq;
    qq.push(varf[sursa]);
    while (!qq.empty()) {
        Varf varful_scos = qq.top();
        u = varful_scos.d.valoare;
        qq.pop();
        vector <pair<int, int> >::iterator it;
        for (it = Adj[u].begin(); it != Adj[u].end(); it++) {
            v = it->first;
            w = it->second;
            if (varf[v].d.dist > varf[u].d.dist + w) {
                varf[v].d.dist = varf[u].d.dist + w;
                varf[v].d.par = u;
                qq.push(varf[v]);
            }
        }
    }
}


void p1(){
    int i,G[100400][4]={0};
    f>>V>>E>>S;
    for(i=0;i<E;i++){
        f>>G[i][0]>>G[i][1]>>G[i][2];
    }
    Varf v[10001];
    if(BellmanFord(v,G,S)==true){
        for(i=0;i<V;i++){
            if(v[i].d.dist==inf){
                g<<"INF ";
            }
            else{
                g<<v[i].d.dist<<" ";
            }
        }
    }
    else{
        g<<"Circuite negative!";
    }
    f.close();
    g.close();
}

bool BellmanFord2(struct Varf varf[], vector <pair<int, int> > Adj[], int sursa){
    int i,j,u,v,w;
    for(i=0;i<V;i++){
        varf[i].d.dist=inf;
        varf[i].d.par=inf;
    }
    varf[sursa].d.dist=0;
    for(j=1;j<V;j++){
        for(i=0;i<V;i++){
            vector <pair<int, int> >::iterator it;
            for (it = Adj[i].begin(); it!=Adj[i].end(); it++){
                u = i;
                v = it->first;
                w = it->second;
                if(varf[v].d.dist > varf[u].d.dist + w){
                    varf[v].d.dist = varf[u].d.dist + w;
                    varf[v].d.par = u;
                }
            }
        }
    }
    for(i=0;i<V;i++){
        vector <pair<int, int> >::iterator it;
        for (it = Adj[i].begin(); it!=Adj[i].end(); it++){
            u = i;
            v = it->first;
            w = it->second;
            if(varf[v].d.dist > varf[u].d.dist + w){
                return false;
            }
        }
    }
    return true;
}

void ada(vector <pair<int, int> > Adj[], int u, int v, int w)
{
    Adj[u].push_back(make_pair(v, w));
}

void p12(){
    f>>V>>E>>S;
    vector<pair<int, int> > Adj[E];
    int i,u,vv,w;
    for(i=0;i<E;i++){
        f>>u>>vv>>w;
        ada(Adj,u,vv,w);
    }
    Varf v[10001];
    if(BellmanFord2(v,Adj,S)==true){
        for(i=0;i<V;i++){
            if(v[i].d.dist==inf){
                g<<"INF ";
            }
            else{
                g<<v[i].d.dist<<"\n";
            }
        }
    }
    else{
        g<<"Circuite negative!";
    }
    f.close();
    g.close();
}

void di(){
    f>>V>>E>>S;
    vector<pair<int, int> > Adj[E];
    int i,u,vv,w;
    for(i=0;i<E;i++){
        f>>u>>vv>>w;
        ada(Adj,u,vv,w);
    }
    Varf v[10001];
    Dijkstra(v,Adj,S);
    for(i=0;i<V;i++){
        if(v[i].d.dist==inf){
            g<<"INF ";
        }
        else{
            g<<v[i].d.dist<<" ";
        }
    }
    f.close();
    g.close();
}



void Johnson(struct Varf varf[20000], vector<pair<int, int> > Adj[10000]) {
    int i, j, u, v, w,wnou,dist_u_v;
    vector<int>h;
    E+=V;
    V++;
    for(i=0;i<V-1;i++){
        ada(Adj,V-1,i,0);
    }
    vector <pair<int, int> >Adj_pt_wnou[E];
    if(BellmanFord2(varf,Adj,V-1)==false){
        g<<-1;
    }
    else{
        for(i=0;i<V;i++){
            h.push_back(varf[i].d.dist);
        }
        vector <pair<int, int> >::iterator it;
        for(i=0;i<V;i++){
                for (it = Adj[i].begin(); it!=Adj[i].end(); it++){
                u = i;
                v = it->first;
                w = it->second;
                wnou = w + h[u] - h[v];
                ada(Adj_pt_wnou,u,v,wnou);
            }
        }
        for(i=0;i<V-1;i++){
                for (it = Adj_pt_wnou[i].begin(); it!=Adj_pt_wnou[i].end(); it++){
                u = i;
                v = it->first;
                w = it->second;
                g<<u<<" "<<v<<" "<<w<<"\n";
            }
        }
        for(i=0;i<V-1;i++){
            Dijkstra(varf,Adj_pt_wnou,i);
            for(j=0;j<V-1;j++){
                if(varf[j].d.dist == inf){
                    g<<"INF ";
                }
                else{
                    dist_u_v = varf[j].d.dist-h[i]+h[j];
                    g<<dist_u_v<<" ";
                }
            }
            g<<"\n";
        }
    }
}


void p2(){
    f>>V>>E;
    vector<pair<int, int> > Adj[E];
    int i,u,vv,w;
    for(i=0;i<E;i++){
        f>>u>>vv>>w;
        ada(Adj,u,vv,w);
    }
    Varf v[1001];
    Johnson(v,Adj);
    f.close();
    g.close();
}


int main(){
    //p1();
    //p12();
    di();
    //p2();
    return 0;
}

