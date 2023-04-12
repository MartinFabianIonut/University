import networkx as nx
import numpy as np
import matplotlib.pyplot as plt
import warnings
import time

import FitnessFunction
from GeneticAlgorithm import GeneticAlgorithm

warnings.simplefilter('ignore')
start_time = time.time()


# display our network in a visual manner, so that we can observe
# in witch community a node is
def plotNetwork(networkGraph, foundCommunities, title):
    np.random.seed(1000)
    pos = nx.spring_layout(networkGraph)
    plt.figure(figsize=(16, 12))
    plt.title(title)
    nx.draw(networkGraph, pos, cmap=plt.get_cmap('Set3'), node_color=foundCommunities, with_labels=True)
    plt.show()


if __name__ == '__main__':
    inputs = ["graphs/dolphins.gml", "graphs/football.gml", "graphs/karate.gml", "graphs/krebs.gml",
              "graphs/france.gml", "graphs/dogs.gml", "graphs/painters.gml", "graphs/musicians.gml",
              "graphs/actors.gml", "graphs/writers.gml",
              "graphs/com-dblp.gml", "graphs/lesmis.gml", "graphs/netscience.gml"]
    outputs = ["outputs/dolphins.txt", "outputs/football.txt", "outputs/karate.txt", "outputs/krebs.txt",
               "outputs/france.txt", "outputs/dogs.txt", "outputs/painters.txt", "outputs/musicians.txt",
               "outputs/actors.txt", "outputs/writers.txt",
               "outputs/com-dblp.txt", "outputs/lesmis.txt", "outputs/netscience.txt"]
    ideal = ['graphs/dolphins.dat', 'graphs/football.dat', 'graphs/karate.dat', 'graphs/krebs.dat']
    number = 2

    network = nx.read_gml(inputs[number], label='id')

    populationSize = 20
    function = FitnessFunction.myFitnessFunction

    if number <= 3:
        parameters = {'populationSize': populationSize, 'function': function, 'network': network,
                      'idealCommunity': ideal[number]}
    else:
        parameters = {'populationSize': populationSize, 'function': function, 'network': network}

    geneticAlgorithm = GeneticAlgorithm(parameters)
    geneticAlgorithm.initialisation()
    geneticAlgorithm.evaluation()

    numberOfGenerations = 300
    for generation in range(numberOfGenerations):
        geneticAlgorithm.oneGenerationElitism()

    executionTime = str("Time of execution: " + str(time.time() - start_time))
    print(executionTime)

    communities = geneticAlgorithm.bestChromosome().representation

    print("Number of communities: ", len(set(communities)))
    # display the network
    plotNetwork(network, communities, inputs[number])

    # writing the results to output files with append
    file = open(outputs[number], "a")
    file.write("\nPopulation size = " + str(populationSize) + " \nNumber of generation = " +
               str(numberOfGenerations) + '\n')
    file.write(executionTime + '\n')
    file.write(str(function) + '\n')
    file.write("Number of communities: " + str(len(set(communities))) + '\n')
    for position in range(len(communities)):
        text = "{} : {} \n"
        file.write(text.format(position + 1, list(communities)[position]))
    file.close()
