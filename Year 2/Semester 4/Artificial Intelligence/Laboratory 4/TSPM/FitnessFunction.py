import math
from math import sqrt


def fitness(weights, path):
    weight = 0
    for i in range(1, len(path)):
        weight += weights[path[i - 1] - 1][path[i] - 1]
    return weight + weights[path[0] - 1][path[len(path) - 1] - 1]


def euclidianDistance(firstNode, secondNode):
    return math.sqrt((pow((firstNode['x'] - secondNode['x']), 2) + pow((firstNode['y'] - secondNode['y']), 2)))


def fitnessLarge(weights, path):
    weight = 0
    for i in range(len(path) - 2):
        weight += euclidianDistance(weights.nodes[path[i]], weights.nodes[path[i + 1]])
    return weight + euclidianDistance(weights.nodes[path[0]], weights.nodes[path[len(path) - 2]])
