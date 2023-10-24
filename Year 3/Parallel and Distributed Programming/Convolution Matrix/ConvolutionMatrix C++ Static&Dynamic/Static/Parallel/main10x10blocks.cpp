#include <iostream>
#include <fstream>
#include <thread>
#include <vector>
#include <chrono>
#include <string>
using namespace std;

const string OUTPUT_PATH = "..\\..\\Outputs\\parallel.txt";
const string INPUT_PATH = "..\\..\\Inputs\\data10x10.txt";
const string CONVOLUTION_PATH = "..\\..\\Inputs\\convolution3x3.txt";
const int MAX_N = 10;
const int MAX_M = 10;
const int K = 3;

int convolutionMatrix[K][K];
int inputMatrix[MAX_N][MAX_M];
int resultMatrix[MAX_N][MAX_M];

void readMatrixFromFile()
{
    ifstream fin(CONVOLUTION_PATH);
    int n, m;
    fin >> n >> m;
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            fin >> convolutionMatrix[i][j];
    fin.close();
    ifstream fin2(INPUT_PATH);
    fin2 >> n >> m;
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            fin2 >> inputMatrix[i][j];
    fin2.close();
}

void writeMatrixToFile()
{
    ofstream fout(OUTPUT_PATH);
    for (int i = 0; i < MAX_N; i++)
        for (int j = 0; j < MAX_M; j++)
            fout << resultMatrix[i][j] << " ";
    fout.close();
}

void applyConvolution(int startIdx, int endIdx)
{
    int result;

    for (int idx = startIdx; idx < endIdx && idx < MAX_N * MAX_M; idx++)
    {
        int i = idx / MAX_M;
        int j = idx % MAX_M;
        result = 0;
        for (int p = 0; p < K; ++p)
        {
            for (int q = 0; q < K; ++q)
            {
                int x = min(max(i - K / 2 + p, 0), MAX_N - 1);
                int y = min(max(j - K / 2 + q, 0), MAX_M - 1);
                result += inputMatrix[x][y] * convolutionMatrix[p][q];
            }
        }
        resultMatrix[i][j] = result;
    }
}

void startThreads(int P)
{
    int start = 0, end;
    int quotient = MAX_N / P;
    int remainder = MAX_N % P;
    vector<thread> threads(P);
    int totalElements = MAX_N * MAX_M;
    int elementsPerThread = totalElements / P;
    for (int i = 0; i < P; ++i)
    {
        end = start + elementsPerThread + (i < remainder ? 1 : 0);
        threads[i] = thread(applyConvolution, start, end);
        start = end;
    }
    for (int i = 0; i < P; ++i)
    {
        threads[i].join();
    }
}

int main(int argc, char *argv[])
{

    if (argc < 1)
    {
        cerr << "Usage: ./your_program <num_threads>\n";
        return 1;
    }

    const int P = stoi(argv[1]);

    readMatrixFromFile();

    auto startTime = chrono::high_resolution_clock::now();

    startThreads(P);

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration<double, milli>(endTime - startTime).count();
    string durationString = to_string(duration);
    cout << durationString;

    writeMatrixToFile();

    return 0;
}
