#pragma once
#include "../Singleton/Convolution.h"
#include <thread>

class ConvolutionTaskBlocks : public std::thread {
private:
	const vector<vector<int>>& inputMatrix;
	const vector<vector<int>>& convolutionMatrix;
	vector<vector<int>>& resultMatrix;
	int startIdx;
	int endIdx;
	Convolution* convolution;

public:
	ConvolutionTaskBlocks(const vector<vector<int>>& inputMatrix,
		const vector<vector<int>>& convolutionMatrix,
		vector<vector<int>>& resultMatrix, int startIdx, int endIdx);
	void run();
};