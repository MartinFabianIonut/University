from random import randint

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
            if 'start' in self.__parameters:
                c.fitness = self.__parameters['function'](self.__parameters['network']['matrix'], c.representation)
            else:
                c.fitness = self.__parameters['function'](self.__parameters['network'], c.representation)

    def bestChromosome(self):
        best = self.__population[0]
        for c in self.__population:
            if c.fitness < best.fitness:
                best = c
        return best

    def allBestChromosomes(self):
        best = [self.bestChromosome()]
        for c in self.__population:
            if c.fitness == best[0].fitness:
                best.append(c)
        return best

    def selection(self):
        pos1 = randint(0, self.__parameters['populationSize'] - 1)
        pos2 = randint(0, self.__parameters['populationSize'] - 1)
        if self.__population[pos1].fitness < self.__population[pos2].fitness:
            return pos1
        else:
            return pos2

    def oneGenerationElitism(self):
        newPop = [self.bestChromosome()]
        for _ in range(self.__parameters['populationSize'] - 1):
            p1 = self.__population[self.selection()]
            p2 = self.__population[self.selection()]
            if self.__parameters['permutation'] == 'simple':
                off = p1.crossoverSimple(p2)
            elif self.__parameters['permutation'] == 'large':
                off = p1.crossoverLarge(p2)
            else:
                off = p1.crossoverComplex(p2)
            off.mutation()
            newPop.append(off)
        self.__population = newPop
        self.evaluation()
