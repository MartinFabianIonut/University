#pragma once
#include <vector>
using namespace std;
class Convolution {
private:
	static Convolution* instance;

	Convolution() {}

public:
	static Convolution* getInstance();

	int applyConvolution(const vector<vector<int>>& inputMatrix,
		const vector<vector<int>>& convolutionMatrix,
		int i, int j);
};