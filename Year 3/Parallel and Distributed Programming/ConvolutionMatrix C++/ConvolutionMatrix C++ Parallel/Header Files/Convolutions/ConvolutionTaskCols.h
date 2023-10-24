#pragma once
#include "../Singleton/Convolution.h"
#include <thread>

class ConvolutionTaskCols : public std::thread {
private:
	const vector<vector<int>>& inputMatrix;
	const vector<vector<int>>& convolutionMatrix;
	vector<vector<int>>& resultMatrix;
	int startCol;
	int endCol;
	Convolution* convolution;

public:
	ConvolutionTaskCols(const vector<vector<int>>& inputMatrix,
	const vector<vector<int>>& convolutionMatrix,
	vector<vector<int>>& resultMatrix, int startCol, int endCol);
	void run();
};