#include <iostream>
#include <limits.h>
#include <fstream>
#include <queue>
#include <string.h>
#include <vector>
#include <iterator>

using namespace std;

ifstream f("in.txt");


int V,E;

bool bfs(vector <pair<int,int> > graf[], int sursa, int destinatie, int parinte[])
{
	bool vizitat[V];
	memset(vizitat, 0, sizeof(vizitat));
	queue<int> q;
	q.push(sursa);
	vizitat[sursa] = true;
	parinte[sursa] = -1;
	while (!q.empty()) {
		int u = q.front();
		q.pop();
        vector <pair<int,int> >::iterator it;
        for(it = graf[u].begin();it!=graf[u].end();it++){
            int v = it->first;
            int w = it->second;
            if (vizitat[v] == false && w> 0) {
                if (v == destinatie) {
                    parinte[v] = u;
                    return true;
                }
                q.push(v);
                parinte[v] = u;
                vizitat[v] = true;
            }
        }
	}
	return false;
}

int FordFulkerson(vector <pair<int,int> > grafrezidual[], int sursa, int destinatie)
{
	int u, v;

	int parinte[V];

	int fluxmaxim = 0;

	while (bfs(grafrezidual, sursa, destinatie, parinte)) {
		int fluxcurent = INT_MAX;
		for (v = destinatie; v != sursa; v = parinte[v]) {
			u = parinte[v];
			int fluxgr;
			vector <pair<int,int> >::iterator it;
            for(it = grafrezidual[u].begin();it !=grafrezidual[u].end();it++){
                if(it->first == v){
                    fluxgr = it->second;
                }
            }
			fluxcurent = min(fluxcurent, fluxgr);
		}
		for (v = destinatie; v != sursa; v = parinte[v]) {
			u = parinte[v];
			vector <pair<int,int> >::iterator it;
            for(it = grafrezidual[u].begin();it !=grafrezidual[u].end();it++){
                if(it->first == v){
                    it->second -= fluxcurent;
                }
            }

            bool ex = false;
			vector <pair<int,int> >::iterator it2;
            for(it2 = grafrezidual[v].begin();it2 !=grafrezidual[v].end();it2++){
                if(it->first == u){
                    it->second += fluxcurent;
                    ex = true; /// verific daca exista arcul (v,u), in caz contrar il voi adauga la graful rezidual
                }
            }
            if(!ex){
                grafrezidual[v].push_back(make_pair(u,fluxcurent));
			}
		}
		fluxmaxim += fluxcurent;
	}
	return fluxmaxim;
}


/// ----------------------------------------------------------------------------------------------


struct pom{
    int h,e;
};

void pompare(vector <pair<int,int> > grafrezidual[], struct pom p[], int u, int v){
    int deltaf;
    bool ex = false;
    vector <pair<int,int> >::iterator ite;
    for(ite = grafrezidual[u].begin();ite !=grafrezidual[u].end();ite++){
        if(ite->first == v && ite->second>0){
            deltaf = ite->second;
            ///Afisare pentru urmarirea procesului
            //cout<<"In for delta= "<<deltaf<<", v= "<<ite->first<<", CF="<<ite->second<<"\n";
            ex = true;
            //break;
        }
    }
    ///Afisare pentru urmarirea procesului
    //cout<<"u.e="<<p[u].e<<" delta = "<<deltaf<<" si bool="<<ex<<"\n";
    deltaf = min(p[u].e, deltaf);
    for(ite = grafrezidual[u].begin();ite !=grafrezidual[u].end();ite++){
        if(ite->first == v){
            ite->second -= deltaf;
            //break;
        }
    }
    ex = false;

    for(ite = grafrezidual[v].begin();ite !=grafrezidual[v].end();ite++){
        if(ite->first == u){
            ite->second += deltaf;
            ex = true; /// verific daca exista arcul (v,u), in caz contrar il voi adauga la graful rezidual
            //break;
        }
    }
    if(!ex){
        grafrezidual[v].push_back(make_pair(u,deltaf));
    }
    ///Afisare pentru urmarirea procesului
    //cout<<"delta="<<deltaf<<"\n";

    p[u].e -= deltaf;
    p[v].e += deltaf;
}

void inaltare(vector <pair<int,int> > grafrezidual[], struct pom p[], int u, int v){
    int mini = V+1;
    int ceva = -1;
    ///Afisare pentru urmarirea procesului
    //cout<<"Mini initial = "<<mini;
    vector <pair<int,int> >::iterator ite;
    for(ite = grafrezidual[u].begin();ite !=grafrezidual[u].end();ite++){
            if(ceva == -1){
                if(ite->second > 0 ){
                ceva = p[ite->first].h ;}
            }
            else{
                if(p[ite->first].h < ceva && (ite->second)>0){
                    ceva = p[ite->first].h ;
                }
            }
    }
    p[u].h = 1 + ceva;
    ///Afisare pentru urmarirea procesului
    //cout<<"Deci u.h="<<p[u].h<<" v.h="<<p[v].h<<"\n";
}

void initializare(vector <pair<int,int> > grafrezidual[],vector <pair<int,int> > graf[], struct pom p[], int sursa){
    int i;
    vector <pair<int,int> >::iterator ite;
    for(i=0;i<V;i++){
        p[i].h=0;
        p[i].e=0;
    }

    for(i=0;i<V;i++){
        for(ite = graf[i].begin();ite !=graf[i].end();ite++){
            grafrezidual[i].push_back(make_pair(ite->first,ite->second));
        }
    }
    p[sursa].h=V;
    vector<int>csv;

    for(ite = graf[sursa].begin();ite !=graf[sursa].end();ite++){
        csv.push_back(ite->second);
        p[ite->first].e = ite->second;
        p[sursa].e -= ite->second;
    }
    for(ite = grafrezidual[sursa].begin();ite !=grafrezidual[sursa].end();ite++){
        ite->second=csv.front();
        csv.erase(csv.begin());
    }


}

void pompare_preflux(vector <pair<int,int> > graf[], struct pom p[]){///sursa e 0 si destinatia V-1
    vector <pair<int,int> > grafrezidual[V];
    initializare(grafrezidual,graf,p,0);
    int i,u,v;
    vector <pair<int,int> >::iterator ite;
    while(1){
        bool ex1 = false, ex2=false;
        i=1;
        while(i<V-1 && ex1 == false){
            if(p[i].e > 0 && i!=V-1 && i!=0){
                for(ite = grafrezidual[i].begin();ite !=grafrezidual[i].end();ite++){
                    if((p[i].h == p[ite->first].h + 1) && (ite->second)>0  ){
                        u=i;
                        v=ite->first;
                        ///Afisare pentru urmarirea procesului
                        //cout<<"Pu= "<<u<<", v= "<<v<<", CF="<<ite->second<<"\n";
                        ex1=true;
                        break;
                    }
                }
            }
            i++;
        }
        if(ex1){
            ///Afisare pentru urmarirea procesului
            //cout<<"Pompez u= "<<u<<", v= "<<v<<"\n";
            pompare(grafrezidual,p,u,v);
            continue;
        }
        i=1;
        while(i<V-1 && ex2 == false){
            if(p[i].e > 0 && i!=V-1 && i!=0){

                for(ite = grafrezidual[i].begin();ite !=grafrezidual[i].end();ite++){
                    if(p[i].h <= p[ite->first].h  && (ite->second)>0  ){
                        u=i;
                        v=ite->first;
                        ///Afisari pentru urmarirea procesului
                        //cout<<"Iu= "<<u<<", v= "<<v<<", CF="<<ite->second<<"\n";
                        //cout<<"u.h="<<p[u].h<<" v.h="<<p[v].h<<"\n";
                        ex2=true;
                        break;
                    }
                }
            }
            i++;
        }
        if(ex2){
            inaltare(grafrezidual,p,u,v);
            continue;
        }
        break;
    }
    cout<<p[V-1].e;
}

int main()
{
    f>>V>>E;
    vector<pair<int,int> >graf[V];
    int x,y,w;
    while(f>>x>>y>>w){
        graf[x].push_back(make_pair(y,w));
    }
    f.close();
	//cout << FordFulkerson(graf, 0, V-1);
	struct pom p[1000];
    pompare_preflux(graf,p);
	return 0;
}

