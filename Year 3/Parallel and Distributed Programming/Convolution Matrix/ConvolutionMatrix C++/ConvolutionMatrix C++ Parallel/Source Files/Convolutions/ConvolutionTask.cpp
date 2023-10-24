#include "../../Header Files/Convolutions/ConvolutionTask.h"

ConvolutionTask::ConvolutionTask(const vector<vector<int>>& inputMatrix,
	const vector<vector<int>>& convolutionMatrix,
	vector<vector<int>>& resultMatrix,
	int start, int end) :
	inputMatrix(inputMatrix),
	convolutionMatrix(convolutionMatrix),
	resultMatrix(resultMatrix),
	start(start),
	end(end) {
	convolution = Convolution::getInstance();
}

void ConvolutionTask::run() {
	int M = inputMatrix[0].size();
	for (int i = start; i < end; ++i) {
		for (int j = 0; j < M; ++j) {
			resultMatrix[i][j] = convolution->applyConvolution(inputMatrix, convolutionMatrix, i, j);
		}
	}
}