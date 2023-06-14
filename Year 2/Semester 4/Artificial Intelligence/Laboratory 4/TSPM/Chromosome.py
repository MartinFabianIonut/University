from random import randint


def permutationSimple(n):
    permutation = [i for i in range(1, n + 1)]
    pos1 = randint(0, n - 1)
    pos2 = randint(0, n - 1)
    permutation[pos1], permutation[pos2] = permutation[pos2], permutation[pos1]
    return permutation


def permutationComplex(n, start):
    permutation = [i for i in range(1, n + 1)]
    pos1 = randint(0, n - 1)
    pos2 = randint(0, n - 1)
    while permutation[pos1] == start or permutation[pos2] == start or permutation[pos1] == permutation[pos2]:
        pos1 = randint(0, n - 1)
        pos2 = randint(0, n - 1)
    myPermutation = [start, permutation[pos1], permutation[pos2], start]
    return myPermutation


class Chromosome:
    def __init__(self, parameters=None):
        self.__parameters = parameters
        if self.__parameters['permutation'] == 'simple' or self.__parameters['permutation'] == 'large':
            self.__representation = permutationSimple(self.__parameters['noOfNodes'])
        else:
            self.__representation = permutationComplex(self.__parameters['noOfNodes'], self.__parameters['start'])
        self.__fitness = 0.0

    @property
    def representation(self):
        return self.__representation

    @representation.setter
    def representation(self, l=[]):
        self.__representation = l

    def crossoverSimple(self, c):
        # orderly crossing
        newRepresentation = []
        position = randint(0, len(self.__representation) // 2)
        length = len(self.__representation) // 2
        for i in range(0, position):
            newRepresentation.append(0)
        for i in range(0, length):
            if position >= len(self.__representation):
                position = 0
            newRepresentation.append(self.__representation[position])
            position += 1

        for i in range(position, len(self.__representation)):
            if c.__representation[i] not in newRepresentation:
                newRepresentation.append(c.__representation[i])
                position += 1

        for i in range(0, len(c.__representation)):
            if position >= len(c.__representation) - 1 or len(newRepresentation) < len(c.__representation):
                position = 0
            if c.__representation[i] not in newRepresentation and len(newRepresentation) < len(c.__representation):
                newRepresentation.append(c.__representation[i])
            elif c.__representation[i] not in newRepresentation:
                newRepresentation[position] = c.__representation[i]
                position += 1

        newChromosome = Chromosome(c.__parameters)
        newChromosome.representation = newRepresentation
        return newChromosome

    def crossoverComplex(self, c):
        r = randint(1, len(self.__representation) - 2)
        newRepresentation = [self.__representation[0]]
        for i in range(1, r + 1):
            newRepresentation.append(self.__representation[i])
        for i in range(1, len(c.__representation) - 1):
            if c.__representation[i] not in newRepresentation:
                newRepresentation.append(c.__representation[i])
                break
        newRepresentation.append(self.__representation[0])
        newChromosome = Chromosome(c.__parameters)
        newChromosome.representation = newRepresentation
        return newChromosome

    def crossoverLarge(self, c):
        newChromosome = self
        r = randint(1, len(self.__representation) - 2)
        for i in range(r):
            newChromosome.mutation()
        return newChromosome

    def mutation(self):
        gene1 = randint(1, len(self.__representation) - 2)
        gene2 = randint(1, len(self.__representation) - 2)
        self.__representation[gene1], self.__representation[gene2] = \
            self.__representation[gene2], self.__representation[gene1]

    def __eq__(self, c):
        return self.__representation == c.__representation and self.__fitness == c.__fitness
