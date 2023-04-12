import networkx as nx


# 1
def modularity(networkGraph, communities):
    M = 2 * networkGraph.number_of_edges()
    Q = 0.0
    if 0 in networkGraph.nodes():
        degrees = [nx.degree(networkGraph, node) for node in range(0, networkGraph.number_of_nodes())]
        end = 0  # defined for a unique case of input: karate.gml, witch does not have node 0
    else:
        degrees = [nx.degree(networkGraph, node) for node in range(1, networkGraph.number_of_nodes())]
        end = 1
    for i in range(0, networkGraph.number_of_nodes() - end):
        for j in range(0, networkGraph.number_of_nodes() - end):
            if communities[i] == communities[j]:
                has = 0
                if networkGraph.has_edge(i, j):
                    has = 1
                Q += (has - degrees[i] * degrees[j] / M)
    return Q * 1 / M


# 2
def myFitnessFunction(networkGraph, communities):
    if 0 in networkGraph.nodes():
        degrees = [nx.degree(networkGraph, node) for node in range(0, networkGraph.number_of_nodes())]
        end = 0  # defined for a unique case of input: karate.gml, witch does not have node 0
    else:
        degrees = [nx.degree(networkGraph, node) for node in range(1, networkGraph.number_of_nodes())]
        end = 1
    noOfEdges = networkGraph.number_of_edges()
    Q = 0
    for componentCommunity in set(communities):
        for node in range(len(communities) - end):
            if componentCommunity == communities[node]:
                m_u = 0
                m = 0
                k_u = 0
                for neighbour in range(len(communities)):
                    if componentCommunity == communities[neighbour]:
                        if networkGraph.has_edge(node, neighbour):
                            m_u += 1
                        else:
                            m += 1
                k_u += degrees[node]
                Q += (float(m_u) / float(noOfEdges) - float(k_u * k_u) / (float(4 * noOfEdges * noOfEdges)))
    return Q


# 3 - work for graphs for witch I know the ideal communities split
def myFitnessFunction2(networkGraph, communities, ideal):
    community = 1
    idealCommunities = [0 for _ in range(networkGraph.number_of_nodes() + 1)]
    with open(ideal) as file:
        for line in file:
            split = line.split(" ")
            numbers = []
            for x in split:
                if x != '\n':
                    numbers.append(int(x))
            for x in numbers:
                idealCommunities[x] = community
            community += 1

    fitness = 0.0
    for i in range(networkGraph.number_of_nodes()):
        fitness += abs(communities[i] - idealCommunities[i] * i)

    return fitness
