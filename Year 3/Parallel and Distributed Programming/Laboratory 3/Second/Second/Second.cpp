#include <mpi.h>
#include <iostream>
#include <fstream>
#include <chrono>
#include <string>

using namespace std;

ifstream fin("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 3\\Second\\Second\\input.txt");
ifstream finConvolution("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 3\\Second\\Second\\convolution.txt");
ofstream fout("C:\\GIT\\University\\Year 3\\Parallel and Distributed Programming\\Laboratory 3\\Second\\Second\\output.txt");

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

void writeMatrix(int start, int end)
{
	for (int i = start; i < end; i++)
	{
		for (int j = 0; j < m; j++)
		{
			fout << inputMatrix[i][j] << " ";
		}
		fout << endl;
	}
}

void readLinesFromStartToEnd(int start, int end)
{
	for (int i = start; i < end; i++)
	{
		for (int j = 0; j < n; j++)
		{
			fin >> inputMatrix[i][j];
		}
	}
}

int resultAtConvolute(int i, int j)
{
	int result = 0;
	for (int p = 0; p < k; ++p)
	{
		for (int q = 0; q < k; ++q)
		{
			int x = min(max(i - k / 2 + p, 0), n - 1);
			int y = min(max(j - k / 2 + q, 0), m - 1);
			result += inputMatrix[x][y] * convolutionMatrix[p][q];
		}
	}
	return result;
}

void applyConvolution(int start, int end)
{
	if (myid == 0)
	{
		MPI_Send(inputMatrix[end - 1], m, MPI_INT, myid + 1, 0, MPI_COMM_WORLD);
		MPI_Recv(inputMatrix[end], m, MPI_INT, myid + 1, 0, MPI_COMM_WORLD, &status);
	}
	if (myid > 0 && myid < numprocs - 1)
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

	deallocateMatrix(buffer, end - start);
}

int main(int argc, char* argv[])
{
	MPI_Init(NULL, NULL);
	MPI_Comm_rank(MPI_COMM_WORLD, &myid);
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);
	int start, end;

	chrono::steady_clock::time_point startTimeT1;

	if (myid == 0)
	{
		startTimeT1 = chrono::high_resolution_clock::now();

		for (int i = 0; i < k; i++)
		{
			for (int j = 0; j < k; j++)
			{
				finConvolution >> convolutionMatrix[i][j];
			}
		}
		finConvolution.close();

		MPI_Bcast(convolutionMatrix, k * k, MPI_INT, 0, MPI_COMM_WORLD);

		readLinesFromStartToEnd(0, n);
	}
	else
	{
		MPI_Bcast(convolutionMatrix, k * k, MPI_INT, 0, MPI_COMM_WORLD);
	}
	
	int* auxMatrix = new int[n * m / numprocs];
	
	MPI_Scatter(inputMatrix, n * m / numprocs, MPI_INT, auxMatrix, n * m / numprocs, MPI_INT, 0, MPI_COMM_WORLD);
	
	// copy auxMatrix to inputMatrix
	for (int i = myid * n / numprocs; i < (myid + 1) * n / numprocs; ++i)
	{
		for (int j = 0; j < m; ++j)
		{
			inputMatrix[i][j] = auxMatrix[(i - myid * n / numprocs) * m + j];
		}
	}

	auto startTimeT2 = chrono::high_resolution_clock::now();
	applyConvolution(myid * n / numprocs, (myid + 1) * n / numprocs);
	auto endTimeT2 = chrono::high_resolution_clock::now();

	// copy inputMatrix to auxMatrix
	for (int i = myid * n / numprocs; i < (myid + 1) * n / numprocs; ++i)
	{
		for (int j = 0; j < m; ++j)
		{
			auxMatrix[(i - myid * n / numprocs) * m + j] = inputMatrix[i][j];
		}
	}

	//MPI_Gather to send the lines

	MPI_Gather(auxMatrix, n * m / numprocs, MPI_INT, inputMatrix, n * m / numprocs, MPI_INT, 0, MPI_COMM_WORLD);

	if (myid != 0) {
		auto durationT2 = chrono::duration<double, milli>(endTimeT2 - startTimeT2).count();
		MPI_Send(&durationT2, 1, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
	}
	if (myid == 0)
	{
		writeMatrix(0, n);
		double durationT2Total = 0.0, durationT2;
		for (int i = 1; i < numprocs; ++i)
		{
			MPI_Recv(&durationT2, 1, MPI_DOUBLE, i, 0, MPI_COMM_WORLD, &status);
			durationT2Total += durationT2;
		}

		auto endTimeT1 = chrono::high_resolution_clock::now();
		auto durationT1 = chrono::duration<double, milli>(endTimeT1 - startTimeT1).count();
		auto durationT2Local = chrono::duration<double, milli>(endTimeT2 - startTimeT2).count();

		durationT2Total += durationT2Local;

		string durationT1String = to_string(durationT1);
		string durationT2String = to_string(durationT2Total);
		cout << durationT1String << "," << durationT2String ;
	}

	MPI_Finalize();
}