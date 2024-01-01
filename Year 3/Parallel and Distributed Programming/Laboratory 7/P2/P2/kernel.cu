#include "cuda_runtime.h"
#include "device_launch_parameters.h"

#include <iostream>
#include <fstream>
#include <chrono>
#include <string>

const std::string OUTPUT_PATH = "C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 7\\P2\\Outputs\\parallel.txt";
using namespace std;

int* convolutionMatrix;
int* inputMatrix;
int* outputMatrix; // Adăugat pentru a stoca rezultatele în CUDA
int MAX_N, MAX_M, K, P;

int* allocateMatrix(int n, int m)
{
    return new int[n * m];
}

void deallocateMatrix(int* matrix)
{
    delete[] matrix;
}

void readMatrixFromFile(const std::string& filePath, int* matrix, int n, int m)
{
    std::ifstream fin(filePath);
    int nothing;
    fin >> nothing >> nothing;
    for (int i = 0; i < n; ++i)
        for (int j = 0; j < m; ++j)
            fin >> matrix[i * m + j];
    fin.close();
}

void writeMatrixToFile(const std::string& filePath, int* matrix, int n, int m)
{
    std::ofstream fout(filePath);
    for (int i = 0; i < n; ++i)
    {
        for (int j = 0; j < m; ++j) {
            fout << matrix[i * m + j] << " ";
        }
        fout << "\n";
    }
    fout.close();
}

__global__ void matrixMultiply(int* inputMatrix, int* convolutionMatrix, int* outputMatrix, int N, int M, int K)
{
    int row = blockIdx.y * blockDim.y + threadIdx.y;
    int col = blockIdx.x * blockDim.x + threadIdx.x;

    if (row < N && col < M)
    {
        int result = 0;
        for (int i = 0; i < K; ++i)
        {
            for (int j = 0; j < K; ++j)
            {
                int x = min(max(row - K / 2 + i, 0), N - 1);
                int y = min(max(col - K / 2 + j, 0), M - 1);

                if (x >= 0 && x < N && y >= 0 && y < M)
                {
                    result += inputMatrix[x * M + y] * convolutionMatrix[i * K + j];
                }
            }
        }
        outputMatrix[row * M + col] = result;
    }
}

void startCUDA()
{
    int* deviceInputMatrix;
    int* deviceConvolutionMatrix;
    int* deviceOutputMatrix;

    cudaMalloc((void**)&deviceInputMatrix, MAX_N * MAX_M * sizeof(int));
    cudaMalloc((void**)&deviceConvolutionMatrix, K * K * sizeof(int));
    cudaMalloc((void**)&deviceOutputMatrix, MAX_N * MAX_M * sizeof(int));

    cudaMemcpy(deviceInputMatrix, inputMatrix, MAX_N * MAX_M * sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(deviceConvolutionMatrix, convolutionMatrix, K * K * sizeof(int), cudaMemcpyHostToDevice);

    dim3 blockSize(16, 16); // Ajustați dimensiunea blocului după necesități
    dim3 gridSize((MAX_M + blockSize.x - 1) / blockSize.x, (MAX_N + blockSize.y - 1) / blockSize.y);

    matrixMultiply << <gridSize, blockSize >> > (deviceInputMatrix, deviceConvolutionMatrix, deviceOutputMatrix, MAX_N, MAX_M, K);

    cudaMemcpy(outputMatrix, deviceOutputMatrix, MAX_N * MAX_M * sizeof(int), cudaMemcpyDeviceToHost);

    cudaFree(deviceInputMatrix);
    cudaFree(deviceConvolutionMatrix);
    cudaFree(deviceOutputMatrix);
}

int main(int argc, char* argv[])
{
    P = 4;
    MAX_N = 1000;
    MAX_M = 1000;
    K = 5;

    string INPUT_PATH = "C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 7\\P2\\Inputs\\data" + to_string(MAX_N) + "x" + to_string(MAX_M) + ".txt";
    string CONVOLUTION_PATH = "C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 7\\P2\\Inputs\\convolution" + to_string(K) + "x" + to_string(K) + ".txt";
    ifstream fin(CONVOLUTION_PATH);
    fin >> K >> K;
    fin.close();
    convolutionMatrix = allocateMatrix(K, K);

    ifstream fin2(INPUT_PATH);
    fin2 >> MAX_N >> MAX_M;
    fin2.close();

    inputMatrix = allocateMatrix(MAX_N, MAX_M);
    // Allocați și inițializați matricea de ieșire pentru CUDA
    outputMatrix = allocateMatrix(MAX_N, MAX_M);

    auto startTime = std::chrono::high_resolution_clock::now();

    readMatrixFromFile(CONVOLUTION_PATH, convolutionMatrix, K, K);
    readMatrixFromFile(INPUT_PATH, inputMatrix, MAX_N, MAX_M);

    startCUDA();

    auto endTime = std::chrono::high_resolution_clock::now();

    auto duration = std::chrono::duration<double, std::milli>(endTime - startTime).count();
    std::string durationString = std::to_string(duration);

    writeMatrixToFile(OUTPUT_PATH, outputMatrix, MAX_N, MAX_M);

    std::cout << durationString;

    deallocateMatrix(convolutionMatrix);
    deallocateMatrix(inputMatrix);
    deallocateMatrix(outputMatrix);

    return 0;
}
