#include<mpi.h> 
#include<iostream>
#include<fstream>
#include <chrono>
#include <string>

using namespace std;

ifstream fin("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 3\\First\\First\\input.txt");
ifstream finConvolution("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 3\\First\\First\\convolution.txt");
ofstream fout("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 3\\First\\First\\output.txt");

int myid, numprocs;
MPI_Status status;

const int n = 1000;
const int m = 1000;
const int k = 3;

int inputMatrix[n][n];
int convolutionMatrix[k][k];

int** allocateMatrix(int n, int m)
{
	int** matrix = new int* [n];
	for (int i = 0; i < n; ++i)
		matrix[i] = new int[m];
	return matrix;
}

void deallocateMatrix(int** matrix, int n)
{
	for (int i = 0; i < n; ++i)
		delete[] matrix[i];
	delete[] matrix;
}

void writeMatrix(int start, int end) {
	for (int i = start; i < end; i++) {
		for (int j = 0; j < m; j++) {
			fout << inputMatrix[i][j] << " ";
		}
		fout << endl;
	}
}

void readLinesFromStartToEnd(int start, int end) {
	for (int i = start; i < end; i++) {
		for (int j = 0; j < n; j++) {
			fin >> inputMatrix[i][j];
		}
	}
}

int resultAtConvolute(int i, int j) {
	int result = 0;
	for (int p = 0; p < k; ++p) {
		for (int q = 0; q < k; ++q) {
			int x = min(max(i - k / 2 + p, 0), n - 1);
			int y = min(max(j - k / 2 + q, 0), m - 1);
			result += inputMatrix[x][y] * convolutionMatrix[p][q];
		}
	}
	return result;
}

void applyConvolution(int start, int end)
{
	if (myid == 1)
	{
		MPI_Send(inputMatrix[end - 1], m, MPI_INT, myid + 1, 0, MPI_COMM_WORLD);
		MPI_Recv(inputMatrix[end], m, MPI_INT, myid + 1, 0, MPI_COMM_WORLD, &status);
	}
	if (myid > 1 && myid < numprocs - 1)
	{
		MPI_Recv(inputMatrix[start - 1], m, MPI_INT, myid - 1, 0, MPI_COMM_WORLD, &status);
		MPI_Send(inputMatrix[start], m, MPI_INT, myid - 1, 0, MPI_COMM_WORLD);
		MPI_Send(inputMatrix[end - 1], m, MPI_INT, myid + 1, 0, MPI_COMM_WORLD);
		MPI_Recv(inputMatrix[end], m, MPI_INT, myid + 1, 0, MPI_COMM_WORLD, &status);
	}
	if (myid == numprocs - 1)
	{
		MPI_Recv(inputMatrix[start - 1], m, MPI_INT, myid - 1, 0, MPI_COMM_WORLD, &status);
		MPI_Send(inputMatrix[start], m, MPI_INT, myid - 1, 0, MPI_COMM_WORLD);
	}

	int** buffer = allocateMatrix(end - start + 1, m);

	for (int i = start; i < end; ++i)
	{
		for (int j = 0; j < m; ++j)
		{
			buffer[i - start][j] = resultAtConvolute(i, j);
		}
	}

	for (int i = start; i < end; ++i)
	{
		for (int j = 0; j < m; ++j)
		{
			inputMatrix[i][j] = buffer[i - start][j];
		}
	}

	deallocateMatrix(buffer, end - start + 1);
}

int main(int argc, char* argv[])
{
	MPI_Init(NULL, NULL);
	MPI_Comm_rank(MPI_COMM_WORLD, &myid);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	int start = 0, end;
	if (myid == 0)
	{
		auto startTimeT1 = chrono::high_resolution_clock::now();
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < k; j++) {
				finConvolution >> convolutionMatrix[i][j];
			}
		}
		finConvolution.close();
		MPI_Bcast(convolutionMatrix, k * k, MPI_INT, 0, MPI_COMM_WORLD);
		int cat, rest;
		cat = n / (numprocs - 1);
		rest = n % (numprocs - 1);
		for (int i = 1; i < numprocs; i++) {
			end = start + cat;
			if (rest > 0)
			{
				end++;
				rest--;
			}
			readLinesFromStartToEnd(start, end);
			MPI_Send(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
			MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
			MPI_Send(inputMatrix[start], (end - start) * m, MPI_INT, i, 0, MPI_COMM_WORLD);
			start = end;
		}
		fin.close();

		double totalT2Time = 0.0, durationT2;

		for (int i = 1; i < numprocs; i++) {
			MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
			MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
			MPI_Recv(inputMatrix[start], (end - start) * m, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
			MPI_Recv(&durationT2, 1, MPI_DOUBLE, i, 0, MPI_COMM_WORLD, &status);
			writeMatrix(start, end);
			totalT2Time += durationT2;
		}
		fout.close();
		auto endTimeT1 = chrono::high_resolution_clock::now();

		auto durationT1 = chrono::duration<double, milli>(endTimeT1 - startTimeT1).count();
		string durationT1String = to_string(durationT1);
		string durationT2String = to_string(totalT2Time);

		cout <<  durationT1String << "," << durationT2String;
	}
	else {
		MPI_Bcast(convolutionMatrix, k * k, MPI_INT, 0, MPI_COMM_WORLD);

		MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
		MPI_Recv(inputMatrix[start], (end - start) * m, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

		auto startTimeT2 = chrono::high_resolution_clock::now();
		applyConvolution(start, end);
		auto endTimeT2 = chrono::high_resolution_clock::now();
		auto durationT2 = chrono::duration<double, milli>(endTimeT2 - startTimeT2).count();

		MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
		MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
		MPI_Send(inputMatrix[start], (end - start) * m, MPI_INT, 0, 0, MPI_COMM_WORLD);
		MPI_Send(&durationT2, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);

	}
	MPI_Finalize();
}
