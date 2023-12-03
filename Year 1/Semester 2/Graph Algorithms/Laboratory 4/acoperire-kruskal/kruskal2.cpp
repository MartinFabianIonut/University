#include <iostream>
#include <fstream>
#include <algorithm>

#define mmax 400003
#define nmax 200003

using namespace std;

struct info
{
    int cost, x, y;
};

info edges[mmax];
int v[nmax], sol[nmax];

bool sortare(info &a, info &b)
{
    if (a.cost < b.cost)
        return true;
    return false;
}

int sef(int a)
{
    if (v[a] == a)
        return a;
    return sef(v[a]);
}

int main()
{
    int n, m, i, a, b, s = 0, j;
    ifstream cin("apm.in");
    ofstream cout("apm.out");
    cin >> n >> m;
    for (i = 0; i < m; i++)
        cin >> edges[i].x >> edges[i].y >> edges[i].cost;

    sort(edges, edges + m, sortare);

    for (i = 1; i <= n; i++)
        v[i] = i;

    j = 0;
    for (i = 0; i < m; i++)
    {
        a = sef(edges[i].x);
        b = sef(edges[i].y);
        if (a != b)
        {
            v[a] = b;
            sol[j] = i;
            j++;
            s += edges[i].cost;
        }
    }
    cout << s << "\n"
         << j << "\n";
    for (i = 0; i < j; i++)
        cout << edges[sol[i]].x << " " << edges[sol[i]].y << "\n";

    return 0;
}