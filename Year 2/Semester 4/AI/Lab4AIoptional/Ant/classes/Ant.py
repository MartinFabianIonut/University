from random import randint


class Ant:
    def __init__(self, graph, alpha, beta):
        self.__graph = graph
        self.__alpha = alpha
        self.__beta = beta
        self.__tour = None
        self.__distance = 0

    def distance(self):
        return self.__distance

    def tour(self):
        return self.__tour

    def calculateProbability(self, current_node, next_node, total_heuristic, total_weight):
        heuristic_value = pow(total_weight / (self.__graph[current_node][next_node]['weight'] + 0.0001), self.__alpha)
        pheromone_trace = pow(self.__graph[current_node][next_node]['pheromone'], self.__beta)
        return heuristic_value * pheromone_trace / total_heuristic

    def nextNode(self):
        print(self.__tour)
        thisNode = self.__tour[-1]
        unvisited = set(node for node in self.__graph.nodes if node not in self.__tour)
        maxProbability = 0.0
        nextNode = None
        totalHeuristic = 0
        totalWeight = 0
        for node in unvisited:
            totalWeight += self.__graph[thisNode][node]['weight']
        for node in unvisited:
            totalHeuristic += pow(self.__graph[thisNode][node]['pheromone'], self.__alpha) * \
                              pow(totalWeight / self.__graph[thisNode][node]['weight'], self.__beta)
        for node in unvisited:
            probability = self.calculateProbability(thisNode, node, totalHeuristic, totalWeight)
            if probability >= maxProbability:
                maxProbability = probability
                nextNode = node
        print(nextNode)
        return nextNode

    def findTour(self):
        self.__tour = [randint(1, self.__graph.number_of_nodes() - 1)]
        for i in range(self.__graph.number_of_nodes() - 1):
            self.__tour.append(self.nextNode())
        return self.__tour

    def getDistance(self):
        self.__distance = 0
        for i in range(self.__graph.number_of_nodes() - 1):
            self.__distance += self.__graph[self.__tour[i]][self.__tour[i + 1]]['weight']
        self.__distance += self.__graph[self.__tour[0]][self.__tour[-1]]['weight']
        return self.__distance
