#include <iostream>
#include <fstream>
#include <thread>
#include <vector>
#include <chrono>
#include <string>
using namespace std;

const string OUTPUT_PATH = "..\\..\\Outputs\\sequential.txt";
const string INPUT_PATH = "..\\..\\Inputs\\data1000x1000.txt";
const string CONVOLUTION_PATH = "..\\..\\Inputs\\convolution5x5.txt";
const int MAX_N = 1000;
const int MAX_M = 1000;
const int K = 5;

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

int applyConvolution(int i, int j)
{
    int result = 0;
    for (int p = 0; p < K; ++p)
    {
        for (int q = 0; q < K; ++q)
        {
            int x = min(max(i - K / 2 + p, 0), MAX_N - 1);
            int y = min(max(j - K / 2 + q, 0), MAX_M - 1);
            result += inputMatrix[x][y] * convolutionMatrix[p][q];
        }
    }
    return result;
}

int main(int argc, char *argv[])
{
    readMatrixFromFile();

    auto startTime = chrono::high_resolution_clock::now();

    for (int i = 0; i < MAX_N; ++i)
    {
        for (int j = 0; j < MAX_M; ++j)
        {
            resultMatrix[i][j] = applyConvolution(i, j);
        }
    }

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration<double, milli>(endTime - startTime).count();
    string durationString = to_string(duration);
    cout << durationString;

    writeMatrixToFile();

    return 0;
}
