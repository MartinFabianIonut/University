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
    const int n = 169999;
    const int p = 8;
    int a[n], b[n], c[n];

    getRandomValues(a, n);
    getRandomValues(b, n);

    thread threads[p];

    auto t_start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < p; i++)
    {
        threads[i] = thread(sumCyclicArray, i, a, b, c, n, p);
    }
    for (int i = 0; i < p; i++)
    {
        threads[i].join();
    }
    auto t_end = std::chrono::high_resolution_clock::now();
    double elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();
    cout << "Static cyclic elapsed_time_ms = " << elapsed_time_ms << "\n";

    return 0;
}
