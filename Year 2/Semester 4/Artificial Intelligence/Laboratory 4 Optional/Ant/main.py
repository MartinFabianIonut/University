import random
import time
from classes.BestAntTour import BestAntTour
import networkx as nx
start_time = time.time()

# acknowledgement:
# @inproceedings{nr-aaai15,
#       title = {The Network Data Repository with Interactive Graph Analytics and Visualization},
#       author={Ryan A. Rossi and Nesreen K. Ahmed},
#       booktitle = {AAAI},
#       url={http://networkrepository.com},
#       year={2015}
#   }


def readGraph(fileName):
    graphToRead = nx.Graph()
    with open(fileName, "r") as file:
        size = int(file.readline().removesuffix('\n'))
        for count in range(size):
            graphToRead.add_node(count)
        for row in range(size):
            line = file.readline().split(',')
            for column in range(size):
                if column == row:
                    continue
                graphToRead.add_edge(row, column, weight=int(line[column]), pheromone=1.0)
            graphToRead.add_edge(row, size - 1, weight=int(line[size - 1].removesuffix('\n')), pheromone=1.0)

    return graphToRead


def readDynamicGraphFromToWeight(fileName):
    graphToRead = nx.Graph()
    maxNode = 0
    with open(fileName, "r") as file:
        # read while the file is not empty
        while True:
            line = file.readline()
            if not line:
                break
            line = line.split(' ')
            graphToRead.add_edge(int(line[0]), int(line[1]), weight=float(line[2]), pheromone=1.0)
            # if node is not in the graph, add it
            if not graphToRead.has_node(int(line[0])):
                graphToRead.add_node(int(line[0]))
            if not graphToRead.has_node(int(line[1])):
                graphToRead.add_node(int(line[1]))
            # remember the max node
            if int(line[0]) > maxNode:
                maxNode = int(line[0])
            if int(line[1]) > maxNode:
                maxNode = int(line[1])
    print('max node: ' + str(maxNode))
    # add missing edges with max weight
    for i in range(1, maxNode+1):
        for j in range(1, maxNode+1):
            if not graphToRead.has_edge(i, j):
                graphToRead.add_edge(i, j, weight=float(999), pheromone=1.0)

    # remove node 0
    return graphToRead


def generateAdjancencyMatrix(lenght):
    matrix = []
    for i in range(lenght):
        matrix.append([])
        for j in range(lenght):
            if i == j:
                matrix[i].append(0)
            else:
                nr = random.randint(1, 12)
                matrix[i].append(nr)
    return matrix


def writeAdjancencyMatrixToFile(matrix, fileName):
    with open(fileName, 'w') as file:
        file.write(str(len(matrix))+ '\n')
        for row in matrix:
            for column in row:
                file.write(str(column))
                file.write(',')
            file.write('\n')


def writeTourToFile(tour, fileName, time):
    with open(fileName, 'a') as file:
        tour1 = tour.split(';')
        file.write(tour1[0] + '\n')
        file.write(tour1[1] + '\n')
        file.write(time + '\n\n')


if __name__ == '__main__':
    # matrix = generateAdjancencyMatrix(120)
    # writeAdjancencyMatrixToFile(matrix, 'inputs/test2.txt')
    nr = 1
    inputs = ['inputs/test1.txt', 'inputs/test2.txt', 'inputs/aves-sparrow-social.edges',
              'inputs/insecta-ant-colony1.edges']
    outputs = ['outputs/test1.txt', 'outputs/test2.txt', 'outputs/aves-sparrow-social.txt',
               'outputs/insecta-ant-colony1.txt']

    if nr < 2:
        graphh = readGraph(inputs[nr])
    else:
        graphh = readDynamicGraphFromToWeight(inputs[nr])
    properties = {'graph': graphh, 'alpha': 1.0, 'beta': 4.0, 'size': 35, 'steps': 5, 'rho': 0.5}
    bat = BestAntTour(properties)
    output, tour = bat.run()
    executionTime = str("Time of execution: " + str(time.time() - start_time))
    writeTourToFile(output, outputs[nr], executionTime)

