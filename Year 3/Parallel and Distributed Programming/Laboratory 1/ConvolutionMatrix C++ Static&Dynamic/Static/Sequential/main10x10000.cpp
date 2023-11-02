#include <iostream>
#include <fstream>
#include <thread>
#include <vector>
#include <chrono>
#include <string>
using namespace std;

const string OUTPUT_PATH = "..\\..\\Outputs\\sequential.txt";
const string INPUT_PATH = "..\\..\\Inputs\\data10x10000.txt";
const string CONVOLUTION_PATH = "..\\..\\Inputs\\convolution5x5.txt";
const int MAX_N = 10;
const int MAX_M = 10000;
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
    {
        for (int j = 0; j < MAX_M; j++)
            fout << resultMatrix[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}

int main(int argc, char *argv[])
{
    readMatrixFromFile();

    auto startTime = chrono::high_resolution_clock::now();

    int i, j, p, q, result, x, y;
    for (i = 0; i < MAX_N; i++)
    {
        for (j = 0; j < MAX_M; j++)
        {
            result = 0;
            for (p = 0; p < K; p++)
            {
                for (q = 0; q < K; q++)
                {
                    x = min(max(i - K / 2 + p, 0), MAX_N - 1);
                    y = min(max(j - K / 2 + q, 0), MAX_M - 1);
                    result += inputMatrix[x][y] * convolutionMatrix[p][q];
                }
            }
            resultMatrix[i][j] = result;
        }
    }

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration<double, milli>(endTime - startTime).count();
    string durationString = to_string(duration);
    cout << durationString;

    writeMatrixToFile();

    return 0;
}
