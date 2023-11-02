#include "../../Header Files/Convolutions/ConvolutionTaskCols.h"

ConvolutionTaskCols::ConvolutionTaskCols(const vector<vector<int>>& inputMatrix, const vector<vector<int>>& convolutionMatrix,
	vector<vector<int>>& resultMatrix, int startCol, int endCol)
	: inputMatrix(inputMatrix), convolutionMatrix(convolutionMatrix), resultMatrix(resultMatrix), startCol(startCol), endCol(endCol) {
	convolution = Convolution::getInstance();
}

void ConvolutionTaskCols::run() {
	int N = sizeof(inputMatrix.size());
	int M = sizeof(inputMatrix[0].size());
	for (int j = startCol; j < endCol && j < M; j++) {
		for (int i = 0; i < N; i++) {
			resultMatrix[i][j] = convolution->applyConvolution(inputMatrix, convolutionMatrix, i, j);
		}
	}
}
