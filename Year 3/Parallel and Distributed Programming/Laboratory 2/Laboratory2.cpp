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

    // printTheArray(a, n);
    // printTheArray(b, n);

    // create threads to compute the sum of arrays a and b in a linear fashion
    thread threads[p];
    int cat = n / p, rest = n % p;
    int start = 0;

    auto t_start = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < p; i++)
    {
        int end = start + cat;
        if (rest > 0)
        {
            end++;
            rest--;
        }
        threads[i] = thread(sumArrays, a, b, c, start, end);
        start = end;
    }

    for (int i = 0; i < p; i++)
    {
        threads[i].join();
    }

    auto t_end = std::chrono::high_resolution_clock::now();
    double elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();

    cout << "Static linear elapsed_time_ms = " << elapsed_time_ms << "\n";

    t_start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < p; i++)
    {
        threads[i] = thread(sumCyclicArray, i, a, b, c, n, p);
    }
    for (int i = 0; i < p; i++)
    {
        threads[i].join();
    }
    t_end = std::chrono::high_resolution_clock::now();
    elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();

    cout << "Static cyclic elapsed_time_ms = " << elapsed_time_ms << "\n";

    int *aDinamic = new int[n];
    int *bDinamic = new int[n];
    int *cDinamic = new int[n];

    getRandomValues(aDinamic, n);
    getRandomValues(bDinamic, n);

    cat = n / p;
    rest = n % p;
    start = 0;
    t_start = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < p; i++)
    {
        int end = start + cat;
        if (rest > 0)
        {
            end++;
            rest--;
        }
        threads[i] = thread(sumArrays, a, b, c, start, end);
        start = end;
    }

    for (int i = 0; i < p; i++)
    {
        threads[i].join();
    }

    t_end = std::chrono::high_resolution_clock::now();
    elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();

    cout << "Dynamic linear elapsed_time_ms = " << elapsed_time_ms << "\n";

    t_start = std::chrono::high_resolution_clock::now();
    for (int i = 0; i < p; i++)
    {
        threads[i] = thread(sumCyclicArray, i, aDinamic, bDinamic, cDinamic, n, p);
    }
    for (int i = 0; i < p; i++)
    {
        threads[i].join();
    }
    t_end = std::chrono::high_resolution_clock::now();
    elapsed_time_ms = std::chrono::duration<double, std::milli>(t_end - t_start).count();

    cout << "Dynamic cyclic elapsed_time_ms = " << elapsed_time_ms << "\n";

    return 0;
}
