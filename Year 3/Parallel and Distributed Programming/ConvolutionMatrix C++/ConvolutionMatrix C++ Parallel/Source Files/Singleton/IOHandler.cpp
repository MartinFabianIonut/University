#include "../../Header Files/Singleton/IOHandler.h"
#include <fstream>
#include <iostream>

IOHandler* IOHandler::instance = nullptr;

IOHandler* IOHandler::getInstance() {
	if (instance == nullptr) {
		instance = new IOHandler();
	}
	return instance;
}

vector<vector<int>> IOHandler::readMatrixFromFile(const string& fileName) {
	// read the matrix from the file
	vector<vector<int>> matrix;
	ifstream file(fileName);
	if (file.is_open()) {
		int n, m;
		file >> n >> m;
		matrix.resize(n);
		for (int i = 0; i < n; ++i) {
			matrix[i].resize(m);
			for (int j = 0; j < m; ++j) {
				file >> matrix[i][j];
			}
		}
		file.close();
	}
	else {
		cout << "Unable to open file " << fileName << endl;
	}
	return matrix;
}

void IOHandler::writeMatrixToFile(const vector<vector<int>>& matrix, const string& fileName) {
	// write the matrix to the file
	ofstream file(fileName);
	if (file.is_open()) {
		int n = matrix.size();
		int m = matrix[0].size();
		for (int i = 0; i < n; ++i) {
			for (int j = 0; j < m; ++j) {
				file << matrix[i][j] << " ";
			}
			file << endl;
		}
		file.close();
	}
	else {
		cout << "Unable to open file " << fileName << endl;
	}
}