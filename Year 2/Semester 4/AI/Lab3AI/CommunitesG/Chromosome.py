from random import randint


class Chromosome:
    def __init__(self, parameters=None):
        self.__parameters = parameters
        self.__representation = []
        for _ in range(parameters['network'].number_of_nodes()):
            gene = randint(0, parameters['network'].number_of_nodes() - 1)
            self.__representation.append(gene)
        self.__fitness = 0.0

    @property
    def representation(self):
        return self.__representation

    @representation.setter
    def representation(self, l=[]):
        self.__representation = l

    def crossover(self, c):
        newRepresentation = []
        k = randint(0, len(self.__representation) - 1)
        for i in range(0, len(self.__representation)):
            r = randint(0, 1)
            if r < 0.5:
                newRepresentation.append(self.__representation[i])
            else:
                newRepresentation.append(c.__representation[i])
        newChromosome = Chromosome(c.__parameters)
        newChromosome.representation = newRepresentation
        return newChromosome

    def mutation(self):
        gene = randint(0, len(self.__representation) - 1)
        self.__representation[gene] = randint(0, len(self.__representation) - 1)

    def __eq__(self, c):
        return self.__representation == c.__representation and self.__fitness == c.__fitness

