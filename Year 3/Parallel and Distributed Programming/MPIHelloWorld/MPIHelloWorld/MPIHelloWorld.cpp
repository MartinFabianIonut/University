#include <iostream>
#include <mpi.h>

using namespace std;

int main()
{
	int my_rank;
	int world_size;

	MPI_Init(NULL, NULL);

	MPI_Comm_size(MPI_COMM_WORLD, &world_size);
	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);

	cout << "Hello World from process " << my_rank << " out of " << world_size << " processes!!!" << endl;

	MPI_Finalize();
	return 0;
}