#pragma once
#include <vector>
#include "../Singleton/Convolution.h"
using namespace std;

class ConvolutionTask {
private:
	const vector<vector<int>>& inputMatrix;
	const vector<vector<int>>& convolutionMatrix;
	vector<vector<int>>& resultMatrix;
	int start;
	int end;
	Convolution* convolution;

public:
	ConvolutionTask(const vector<vector<int>>& inputMatrix,
		const vector<vector<int>>& convolutionMatrix,
		vector<vector<int>>& resultMatrix,
		int start, int end);

	void run();

	void operator()() {
		run();
	}
};