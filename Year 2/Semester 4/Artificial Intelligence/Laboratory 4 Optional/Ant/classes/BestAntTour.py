import math

from classes.Ant import Ant


class BestAntTour:
    def __init__(self, prop):
        self.__properties = prop
        self.__ants = []
        self.__max_distance = math.inf
        self.__best_tour = []
        self.initialise()

    def initialise(self):
        for i in range(self.__properties['size']):
            ant = Ant(self.__properties['graph'], self.__properties['alpha'], self.__properties['beta'])
            self.__ants.append(ant)

    def oneStep(self):
        for ant in self.__ants:
            self.addPheromone(ant.findTour(), ant.getDistance())
            if ant.distance() < self.__max_distance:
                self.__best_tour = ant.tour()
                self.__max_distance = ant.distance()

    def addPheromone(self, tour, distance):
        value = 1 / distance
        for i in range(self.__properties['graph'].number_of_nodes() - 1):
            self.__properties['graph'][tour[i]][tour[i + 1]]['pheromone'] *= 1 - self.__properties['rho']
            self.__properties['graph'][tour[i]][tour[i + 1]]['pheromone'] += value

    def run(self):
        for i in range(self.__properties['steps']):
            self.oneStep()

        return f'Tour: {self.__best_tour};Distance={self.__max_distance}', self.__best_tour