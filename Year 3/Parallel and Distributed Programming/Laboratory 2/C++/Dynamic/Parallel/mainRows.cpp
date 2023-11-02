#include <iostream>
#include <fstream>
#include <chrono>
#include <string>
#include <thread>
#include <vector>
#include <condition_variable>
using namespace std;

const string OUTPUT_PATH = "..\\..\\Outputs\\parallel.txt";

int **convolutionMatrix;
int **inputMatrix;
int MAX_N, MAX_M, K, P;
// declare a barrier
condition_variable cv;
mutex barrier_mutex;
int threadsAtBarrier = 0;

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

int resultAtConvolute(int i, int j)
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

void applyConvolution(int start, int end)
{
    unique_lock<mutex> lock(barrier_mutex);

    int **buffer = allocateMatrix(end - start + 1, MAX_M);

    for (int i = start; i < end; ++i)
    {
        for (int j = 0; j < MAX_M; ++j)
        {
            buffer[i - start][j] = resultAtConvolute(i, j);
        }
    }

    // wait for all threads to finish

    threadsAtBarrier++;
    if (threadsAtBarrier == P)
    {
        cv.notify_all();
    }
    else
    {
        cv.wait(lock);
    }
    // copy the buffer to the input matrix
    for (int i = start; i < end; ++i)
    {
        for (int j = 0; j < MAX_M; ++j)
        {
            inputMatrix[i][j] = buffer[i - start][j];
        }
    }
    deallocateMatrix(buffer, end - start + 1);
}

void startThreads()
{
    int start = 0, end;
    int quotient = MAX_N / P;
    int remainder = MAX_N % P;
    vector<thread> threads(P);
    for (int i = 0; i < P; ++i)
    {
        end = start + quotient + (i < remainder ? 1 : 0);
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
    if (argc < 4)
    {
        cout << "Usage: ./your_program <No_of_threads> <MAX_N> <MAX_M> <K>\n";
    }

    P = stoi(argv[1]);
    MAX_N = stoi(argv[2]);
    MAX_M = stoi(argv[3]);
    K = stoi(argv[4]);

    // compute the path to the input file
    string INPUT_PATH = "..\\..\\Inputs\\data" + to_string(MAX_N) + "x" + to_string(MAX_M) + ".txt";
    string CONVOLUTION_PATH = "..\\..\\Inputs\\convolution" + to_string(K) + "x" + to_string(K) + ".txt";

    // Read the convolution matrix
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

    startThreads();

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration<double, milli>(endTime - startTime).count();
    string durationString = to_string(duration);
    cout << durationString;

    writeMatrixToFile(OUTPUT_PATH, inputMatrix, MAX_N, MAX_M);

    deallocateMatrix(convolutionMatrix, K);
    deallocateMatrix(inputMatrix, MAX_N);

    return 0;
}
