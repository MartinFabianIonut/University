# Ant Colony Optimization (ACO) - Shortest Path with Complete Graph

## Introduction

This code implements an ant-inspired algorithm known as Ant Colony Optimization (ACO) to find the shortest path that starts from one node, visits all nodes, and returns to the starting point. The ACO algorithm utilizes pheromone trails left by ants to guide the search for the optimal path.

## Table of Contents

- [Ant](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%204%20Optional/Ant/classes/Ant.py)
- [BestAntTour](https://github.com/MartinFabianIonut/University/blob/main/Year%202/Semester%204/Artificial%20Intelligence/Laboratory%204%20Optional/Ant/classes/BestAntTour.py)

### Ant

The Ant class represents an ant in the ACO algorithm. It takes a graph, alpha and beta parameters to calculate the probability of moving to the next node based on pheromone levels and heuristic information.

The key methods of the Ant class are:

- `calculateProbability`: Calculates the probability of moving from the current node to the next node.
- `nextNode`: Selects the next node to visit based on the probability calculations.
- `findTour`: Finds the complete tour by iteratively selecting the next node.
- `getDistance`: Calculates and returns the total distance of the tour.

### BestAntTour

The BestAntTour class manages a collection of ants and performs the ACO algorithm to find the best tour. It takes a set of properties including the graph, alpha, beta, size, steps, and rho.

The main methods of the BestAntTour class are:

- `initialise`: Creates and initializes a set of ants based on the specified properties.
- `oneStep`: Executes one step of the ACO algorithm, updating the pheromone levels and tracking the best tour.
- `addPheromone`: Updates the pheromone levels on the graph based on the tour and its distance.
- `run`: Runs the ACO algorithm for the specified number of steps.

## Inputs

This project includes example datasets to test the ACO algorithm:

- `test1.txt`: A sample static graph dataset.
- `test2.txt`: Another sample static graph dataset.
- `aves-sparrow-social.edges`: A dynamic graph dataset representing social interactions among sparrows.
- `insecta-ant-colony1.edges`: A dynamic graph dataset representing ant colony interactions.

## Acknowledgements

This project acknowledges the following research paper for providing the Network Data Repository:

- Title: "The Network Data Repository with Interactive Graph Analytics and Visualization"

  Authors: Ryan A. Rossi and Nesreen K. Ahmed

  Year: 2015

  URL: http://networkrepository.com
