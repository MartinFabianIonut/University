from random import randint

import FitnessFunction
from Chromosome import Chromosome


class GeneticAlgorithm:
    def __init__(self, parameters=None):
        self.__parameters = parameters
        self.__population = []

    @property
    def population(self):
        return self.__population

    def initialisation(self):
        for _ in range(0, self.__parameters['populationSize']):
            c = Chromosome(self.__parameters)
            self.__population.append(c)

    def evaluation(self):
        for c in self.__population:
            # this function (myFitnessFunction2) compares a chromosome with the ideal separation in communities
            if self.__parameters['function'] == FitnessFunction.myFitnessFunction2:
                c.fitness = self.__parameters['function'](self.__parameters['network'], c.representation,
                                                          self.__parameters['idealCommunity'])
            else:
                c.fitness = self.__parameters['function'](self.__parameters['network'], c.representation)

    def bestChromosome(self):
        best = self.__population[0]
        for c in self.__population:
            if c.fitness > best.fitness:
                best = c
        return best

    def selection(self):
        pos1 = randint(0, self.__parameters['populationSize'] - 1)
        pos2 = randint(0, self.__parameters['populationSize'] - 1)
        if self.__population[pos1].fitness > self.__population[pos2].fitness:
            return pos1
        else:
            return pos2

    def oneGenerationElitism(self):
        newPop = [self.bestChromosome()]
        for _ in range(self.__parameters['populationSize'] - 1):
            p1 = self.__population[self.selection()]
            p2 = self.__population[self.selection()]
            off = p1.crossover(p2)
            off.mutation()
            newPop.append(off)
        self.__population = newPop
        self.evaluation()
