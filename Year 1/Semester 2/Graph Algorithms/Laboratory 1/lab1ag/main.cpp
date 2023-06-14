#include <iostream>
#include <fstream>

using namespace std;
ifstream f("in.txt");

int c[100],v[100];

int exista_nod_nevizitat(int v[100], int n)
{
    for(int i=1;i<=n;i++)
      if(v[i]==0)
         return i; // primul nod nevizitat
      return 0;   // nu mai exista noduri nevizitate
}

void parcurgere_latime(int a[100][100], int n)
{
    int prim, ultim;
    v[1]=1;
    prim=ultim=1;
    c[ultim]=1;
    while(prim<=ultim)
    {
        for(int i=1;i<=n;i++)
            if(a[c[prim]][i]==1)
                if(v[i]==0)
                {
                    ultim++;
                    c[ultim]=i;
                    v[i]=1;
                }
         prim++;
    }
}

void punctul1()
{
    int n,a,b,poz=1,frecventa; //a si b reprezinta capete
    int adiacenta[100][100]={0},incidenta[100][200]={0},listaa[100][100]={0};
    f>>n;
    while(f>>a>>b){
        adiacenta[a][b]=1;
        adiacenta[b][a]=1;
        incidenta[a][poz]=1;
        incidenta[b][poz]=1;
        poz++;
        listaa[a][1]++;
        frecventa = listaa[a][1];
        listaa[a][frecventa+1]=b;
        listaa[b][1]++;
        frecventa = listaa[b][1];
        listaa[b][frecventa+1]=a;
    }
    int i,j;
    cout<<"Matricea de adiacenta:\n\n";
    for(i=1;i<=n;i++)
    {
        for(j=1;j<=n;j++)
            cout<<adiacenta[i][j]<<" ";
        cout<<"\n";
    }
    cout<<"Lista de adiacenta:\n\n";
    for(i=1;i<=n;i++)
    {
        cout<<i<<": ";
        for(j=2;j<=listaa[i][1]+1;j++)
            cout<<listaa[i][j]<<" ";
        cout<<"\n";
    }
    cout<<"Matricea de incidenta:\n\n";
    for(i=1;i<=n;i++)
    {
        for(j=1;j<poz;j++)
            cout<<incidenta[i][j]<<" ";
        cout<<"\n";
    }
}

void punctul2(){
    int adi[100][100]={0};
    int n,i,j,k,a,b;
    f>>n;
    while(f>>a>>b)
    {
        adi[a][b]=1;
        adi[b][a]=1;
    }

    cout<<"\n";
    int suma_linii[100];
    for (i=1;i<=n;i++)
        for(j=1;j<=n;j++)
            suma_linii[i]+=adi[i][j];
    cout<<"a) Noduri izolate: ";
    bool sunt=false;
    for(i=1;i<=n;i++)
        if(suma_linii[i]==0)
            cout<<i<<" ",sunt=true;
    if(sunt==false)
        cout<<"nu exista.";
    cout<<"\n";
    bool regular=true;
    for (i =2;i<=n;i++)
        if(suma_linii[i]!=suma_linii[1])
            regular=false;
    if(regular)
        cout<<"b) Este regular.";
    else
        cout<<"b) Nu este regular.";
    cout<<"\n";
    int dist[100][100];
    for (i=1;i<=n;i++)
        for(j=1;j<=n;j++)
            dist[i][j]=adi[i][j];

    for (i=1;i<=n;i++)
        for(j=1;j<=n;j++)
            if(dist[i][j]!=1 && i!=j)
                dist[i][j]=9999;
    for (i=1;i<=n;i++)
        for(j=1;j<=n;j++)
            for (k=1;k<=n;k++)
                if(dist[i][j]>dist[i][k]+dist[k][j])
                    dist[i][j]=dist[i][k]+dist[k][j];
    cout<<"\n";
    cout<<"c) Matricea distantelor:\n\n";
    for(i=1;i<=n;i++)
    {
        for(j=1;j<=n;j++)
            cout<<dist[i][j]<<" ";
        cout<<"\n";
    }
    if(sunt==true)
        cout<<"d) Nu e conex.";
    else
    {
        parcurgere_latime(adi,n);
        if(exista_nod_nevizitat(v,n)!=0)
            cout<<"d) NU este conex.";
        else
            cout<<"d) Este conex.";
    }
}





int main()
{
    //punctul1();
    punctul2();
    return 0;
}
