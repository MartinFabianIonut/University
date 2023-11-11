#include<stdio.h>
#include<mpi.h> 
#include<random>
#include<iostream>
#include<stdlib.h> 
#include<time.h>

using namespace std;

void printVector(int v[], int n) {
	for (int i = 0; i < n; i++) {
		cout << v[i] << " ";
	}
	cout << endl;
}

//int main(int argc, char* argv[])
//{
//	int myid, numprocs, namelen;
//	char processor_name[MPI_MAX_PROCESSOR_NAME];
//	const int n = 10;
//	srand(time(NULL));
//	int a[n], b[n], c[n];
//	MPI_Init(NULL, NULL);
//	MPI_Comm_rank(MPI_COMM_WORLD, &myid);  // get current process id
//	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);      // get number of processeser
//	MPI_Get_processor_name(processor_name, &namelen);
//	int start = 0, end;
//	MPI_Status status;
//	if (myid == 0)
//	{
//		for (int i = 0; i < n; i++) {
//			a[i] = rand() % 10;
//			b[i] = rand() % 10;
//		}
//		int cat, rest;
//		cat = n / (numprocs - 1);
//		rest = n % (numprocs - 1);
//		for (int i = 1; i < numprocs; i++) {
//			end = start + cat;
//			if (rest > 0)
//			{
//				end++;
//				rest--;
//			}
//			
//			MPI_Send(&start,1, MPI_INT, i, 0, MPI_COMM_WORLD);
//			MPI_Send(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
//			MPI_Send(a + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
//			MPI_Send(b + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD);
//			start = end;
//		}
//		for (int i = 1; i < numprocs; i++) {
//			MPI_Recv(&start, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//			MPI_Recv(&end, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//			MPI_Recv(c + start, end - start, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
//		}
//		printVector(a, n);
//		printVector(b, n);
//		printVector(c, n);
//	}
//	else {
//		MPI_Recv(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//		MPI_Recv(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//		cout << "Process " << myid << " will receive from " << start << " to " << end << endl;
//		MPI_Recv(a + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//		MPI_Recv(b + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
//		for (int i = start; i < end; i++) {
//			c[i] = a[i] + b[i];
//		}
//		MPI_Send(&start, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
//		MPI_Send(&end, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
//		MPI_Send(c + start, end - start, MPI_INT, 0, 0, MPI_COMM_WORLD);
//	}
//	MPI_Finalize();
//}

int main(int argc, char* argv[])
{
	int myid, numprocs, namelen;
	char processor_name[MPI_MAX_PROCESSOR_NAME];
	const int n = 10;
	srand(time(NULL));
	int a[n], b[n], c[n];
	MPI_Init(NULL, NULL);
	MPI_Comm_rank(MPI_COMM_WORLD, &myid);  // get current process id
	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);      // get number of processeser
	MPI_Get_processor_name(processor_name, &namelen);
	if (myid == 0)
	{
		for (int i = 0; i < n; i++) {
			a[i] = rand() % 10;
			b[i] = rand() % 10;
		}
	}
	int* auxA = new int[n / numprocs];
	int* auxB = new int[n / numprocs];
	int* auxC = new int[n / numprocs];
	MPI_Scatter(a, n / numprocs, MPI_INT, auxA, n / numprocs, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatter(b, n / numprocs, MPI_INT, auxB, n / numprocs, MPI_INT, 0, MPI_COMM_WORLD);
	for (int i = 0; i < n / numprocs; i++) {
		auxC[i] = auxA[i] + auxB[i];
	}
	MPI_Gather(auxC, n/numprocs, MPI_INT, c, n/numprocs, MPI_INT, 0, MPI_COMM_WORLD);
	if (myid == 0) {
		printVector(a, n);
		printVector(b, n);
		printVector(c, n);
	}
	MPI_Finalize();
}