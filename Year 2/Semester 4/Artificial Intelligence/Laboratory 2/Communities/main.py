import networkx as nx
import numpy as np
import matplotlib.pyplot as plt
import warnings
import time
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


# Complexity: O(n^2)
def WeightOfNetwork(networkGraph):
    matrix = nx.to_numpy_array(networkGraph, dtype=int)
    weightOfNetwork = 0.0
    for i in range(networkGraph.number_of_nodes()):
        for j in range(networkGraph.number_of_nodes()):
            weightOfNetwork += matrix[i][j]
    weightOfNetwork /= 2.0
    return weightOfNetwork


# Remove edges until the number of connected components in the graph increases with one
# using the betweenness property of graphs, we find at each step what's the edge with the maximum
# betweenness centrality, and we remove that edge.
# Obs: edge_betweenness_centrality() function returns a map for witch the key is a tuple consisting in the nodes
# between we have an edge and the value of betweenness.
def RemoveEdges(networkGraph):
    noConnectedComponents = nx.number_connected_components(networkGraph)
    copy = noConnectedComponents
    while copy <= noConnectedComponents:
        betweenness = nx.edge_betweenness_centrality(networkGraph)
        maxBetweenness = max(betweenness.values())
        for key, value in betweenness.items():
            if float(value) == maxBetweenness:
                networkGraph.remove_edge(key[0], key[1])
        copy = nx.number_connected_components(networkGraph)


# This function returns a map where the key is the id of the node and the value is
# the degree (number of neighbours) of that node
# Complexity: O(n^2) or, if using implicit function, O(n+m)
def DegreesForGraph(networkGraph):
    return dict(networkGraph.degree())
    # # adjacency matrix for the graph
    # currentMatrix = nx.to_numpy_array(networkGraph, dtype=int)
    # # finding the degree of each node, mapped
    # degrees = {}
    # for i in range(networkGraph.number_of_nodes()):
    #     degree = 0
    #     for j in range(networkGraph.number_of_nodes()):
    #         if currentMatrix[i][j] == 1:
    #             degree += 1
    #     degrees[i] = degree
    # return degrees


# This function calculate the modularity (Q) of the current split
# Complexity: O(n^2)
def GetModularityQ(networkGraph, degrees, noOfEdges):
    newDegreesAfterRemovingEdges = DegreesForGraph(networkGraph)
    Q = 0
    components = nx.connected_components(networkGraph)
    for componentCommunity in components:
        m_u = 0
        k_u = 0
        for node in componentCommunity:
            m_u += newDegreesAfterRemovingEdges[node]
            k_u += degrees[node]
        # if we don't divide m_u with 2, we obtain more accurate results for some inputs
        # except for football.gml, where it shows only 6 communities
        # but for dolphins, karate and krebs we have more communities than in our course's examples
        Q += (float(m_u / 2) / float(noOfEdges) - float(k_u * k_u) / (float(4 * noOfEdges * noOfEdges)))
    return Q


# Complexity: O(m*n^2)
def BestCommunityGreedy(networkGraph, degrees, noOfEdges):
    bestQ = 0.0
    bestSplit = []
    while True:
        RemoveEdges(networkGraph)
        currentQ = GetModularityQ(networkGraph, degrees, noOfEdges)
        if currentQ > bestQ:
            bestQ = currentQ
            bestSplit = list(nx.connected_components(networkGraph))
        if networkGraph.number_of_edges() == 0:
            break
    print("Best modularity: ", bestQ, "\nNumber of communities: ", len(bestSplit), "\nCommunities: ", bestSplit)
    return bestSplit


if __name__ == '__main__':
    inputs = ["graphs/dolphins.gml", "graphs/football.gml", "graphs/karate.gml", "graphs/krebs.gml",
              "graphs/france.gml", "graphs/dogs.gml", "graphs/painters.gml", "graphs/musicians.gml",
              "graphs/actors.gml", "graphs/writers.gml"]
    outputs = ["outputs/dolphins.txt", "outputs/football.txt", "outputs/karate.txt", "outputs/krebs.txt",
               "outputs/france.txt", "outputs/dogs.txt", "outputs/painters.txt", "outputs/musicians.txt",
               "outputs/actors.txt", "outputs/writers.txt"]
    number = 2

    network = nx.read_gml(inputs[number], label='id')

    weight = WeightOfNetwork(network)

    originalDegrees = DegreesForGraph(network)

    split = BestCommunityGreedy(network, originalDegrees, weight)

    communities = [0 for node in range(network.number_of_nodes())]

    ok = 0
    # in case of karate.gml, where nodes start at 1, not 0
    for component in nx.connected_components(network):
        if 0 in component:
            ok = 1

    # assigning a number for each position, so that will map in
    # witch community a node is
    noOfCommunity = 1
    for community in split:
        for node in community:
            if ok == 0:
                communities[node - 1] = noOfCommunity
            else:
                communities[node] = noOfCommunity
        noOfCommunity = noOfCommunity + 1

    print("Positions: ", communities)

    # re-read the network, because our algorithm eliminates all edges
    network = nx.read_gml(inputs[number], label='id')

    print("Time of execution: ", time.time() - start_time)

    # display the network
    plotNetwork(network, communities, inputs[number])

    # writing the results to output files
    file = open(outputs[number], "w")
    for position in range(len(communities)):
        text = "{} : {} \n"
        file.write(text.format(position + 1, list(communities)[position]))
    file.close()
