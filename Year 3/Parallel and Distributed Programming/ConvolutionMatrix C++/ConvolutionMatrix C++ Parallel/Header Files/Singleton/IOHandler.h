#pragma once
#include <string>
#include <vector>
using namespace std;

class IOHandler {
private:
	static IOHandler* instance;

	IOHandler() {}

public:
	static IOHandler* getInstance();

	vector<vector<int>> readMatrixFromFile(const string& fileName);
	void writeMatrixToFile(const vector<vector<int>>& matrix, const string& fileName);
};