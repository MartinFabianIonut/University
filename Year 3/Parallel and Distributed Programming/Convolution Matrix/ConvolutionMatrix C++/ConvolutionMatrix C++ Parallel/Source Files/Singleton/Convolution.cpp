#include "../../Header Files/Singleton/Convolution.h"
Convolution* Convolution::instance = nullptr;

Convolution* Convolution::getInstance() {
	if (instance == nullptr) {
		instance = new Convolution();
	}
	return instance;
}

int Convolution::applyConvolution(const vector<vector<int>>& inputMatrix,
	const vector<vector<int>>& convolutionMatrix,
	int i, int j) {
	int result = 0;
	int n = inputMatrix.size();
	int m = inputMatrix[0].size();
	int k = convolutionMatrix.size();

	for (int p = 0; p < k; ++p) {
		for (int q = 0; q < k; ++q) {
			int x = min(max(i - k / 2 + p, 0), n - 1);
			int y = min(max(j - k / 2 + q, 0), m - 1);
			result += inputMatrix[x][y] * convolutionMatrix[p][q];
		}
	}

	return result;
}