#include <iostream>
#include <fstream>
#include <cstring>
#include <queue>
#include <string>

using namespace std;

ifstream f("graf.txt");
ifstream f2("graf2.txt");
ifstream flab("labirint.txt");
ifstream f4("graf4.txt");
ifstream f5("graf5.txt");

int lungime[100],parinte[100];
#define inf 10000

struct proprietati{
    char culoare[16];
    int parinte;
    int distanta;
};

struct varf{
    int val;
    proprietati prop;
};

struct prop{
    char culoare[16];
    int parinte;
    int descoperire,finalizare;
};

struct dfs{
    int val;
    prop prop;
};


void Moore(int l[100][100],int n, int u){
    int i,x,y;
    for(i=1;i<=n;i++){
        lungime[i]=inf;
    }
    lungime[u]=0;
    queue<int>coada;
    coada.push(u);
    while(!coada.empty()){
        x = coada.front();
        coada.pop();
        for(y=1;y<=l[x][1];y++){
            if(lungime[l[x][y+1]]==inf || lungime[l[x][y+1]]==0){
                parinte[l[x][y+1]]=x;
                lungime[l[x][y+1]]=lungime[x]+1;
                coada.push(l[x][y+1]);
            }
        }
    }
}

void Drum_Moore(int v){
    int dim = lungime[v];
    int drum[100];
    drum[dim]=v;
    while(dim){
        dim--;
        drum[dim]=parinte[drum[dim+1]];
    }
    for(int i=0;i<lungime[v];i++){
        cout<<drum[i]<<"->";
    }
    cout<<drum[lungime[v]];
}

void punctul1(){
    int i,x,y,n,frecventa,listaa[100][100];
    f>>n;
    while(f>>x>>y){
        listaa[x][1]++;
        frecventa = listaa[x][1];
        listaa[x][frecventa+1]=y;
    }
    f.close();
    int vf;
    cout<<"Introduceti varful sursa: ";
    cin>>vf;
    Moore(listaa,n,vf);
    for(i=1;i<=n;i++){
        if(lungime[i]!=inf){
            cout<<"Lungimea drumului minim intre "<<vf<<" si "<<i<<" este: "<<lungime[i];
            cout<<", iar drumul arata asa:\n\t";
            Drum_Moore(i);
            cout<<"\n";
        }
        if(lungime[i]==inf){
            cout<<"Nu exista drum intre "<<vf<<" si "<<i<<"\n";
        }
    }
}

/// ---------------------------------------------------------------------------------------------

void punctul2(){
    int i,j,inchidere_tranzitiva[100][100]={0},n,x,y,frecventa,listaa[100][100];
    f2>>n;
    while(f2>>x>>y){
        listaa[x][1]++;
        frecventa = listaa[x][1];
        listaa[x][frecventa+1]=y;
    }
    f2.close();
    for(i=1;i<=n;i++){
        Moore(listaa,n,i);
        for(j=1;j<=n;j++){
            if(lungime[j]!=inf && lungime[j]!=0){
                inchidere_tranzitiva[i][j]=1;
            }
        }
    }
    cout<<"\nInchiderea tranzitiva a grafului dat este:\n";
    for(i=1;i<=n;i++){
        for(j=1;j<=n;j++){
            cout<<inchidere_tranzitiva[i][j]<<" ";
        }
        cout<<"\n";
    }
}

/// ---------------------------------------------------------------------------------------------

void BFS(int l[100][100], struct varf v[10], int vf,int n){
    int i,u,p;
    for(i=1;i<=n;i++){
        if(i!=vf){
            strcpy(v[i].prop.culoare,"alb");
            v[i].prop.distanta=inf;
            v[i].prop.parinte=0;
        }
    }
    strcpy(v[vf].prop.culoare,"gri");
    v[vf].prop.distanta=0;
    v[vf].prop.parinte=0;
    queue<int>coada;
    coada.push(vf);
    while(!coada.empty()){
        u=coada.front();
        coada.pop();
        for(i=1;i<=l[u][1];i++){
            p=l[u][i+1];
            if(strcmp(v[p].prop.culoare,"alb")==0){
                strcpy(v[p].prop.culoare,"gri");
                v[p].prop.distanta=v[u].prop.distanta+1;
                v[p].prop.parinte=u;
                coada.push(p);
            }
        }
        strcpy(v[u].prop.culoare,"negru");
    }
}

void punctul4(){
    int i,x,y,n,frecventa,listaa[100][100];
    f4>>n;
    while(f4>>x>>y){
        listaa[x][1]++;
        frecventa = listaa[x][1];
        listaa[x][frecventa+1]=y;
        listaa[y][1]++;
        frecventa = listaa[y][1];
        listaa[y][frecventa+1]=x;
    }
    f4.close();
    varf v[n+1];
    for(i=1;i<=n;i++){
        v[i].val=i;
    }
    int vf;
    cout<<"Introduceti varful sursa: ";
    cin>>vf;
    BFS(listaa,v,vf,n);
    for(i=1;i<=n;i++){
        if(strcmp(v[i].prop.culoare,"negru")==0){
            cout<<"A fost descoperit nodul "<<i<<" si se afla la distanta "<<v[i].prop.distanta;
            cout<<" fata de nodul sursa "<<vf<<", avand ca parinte pe " << v[i].prop.parinte<<"\n";
        }
        else{
            cout<<"Nodul "<<i<<" nu a fost descoperit de BFS, adica nu exista";
            cout<<" un drum intre el si nodul sursa "<<vf<<"\n";
        }
    }
}

/// ---------------------------------------------------------------------------------------------

void DFS_VISIT(int l[100][100], struct dfs v[10], int n, int &time,int u){
    int i,p;
    time++;
    v[u].prop.descoperire=time;
    strcpy(v[u].prop.culoare,"gri");
    cout<<u<<" ";
    for(i=1;i<=l[u][1];i++){
        p=l[u][i+1];
        if(strcmp(v[p].prop.culoare,"alb")==0){
            v[p].prop.parinte=u;
            DFS_VISIT(l,v,n,time,p);
        }
    }
    strcpy(v[u].prop.culoare,"negru");
    time++;
    v[u].prop.finalizare=time;
}

void DFS(int l[100][100], struct dfs v[10], int n){
    int i,j,k,time;
    for(i=1;i<=n;i++){
        for(k=1;k<=n;k++){
            strcpy(v[k].prop.culoare,"alb");
            v[k].prop.parinte=0;
        }
        time=0;
        cout<<"In parcurgerea DFS din varful "<<i<<" am descoperit varfurile: ";
        for(j=1;j<=n;j++){
            if(strcmp(v[i].prop.culoare,"alb")==0){
                DFS_VISIT(l,v,n,time,i);
            }
        }
        cout<<"\n";
    }
}

void punctul5(){
    int i,x,y,n,frecventa,listaa[100][100];
    f5>>n;
    while(f5>>x>>y){
        listaa[x][1]++;
        frecventa = listaa[x][1];
        listaa[x][frecventa+1]=y;
        listaa[y][1]++;
        frecventa = listaa[y][1];
        listaa[y][frecventa+1]=x;
    }
    f5.close();
    dfs v[n+1];
    for(i=1;i<=n;i++){
        v[i].val=i;
    }
    DFS(listaa,v,n);
}

int main()
{
    //punctul1();
    //punctul2();
    //punctul4();
    punctul5();
    return 0;
}
