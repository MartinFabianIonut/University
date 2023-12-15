// daca in fisierul de intrare am reguli de productie de forma:
// Neterminal -> Compus
// Compus fiind o secventa de neterminale si terminale
// atunci determina gramatica

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <algorithm>
#include <set>
#include <map>

using namespace std;

ifstream f("gramatica.in");

map<string, string> gramatica;
vector<string> reguli;
char S;

void citire()
{
    string neterminal, compus;
    string line;
    bool first = true;
    while (getline(f, line))
    {
        int poz = line.find("->");
        neterminal = line.substr(0, poz);
        if (first)
        {
            S = neterminal[0];
            first = false;
        }
        compus = line.substr(poz + 2, line.size() - poz - 2);
        if (gramatica.find(neterminal) == gramatica.end())
        {
            gramatica[neterminal] = compus;
        }
        else
        {
            if (compus != "ε")
                gramatica[neterminal] += compus;
        }
        reguli.push_back(line);
    }
    f.close();
}

bool isTerminal(char c)
{
    return !(c >= 'A' && c <= 'Z');
}

int main()
{
    citire();
    // determina neterminalele
    set<char> neterminale;
    for (auto it : gramatica)
    {
        neterminale.insert(it.first[0]);
    }
    // determina terminalele
    set<string> terminale;
    for (auto it : gramatica)
    {
        for (auto c : it.second)
        {
            if (isTerminal(c))
            {
                terminale.insert(string(1, c));
            }
        }
    }
    // afisare gramatica
    cout << "Gramatica este:\n";
    cout << "Simbolul de start este: " << S << "\n";
    cout << "Neterminalele sunt: ";
    for (auto it : neterminale)
    {
        cout << it << " ";
    }
    cout << "\nTerminalele sunt: ";
    for (auto it : terminale)
    {
        cout << it << " ";
    }
    cout << "\nRegulile de productie sunt:\n";
    for (auto it : reguli)
    {
        cout << it << "\n";
    }
    return 0;
}