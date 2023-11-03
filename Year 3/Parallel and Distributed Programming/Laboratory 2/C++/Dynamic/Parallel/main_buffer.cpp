#include <iostream>
#include <fstream>
#include <chrono>
#include <string>
#include <thread>
#include <vector>
#include "my_barrier.hpp"

const string OUTPUT_PATH = "..\\..\\Outputs\\parallel.txt";

int **convolutionMatrix;
int **inputMatrix;
int MAX_N, MAX_M, K, P;

int **allocateMatrix(int n, int m)
{
    int **matrix = new int *[n];
    for (int i = 0; i < n; ++i)
        matrix[i] = new int[m];
    return matrix;
}

void deallocateMatrix(int **matrix, int n)
{
    for (int i = 0; i < n; ++i)
        delete[] matrix[i];
    delete[] matrix;
}

void readMatrixFromFile(const string &filePath, int **matrix, int n, int m)
{
    ifstream fin(filePath);
    int nothing;
    fin >> nothing >> nothing;
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < m; ++j)
            fin >> matrix[i][j];
    fin.close();
}

void writeMatrixToFile(const string &filePath, int **matrix, int n, int m)
{
    ofstream fout(filePath);
    for (int i = 0; i < n; ++i)
    {
        for (int j = 0; j < m; ++j)
            fout << matrix[i][j] << " ";
        fout << "\n";
    }
    fout.close();
}

int resultAtConvolute(int **buffer, int j)
{
    int result = 0;
    for (int p = 0; p < K; ++p)
    {
        for (int q = 0; q < K; q++)
        {
            int x = 1 - K / 2 + p;
            int y;
            if (j == 0)
                y = max(j - K / 2 + q, 0);
            else if (j == MAX_M - 1)
                y = min(j - K / 2 + q, MAX_M - 1);
            else
                y = j - K / 2 + q;
            result += buffer[x][y] * convolutionMatrix[p][q];
        }
    }
    return result;
}

void applyConvolution(int start, int end, my_barrier &barrier)
{
    int M = MAX_M;
    int *buffFrontLineStart = new int[M];
    int *buffCurrentLine = new int[M];
    int *buffFrontLineEnd = new int[M];

    // COPY THE FRONT LINE START
    if (start == 0)
    {
        copy(inputMatrix[0], inputMatrix[0] + M, buffFrontLineStart);
    }
    else
    {
        copy(inputMatrix[start - 1], inputMatrix[start - 1] + M, buffFrontLineStart);
    }

    // COPY THE FRONT LINE END
    if (end == MAX_N)
    {
        copy(inputMatrix[MAX_N - 1], inputMatrix[MAX_N - 1] + M, buffFrontLineEnd);
    }
    else
    {
        copy(inputMatrix[end], inputMatrix[end] + M, buffFrontLineEnd);
    }

    barrier.wait();

    // COPY THE CURRENT LINE
    copy(inputMatrix[start], inputMatrix[start] + M, buffCurrentLine);

    // Create matrix of three lines
    int **buffer = allocateMatrix(3, M);
    copy(buffFrontLineStart, buffFrontLineStart + M, buffer[0]);
    copy(buffCurrentLine, buffCurrentLine + M, buffer[1]);
    copy(inputMatrix[start + 1], inputMatrix[start + 1] + M, buffer[2]);

    for (int i = start; i < end - 1; ++i)
    {
        for (int j = 0; j < M; ++j)
        {
            inputMatrix[i][j] = resultAtConvolute(buffer, j);
        }
        copy(buffer[1], buffer[1] + M, buffer[0]);
        copy(buffer[2], buffer[2] + M, buffer[1]);
        if (i + 2 == MAX_N || i + 2 == end)
            copy(buffFrontLineEnd, buffFrontLineEnd + M, buffer[2]);
        else
            copy(inputMatrix[i + 2], inputMatrix[i + 2] + M, buffer[2]);
    }

    for (int j = 0; j < M; ++j)
    {
        inputMatrix[end - 1][j] = resultAtConvolute(buffer, j);
    }

    deallocateMatrix(buffer, 3);
    delete[] buffFrontLineStart;
    delete[] buffCurrentLine;
    delete[] buffFrontLineEnd;
}

void startThreads(my_barrier &barrier)
{
    int start = 0, end;
    int quotient = MAX_N / P;
    int remainder = MAX_N % P;
    vector<thread> threads(P);
    for (int i = 0; i < P; ++i)
    {
        end = start + quotient + (i < remainder ? 1 : 0);
        threads[i] = thread(applyConvolution, start, end, ref(barrier));
        start = end;
    }
    for (int i = 0; i < P; ++i)
    {
        threads[i].join();
    }
}

int main(int argc, char *argv[])
{
    if (argc < 4)
    {
        cout << "Usage: ./your_program <No_of_threads> <MAX_N> <MAX_M> <K>\n";
    }

    P = stoi(argv[1]);
    MAX_N = stoi(argv[2]);
    MAX_M = stoi(argv[3]);
    K = stoi(argv[4]);

    my_barrier barrier(P);

    string INPUT_PATH = "..\\..\\Inputs\\data" + to_string(MAX_N) + "x" + to_string(MAX_M) + ".txt";
    string CONVOLUTION_PATH = "..\\..\\Inputs\\convolution" + to_string(K) + "x" + to_string(K) + ".txt";

    ifstream fin(CONVOLUTION_PATH);
    fin >> K >> K;
    fin.close();
    convolutionMatrix = allocateMatrix(K, K);

    ifstream fin2(INPUT_PATH);
    fin2 >> MAX_N >> MAX_M;
    fin2.close();

    inputMatrix = allocateMatrix(MAX_N, MAX_M);

    readMatrixFromFile(CONVOLUTION_PATH, convolutionMatrix, K, K);
    readMatrixFromFile(INPUT_PATH, inputMatrix, MAX_N, MAX_M);

    auto startTime = chrono::high_resolution_clock::now();

    startThreads(barrier);

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration<double, milli>(endTime - startTime).count();
    string durationString = to_string(duration);

    writeMatrixToFile(OUTPUT_PATH, inputMatrix, MAX_N, MAX_M);

    cout << durationString;

    deallocateMatrix(convolutionMatrix, K);
    deallocateMatrix(inputMatrix, MAX_N);

    return 0;
}
