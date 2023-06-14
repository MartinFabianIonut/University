#include <iostream>
#include <fstream>
#include <vector>
#include <iterator>
using namespace std;
ifstream f("cod.txt");
ofstream g("decodificare.txt");

int main()
{
    int M, i, x, y;
    f >> M;
    vector<int> cod;
    for (i = 0; i < M; i++) {
        int x;
        f >> x;
        cod.push_back(x);
    }

    ///decodare Prufer

    vector<int> tati;
    for(i=0;i<=M;i++){
        tati.push_back(-1);
    }
    for(i=0;i<M;i++){
        vector<int>::iterator it;
        x = *cod.begin();
        int mic[M+1]={0}, k;
        for(it = cod.begin(); it!= cod.end(); it++){
            mic[*it]=1;
        }
        k = 0;
        bool gasit = false;
        while(k<=M && !gasit){
            if(mic[k]==0){
                y = k;
                gasit= true;
            }
            k++;
        }
        it = cod.begin();
        cod.erase(it);
        tati[y]=x;
        cod.push_back(y);
    }
    g<<M+1<<"\n";
    vector<int>::iterator it;
    for (it = tati.begin(); it != tati.end(); it++) {
        g << *it << " ";
    }
    f.close();
    g.close();
    return 0;
}
