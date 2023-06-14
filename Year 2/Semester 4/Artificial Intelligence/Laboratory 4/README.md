# Shortest Route Finder Algorithm

The Shortest Route Finder Algorithm is designed to solve the task of finding the shortest route that visits all nodes, starting from a specific node. The algorithm employs genetic algorithm techniques to optimize the route and determine the most efficient path.

## Table of Contents

- [Chromosome](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%204/TSPM/Chromosome.py)
- [GeneticAlgorithm](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%204/TSPM/GeneticAlgorithm.py)

## Solution Overview

The algorithm consists of the following classes:

### Chromosome

This class represents a chromosome in the genetic algorithm. Here is a brief description of its key methods:

- `crossover`:
  - _crossoverSimple_, _crossoverComplex_, and _crossoverLarge_ methods perform crossover operations on chromosomes
  - ! Obs: Specifically, for the easy and medium tests, I applied the ordered crossover for permutations as outlined in the course. However, for the hard test, I modified the crossover to incorporate r random mutations of the genes. This was necessary because the ordered crossover would have been inefficient for the hard level.
- `mutation`: introduces a random mutation in the chromosome's representation.

### GeneticAlgorithm

This class encapsulates the genetic algorithm operations. Here is a brief description of its key methods:

- `initialisation`: Initializes the population by creating a set of chromosomes.
- `evaluation`: Evaluates the fitness of each chromosome in the population.
- `bestChromosome`: Returns the best chromosome with the best fitness in the population.
- `allBestChromosomes`: Returns a list of all chromosomes with the best fitness.
- `selection`: Performs selection operation to select a parent chromosome for reproduction.
- `oneGenerationElitism`: Generates a new population by applying crossover and mutation operations, while preserving the best chromosome through elitism.

The algorithm also includes auxiliary functions like readFile and readTsp to read input data files.

## Usage

To use the Shortest Route Finder Algorithm, follow these steps:

1. **Prepare Input Data**: Create or obtain input data files in the required format.

- For easy and medium tests, use text files with weights and start node information: `easy.txt` and `medium.txt`.
- For the hard test, use a TSP (Traveling Salesman Problem) file format: `mona-lisa100K.tsp`.

2. **Set Algorithm Parameters**: Configure the algorithm parameters according to the input data. Specify the following:

- `population size`
- `fitness function`
- `network`
- `number of nodes`
- `start node` and
- `permutation type`.

3. **Run the Algorithm**: Execute the algorithm by running the provided code or script. Ensure that the input data files are accessible, and the algorithm parameters are correctly set.

4. **Analyze Results**: After the algorithm completes, review the output. The algorithm outputs the best path and weight found for the shortest route. Additionally, all the best paths found by the algorithm are displayed.

## Output

The algorithm generates the following output:

- `.txt` files: Each file contains:
  - `Time of execution`
  - `Population size`
  - `Number of generation`
  - `Number of nodes` in the path
  - `The path`
  - `The weight` of the path

## Conclusion

The Shortest Route Finder Algorithm provides a solution to finding the shortest route that visits all nodes in a given network, starting from a specific node. By utilizing genetic algorithm techniques, the algorithm optimizes the route to minimize the overall weight or distance traveled.
