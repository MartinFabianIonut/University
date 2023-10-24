#include "../Header Files/Convolutions/ConvolutionTask.h"
#include "../Header Files/Convolutions/ConvolutionTaskCols.h"
#include "../Header Files/Convolutions/ConvolutionTaskBlocks.h"
#include "../Header Files/Singleton/IOHandler.h"
#include "../Header Files/Paths/InputPaths.h"
#include "../Header Files/Paths/ConvolutionPaths.h"

#include <iostream>
#include <thread>
#include <vector>
#include <chrono>
using namespace std;

const string OUTPUT_PATH = "C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\ConvolutionMatrix C++\\ConvolutionMatrix C++ Parallel\\parallel.txt";

void startThreads(int P, const vector<vector<int>>& inputMatrix, const vector<vector<int>>& convolutionMatrix, vector<vector<int>>& resultMatrix) {
	int N = inputMatrix.size();
	int start = 0, end;
	int quotient = N / P;
	int remainder = N % P;
	vector<thread> threads(P);
	for (int i = 0; i < P; ++i) {
		end = start + quotient;
		if (remainder > 0) {
			end++;
			remainder--;
		}
		threads[i] = thread(ConvolutionTask(inputMatrix, convolutionMatrix, resultMatrix, start, end));
		start = end;
	}
	for (int i = 0; i < P; ++i) {
		threads[i].join();
	}
}

int main(int argc, char* argv[]) {
	if (argc < 4) {
		cerr << "Usage: ./your_program <num_threads> <input_matrix_index> <convolution_matrix_index>\n";
		return 1;
	}

	const int P = stoi(argv[1]);
	int whatMatrixIndex = stoi(argv[2]);
	int whatConvolutionIndex = stoi(argv[3]);

	string whatMatrix = (whatMatrixIndex >= 0 && whatMatrixIndex < 4) ? InputPaths::paths[whatMatrixIndex] : "";
	string whatConvolution = (whatConvolutionIndex >= 0 && whatConvolutionIndex < 2) ? ConvolutionPaths::paths[whatConvolutionIndex] : "";

	if (whatMatrix.empty() || whatConvolution.empty()) {
		cerr << "Invalid input matrix or convolution matrix index.\n";
		return 1;
	}

	IOHandler* ioHandler = IOHandler::getInstance();
	vector<vector<int>> convolutionMatrix = ioHandler->readMatrixFromFile(whatConvolution);
	vector<vector<int>> inputMatrix = ioHandler->readMatrixFromFile(whatMatrix);
	int N = inputMatrix.size();
	int M = inputMatrix[0].size();
	vector<vector<int>> resultMatrix(N, vector<int>(M, 0));

	auto startTime = chrono::high_resolution_clock::now();
	startThreads(P, inputMatrix, convolutionMatrix, resultMatrix);
	auto endTime = chrono::high_resolution_clock::now();
	auto duration = chrono::duration<double, milli>(endTime - startTime).count();
	cout << duration;

	ioHandler->writeMatrixToFile(resultMatrix, OUTPUT_PATH);

	return 0;
}
