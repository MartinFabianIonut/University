#include<stdio.h>
#include<mpi.h> 
#include<random>
#include<iostream>
#include<stdlib.h> 
#include<time.h>
#include<string>
#include <future>

using namespace std;

void printVector(int v[], int n) {
	for (int i = 0; i < n; i++) {
		cout << v[i] << " ";
	}
	cout << endl;
}

void printFloatVector(float v[], int n) {
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

//int main(int argc, char* argv[])
//{
//	int myid, numprocs, namelen;
//	char processor_name[MPI_MAX_PROCESSOR_NAME];
//	const int n = 12;
//	srand(time(NULL));
//	int a[n], b[n], c[n];
//	MPI_Init(NULL, NULL);
//	MPI_Comm_rank(MPI_COMM_WORLD, &myid);  // get current process id
//	MPI_Comm_size(MPI_COMM_WORLD, &numprocs);      // get number of processeser
//	MPI_Get_processor_name(processor_name, &namelen);
//	if (myid == 0)
//	{
//		for (int i = 0; i < n; i++) {
//			a[i] = rand() % 10;
//			b[i] = rand() % 10;
//		}
//	}
//	int* auxA = new int[n / numprocs];
//	int* auxB = new int[n / numprocs];
//	int* auxC = new int[n / numprocs];
//	MPI_Scatter(a, n / numprocs, MPI_INT, auxA, n / numprocs, MPI_INT, 0, MPI_COMM_WORLD);
//	MPI_Scatter(b, n / numprocs, MPI_INT, auxB, n / numprocs, MPI_INT, 0, MPI_COMM_WORLD);
//	for (int i = 0; i < n / numprocs; i++) {
//		auxC[i] = auxA[i] + auxB[i];
//	}
//	MPI_Gather(auxC, n/numprocs, MPI_INT, c, n/numprocs, MPI_INT, 0, MPI_COMM_WORLD);
//	if (myid == 0) {
//		printVector(a, n);
//		printVector(b, n);
//		printVector(c, n);
//		
//	}
//	MPI_Finalize();
//}



//int main(int argc, char** argv) {
//
//
//	int nprocs, id, n=100;
//	int* a, * b, * c;
//
//
//	a = (int*)malloc(n * n * sizeof(int));
//	b = (int*)malloc(n * n * sizeof(int));
//	c = (int*)malloc(n * n * sizeof(int));
//
//
//
//
//
//	MPI_Init(&argc, &argv);
//	MPI_Comm_size(MPI_COMM_WORLD, &nprocs);
//	MPI_Comm_rank(MPI_COMM_WORLD, &id);
//
//
//
//
//
//	if (id == 0) {
//		for (int i = 0; i < n; i++)
//			for (int j = 0; j < n; j++){
//				a[i * n + j] = rand() % 10; //analog pt a
//				b[i * n + j] = rand() % 10; //analog pt b
//				} 
//	}
//	int* auxa, * auxb, * auxc;
//
//
//	auxa = (int*)malloc(n * n / nprocs * sizeof(int));
//	auxb = (int*)malloc(n * n / nprocs * sizeof(int));
//	auxc = (int*)malloc(n * n / nprocs * sizeof(int));
//
//
//	MPI_Scatter(a, n * n / nprocs, MPI_INT, auxa, n * n / nprocs, MPI_INT, 0, MPI_COMM_WORLD);
//	MPI_Scatter(b, n * n / nprocs, MPI_INT, auxb, n * n / nprocs, MPI_INT, 0, MPI_COMM_WORLD);
//
//
//	for (int i = 0; i < n * n / nprocs; i++) {auxc[i] = auxa[i] + auxb[i];}
//
//
//	MPI_Gather(auxc, n * n / nprocs, MPI_INT, c, n * n / nprocs, MPI_INT, 0, MPI_COMM_WORLD);
//
//
//	if (id == 0) {
//	printVector(a, n);
//	printVector(b, n);
//		printVector(c, n);
//	}
//	MPI_Finalize();
//}


//int main(int argc, char* argv[]) {
//	int nprocs, myrank;
//	int i, value = 0;
//	int* a, * b;
//	MPI_Init(&argc, &argv);
//	MPI_Comm_size(MPI_COMM_WORLD, &nprocs);
//	MPI_Comm_rank(MPI_COMM_WORLD, &myrank);
//	a = (int*)malloc(nprocs * sizeof(int));
//	if (myrank == 0) {
//		
//		for (int i = 0; i < nprocs; i++) {
//			a[i] = i + 1; // 1 2 3
//         
//		}
//	}
//	b = (int*)malloc(sizeof(int));
//	MPI_Scatter(a, 1, MPI_INT, b, 1, MPI_INT, 0, MPI_COMM_WORLD);
//	cout << "Process " << myrank << " received " << *b << endl;
//	b[0] += myrank; // !! se mai adauga si myrank
//	MPI_Reduce(b, &value, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);
//	if (myrank == 0) {
//		printf("value = %d\n", value);
//		}
//	MPI_Finalize();
//	return 0;
//}
// 
// 
// 
//
//int main(int argc, char* argv[]) {
//
//
//	int nprocs, id, n;
//	float* a, * auxa;
//	n = 9;
//	a = (float*)malloc(sizeof(float) * n);
//	auxa = (float*)malloc(sizeof(float) * n);
//
//
//	MPI_Init(&argc, &argv);
//	MPI_Comm_size(MPI_COMM_WORLD, &nprocs);
//	MPI_Comm_rank(MPI_COMM_WORLD, &id);
//
//	if (id == 0) {
//		for (int i = 0; i < n; i++) {
//			// a[i] gets notes with 2 decimals
//			a[i] = (rand() % 100) / 10.0;
//		}
//		printFloatVector(a, n);
//	}
//
//	float sum = 0, sum2 = 0;
//	MPI_Scatter(a, n/nprocs, MPI_FLOAT, auxa, n/nprocs, MPI_FLOAT, 0, MPI_COMM_WORLD);
//	for (int i = 1; i < n / nprocs; i++) {
//		auxa[0] += auxa[i];
//	}
//	MPI_Reduce(auxa, &sum2, 1, MPI_FLOAT, MPI_SUM, 0, MPI_COMM_WORLD);
//
//
//	//if (id == 0) {
//		cout << "Suma este: " << sum2 << endl;
//	//}
//
//	MPI_Finalize();
//}


int main(int argc, char* argv[]) {
    int nprocs, myrank;
    int i;
    int* a, * b;
    MPI_Status status;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &nprocs);
    MPI_Comm_rank(MPI_COMM_WORLD, &myrank);
    a = (int*)malloc(nprocs * sizeof(int));
    b = (int*)malloc(nprocs * nprocs * sizeof(int));
    for (int i = 0; i < nprocs; i++) a[i] = nprocs * myrank + i;
    for (i = 0; i < nprocs; i++) b[i + nprocs * myrank] = a[i];

    if (myrank > 0) MPI_Recv(b, nprocs * (myrank + 1), MPI_INT, (myrank - 1), 10, MPI_COMM_WORLD, &status);
    MPI_Send(b, nprocs * (myrank + 1), MPI_INT, (myrank + 1) % nprocs, 10, MPI_COMM_WORLD);
    if (myrank == 0) MPI_Recv(b, nprocs * nprocs, MPI_INT, (nprocs - 1), 10, MPI_COMM_WORLD, &status);

    if (myrank == 0)
        for (i = 0; i < nprocs * nprocs; i++) printf(" %d", b[i]);
    MPI_Finalize();
    return 0;
}
