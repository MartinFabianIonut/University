# Evolutionary algorithms for determining communities

## Introduction

The Optimal Subgroup Identification Algorithm is designed to identify optimal subgroups within a larger community. It takes input in the form of `.gml` files and produces output in the form of `.txt` files and a graphical representation using the matplotlib.pyplot library.

### Solution Overview

The algorithm consists of the following components:

1. **Input Data**: The algorithm requires input data in the form of `.gml` files. These files contain graph data representing the community structure.

2. **Community Detection**: The algorithm applies a community detection method to identify subgroups within the community. This process involves analyzing the connectivity patterns of nodes and partitioning them into distinct communities.

3. **Output Generation**: Once the subgroups are identified, the algorithm generates output files in the .txt format. These files provide information about each node and its assigned community, facilitating further analysis.

4. **Visualization**: The algorithm utilizes the matplotlib.pyplot library to create visualizations of the identified subgroups. The generated graph visualizations color-code and label each node according to its community membership, allowing for a clear representation of the community's structure.

5. **Fitness Calculation**: The algorithm includes two additional functions to improve the fitness calculation based on the input. The first function reinterprets modularity, while the second function applies to graphs that can be ideally divided into communities. These functions enhance the accuracy of the fitness calculation.

6. **Execution on Additional Input Sets**: The algorithm is designed to be versatile and can be executed on different input sets. It includes predefined input sets such as `lesmis.gml` and `netscience.gml`. Additionally, the algorithm can process custom input provided for the previous lab, ensuring flexibility in analyzing various community structures.

## Output

The algorithm generates the following output:

- `.txt` files: Each file contains:
  - `Population size`
  - `Number of generation`
  - `Time of execution`
  - `Number of communities` and
  - The node-community mappings in the format `node: community`. These files provide information about the optimal subgroups identified within the community.
- Graphical visualization: The algorithm uses the matplotlib.pyplot library to create a graphical representation of the community structure. The graph distinguishes between different communities by color-coding and labeling each node with its ID.

## Observation

!!! In addition to implementing modularity, I've created two functions. The first function reinterprets modularity (`myFitnessFunction`), while the second function (`myFitnessFunction2`) applies to graphs that can be ideally divided into communities. This allows me to improve the fitness calculation based on that input.

## Table of Contents

- [Chromosome](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%203/CommunitesG/Chromosome.py)
- [GeneticAlgorithm](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%203/CommunitesG/GeneticAlgorithm.py)

### Chromosome

The Chromosome class represents a chromosome in a genetic algorithm. It is used to encode and manipulate the genetic representation of an individual. Here is a brief description of its key methods:

- `crossover`: Performs crossover operation with another chromosome c and returns a new chromosome as the offspring.
- `mutation`: Performs a mutation operation on the chromosome by randomly modifying a gene.

### GeneticAlgorithm

The GeneticAlgorithm class represents the main framework of the genetic algorithm. It handles the population, evolution, and selection processes. Here is a brief description of its key methods:

- `initialisation`: Initializes the population by creating a set of chromosomes.
- `evaluation`: Evaluates the fitness of each chromosome in the population.
- `bestChromosom`e: Returns the best chromosome in the current population based on fitness.
- `selection`: Performs selection operation to select a parent chromosome for reproduction.
- `oneGenerationElitism`: Generates a new population by applying crossover and mutation operations, while preserving the best chromosome through elitism.

These classes and their methods form the core components of the genetic algorithm for identifying optimal subgroups within a community.
