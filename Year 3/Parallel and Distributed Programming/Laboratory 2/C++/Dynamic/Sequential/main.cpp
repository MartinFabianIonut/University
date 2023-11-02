#include <iostream>
#include <fstream>
#include <chrono>
#include <string>
using namespace std;

const string OUTPUT_PATH = "..\\..\\Outputs\\sequential.txt";

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

int applyConvolution(int i, int j, int **inputMatrix, int **convolutionMatrix, int n, int m, int k)
{
    int result = 0;
    for (int p = 0; p < k; ++p)
    {
        for (int q = 0; q < k; ++q)
        {
            int x = min(max(i - k / 2 + p, 0), n - 1);
            int y = min(max(j - k / 2 + q, 0), m - 1);
            result += inputMatrix[x][y] * convolutionMatrix[p][q];
        }
    }
    return result;
}

void copyRowFromVectorToMatrix(int **matrix, int *vector, int row, int MAX_M)
{
    for (int i = 0; i < MAX_M; ++i)
    {
        matrix[row][i] = vector[i];
    }
}

void copyVectorToVector(int *vector1, int *vector2, int MAX_M)
{
    for (int i = 0; i < MAX_M; ++i)
    {
        vector2[i] = vector1[i];
    }
}

int main(int argc, char *argv[])
{
    int **convolutionMatrix;
    int **inputMatrix;
    int MAX_N, MAX_M, K;

    if (argc < 3)
    {
        cout << "Usage: ./your_program <MAX_N> <MAX_M> <K>\n";
    }

    MAX_N = stoi(argv[1]);
    MAX_M = stoi(argv[2]);
    K = stoi(argv[3]);

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

    int *buffer1 = new int[MAX_M];
    int *buffer2 = new int[MAX_M];

    for (int j = 0; j < MAX_M; ++j)
    {
        buffer1[j] = applyConvolution(0, j, inputMatrix, convolutionMatrix, MAX_N, MAX_M, K);
    }

    for (int i = 1; i < MAX_N; ++i)
    {

        for (int j = 0; j < MAX_M; ++j)
        {
            buffer2[j] = applyConvolution(i, j, inputMatrix, convolutionMatrix, MAX_N, MAX_M, K);
        }

        copyRowFromVectorToMatrix(inputMatrix, buffer1, i - 1, MAX_M);
        copyVectorToVector(buffer2, buffer1, MAX_M);
    }
    copyRowFromVectorToMatrix(inputMatrix, buffer2, MAX_N - 1, MAX_M);

    auto endTime = chrono::high_resolution_clock::now();

    auto duration = chrono::duration<double, milli>(endTime - startTime).count();
    string durationString = to_string(duration);
    cout << durationString;

    writeMatrixToFile(OUTPUT_PATH, inputMatrix, MAX_N, MAX_M);

    deallocateMatrix(convolutionMatrix, K);
    deallocateMatrix(inputMatrix, MAX_N);

    return 0;
}
