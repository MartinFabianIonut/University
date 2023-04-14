import os
import time

import networkx as nx

import FitnessFunction
from GeneticAlgorithm import GeneticAlgorithm

start_time = time.time()


def readFile(filePath):
    networkGraph = {}
    weights = []
    with open(filePath, 'r') as file:
        noNodes = int(file.readline())
        for i in range(0, noNodes):
            line_weights = []
            line = file.readline().strip().split(' ')
            for weight in line:
                line_weights.append(int(weight))
            weights.append(line_weights)
        start = int(file.readline())
    networkGraph['noOfNodes'] = noNodes
    networkGraph['matrix'] = weights
    networkGraph['start'] = start
    return networkGraph


def readTsp(filePath):
    networkGraph = nx.Graph()
    with open(filePath, "r") as file:
        for i in range(100000):
            node, longitude, latitude = file.readline().strip().split()
            networkGraph.add_node(int(node) - 1, x=int(longitude), y=int(latitude))
    return networkGraph

def readCsv(filePath):

if __name__ == '__main__':
    inputs = ['easy.txt', 'medium.txt', 'mona-lisa100K.tsp']
    outputs = ['outputs/easy_solution.txt', 'outputs/medium_solution.txt', 'outputs/hard_mona-lisa_solution.txt']
    number = 1
    currentDirector = os.getcwd()
    path = os.path.join(currentDirector, 'graphs', inputs[number])
    if number < 2:
        network = readFile(path)
    else:
        network = readTsp(path)

    populationSize = 20
    # function = FitnessFunction.fitness
    function = FitnessFunction.fitnessLarge
    if number < 2:
        parameters = {'populationSize': populationSize, 'function': function, 'network': network,
                      'noOfNodes': network['noOfNodes'], 'start': network['start'], 'permutation': 'simple'}
    else:
        parameters = {'populationSize': populationSize, 'function': function, 'network': network,
                      'noOfNodes': network.number_of_nodes(), 'permutation': 'large'}

    geneticAlgorithm = GeneticAlgorithm(parameters)
    geneticAlgorithm.initialisation()
    geneticAlgorithm.evaluation()

    numberOfGenerations = 100
    for generation in range(numberOfGenerations):
        geneticAlgorithm.oneGenerationElitism()

    executionTime = str("Time of execution: " + str(time.time() - start_time))
    print(executionTime)

    path = geneticAlgorithm.bestChromosome().representation
    weight = geneticAlgorithm.bestChromosome().fitness

    print("The path: ", path)
    print("The weight: ", weight)

    print("All best paths: \n")
    paths = {}
    for aPath in geneticAlgorithm.allBestChromosomes():
        paths[str(aPath.representation)] = aPath.fitness
    for myPath in paths:
        print(myPath)

    # writing the results to output files with append
    file = open(outputs[number], "a")
    file.write("\n" + executionTime + "\nPopulation size = " + str(populationSize) + " \nNumber of generation = " +
               str(numberOfGenerations) + '\n')
    # file.write('\n' + str(network['noOfNodes']) + '\n')
    file.write('\n' + str(network.number_of_nodes()) + '\n')
    file.write(str(path) + '\n')
    file.write(str(weight) + '\n')
    file.close()
