#include <iostream>
#include <thread>
#include <random>
#include <chrono>
using namespace std;

void sumArrays(int *a, int *b, int *c, int start, int end)
{
    for (int i = start; i < end; i++)
    {
        c[i] = a[i] + b[i];
    }
}

void sumCyclicArray(int id, int *a, int *b, int *c, int n, int p)
{
    for (int i = id; i < n; i += p)
    {
        c[i] = a[i] + b[i];
    }
}

void printTheArray(int *a, int n)
{
    for (int i = 0; i < n; i++)
    {
        cout << a[i] << " ";
    }
    cout << endl;
}

void getRandomValues(int *v, int n)
{
    random_device rd;
    mt19937 gen(rd());                     // seed the generator
    uniform_int_distribution<> dis(1, 10); // generate random integers between 1 and 10

    for (int i = 0; i < n; i++)
    {
        v[i] = dis(gen);
    }
}

int main()
{
    const int n = 1000000;
    const int p = 8;
    thread threads[p];

    int *aDinamic = new int[n];
    int *bDinamic = new int[n];
    int *cDinamic = new int[n];

    getRandomValues(aDinamic, n);
    getRandomValues(bDinamic, n);

    auto t_start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < p; i++)
    {
        threads[i] = thread(sumCyclicArray, i, aDinamic, bDinamic, cDinamic, n, p);
    }
    for (int i = 0; i < p; i++)
    {
        threads[i].join();
    }
    auto t_end = std::chrono::high_resolution_clock::now();
    auto elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();

    cout << "Dynamic cyclic elapsed_time_ms = " << elapsed_time_ms << "\n";

    return 0;
}
