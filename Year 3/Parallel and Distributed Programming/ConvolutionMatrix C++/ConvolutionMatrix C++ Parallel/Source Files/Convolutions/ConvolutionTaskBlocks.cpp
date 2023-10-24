#include "../../Header Files/Convolutions/ConvolutionTaskBlocks.h"

ConvolutionTaskBlocks::ConvolutionTaskBlocks(const vector<vector<int>>& inputMatrix,
	const vector<vector<int>>& convolutionMatrix,
	vector<vector<int>>& resultMatrix, int startIdx, int endIdx)
	: inputMatrix(inputMatrix), convolutionMatrix(convolutionMatrix), resultMatrix(resultMatrix), startIdx(startIdx), endIdx(endIdx) {
	convolution = Convolution::getInstance();
}

void ConvolutionTaskBlocks::run() {
	int N = sizeof(inputMatrix) ;
	int M = sizeof(inputMatrix[0]);
	for (int idx = startIdx; idx < endIdx && idx < N * M; idx++) {
		int i = idx / M;
		int j = idx % M;
		resultMatrix[i][j] = convolution->applyConvolution(inputMatrix, convolutionMatrix, i, j);
	}
}