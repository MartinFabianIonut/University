#include <iostream>
#include <fstream>
#include <vector>
#include <iterator>
using namespace std;
ifstream f("arb.txt");
ofstream g("out.txt");

int main()
{
    int N, i;
    f >> N;
    vector<int> tati;
    vector<int>fii[N];
    for (i = 0; i < N; i++) {
        int x;
        f >> x;
        tati.push_back(x);
        if (x != -1) {
            fii[x].push_back(i);
        }
    }
    ///codare Prufer
    vector<int> cod;
    bool gata = false;
    while (!gata) {
        int poz = 0;
        bool gasit = false;
        while (poz <= N && gasit == false) {
            if (fii[poz].empty()) {
                gasit = true;
            }
            else {
                poz++;
            }
        }
        if (tati[poz] != -1) {
            cod.push_back(tati[poz]);
            vector<int>::iterator it, it2;
            for (it = fii[tati[poz]].begin(); it != fii[tati[poz]].end(); it++) {
                if (*it == poz) {
                    it2 = it;
                }
            }
            fii[tati[poz]].erase(it2);
            fii[poz].push_back(N);
        }
        else {
            gata = true;
        }
    }
    g<<N-1<<"\n";
    vector<int>::iterator it;
    for (it = cod.begin(); it != cod.end(); it++) {
        g << *it << " ";
    }
    f.close();
    g.close();
    return 0;
}
