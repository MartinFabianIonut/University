package socialnetwork.domain.graph;

import java.util.*;

public class UndirectedGraph {
    protected Map<Long, HashSet<Long>> adjMap;
    protected Map<Long, Boolean> visited = new HashMap<>();

    /**
     *
     * @param adjMap - creating the graph based on an adjacency matrix that contains users as nodes
     *               and sets false to visited map
     */
    public UndirectedGraph(Map<Long, HashSet<Long>> adjMap) {
        this.adjMap = adjMap;
        for (Long userId : adjMap.keySet()) {
            visited.put(userId, false); // daca il mut in al doilea for, obtin doar comunitatile care au cel putin 2 prieteni
            //astfel, consider ca o comunitate de sine statatoare si un utilizator neprietenos (nod izolat)
            for (Long friendId : adjMap.get(userId)) {
                visited.put(friendId, false);
            }
        }
    }

    /**
     * Put false to every vertex in graph, that means is not visited
     */
    private void resetVisited() {
        visited.replaceAll((k, v) -> false);
    }


    /**
     *
     * @param vertex type Long - is the vertex from which we begin to traverse the graph recursively
     */
    private void DFS(Long vertex) {
        visited.put(vertex, true);
        for (Long adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                 DFS(adjVertex);

            }
        }
    }

    private int DFS_path(Long vertex) {
        visited.put(vertex, true);
        int maxPath = 0;
        for (Long adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                int path = DFS_path(adjVertex);
                maxPath = Math.max(maxPath,path);
            }
        }
        return maxPath + 1;
    }

    /**
     *
     * @return the number of related components of the graph, i.e. the number of communities
     */
    public int getConnectedComponentsCount() {
        int count = 0;
        resetVisited();
        for (Long vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                DFS(vertex);
                count++;
            }
        }
        return count;
    }

    /**
     *
     * @param vertex - the node from which the construction of the subgraph (of the community that contains it) begins
     * @param connectedComponent - collection of Long numbers, which represents the connected component of the graph
     *                           in which the vertex node is located
     */
    private void makeSubgraph(Long vertex, Collection<Long> connectedComponent) {
        connectedComponent.add(vertex);
        visited.put(vertex, true);
        for (Long adjVertex : adjMap.get(vertex)) {
            if (!visited.get(adjVertex)) {
                makeSubgraph(adjVertex, connectedComponent);
            }
        }
    }

    /**
     *
     * @return the number of visited vertexes at a moment
     */
    private int howManyVisited(){
        int count=0;
        for(Long adjVertex: visited.keySet())
            if(visited.get(adjVertex))
                count++;
        return count;
    }

    /**
     *
     * @return the most sociable community, in the form of a Iterable<Long>, which contains id-s of users
     */
    public Iterable<Long> getConnectedComponentMostSociable() {
        resetVisited();
        int maxPathLength = 0, currentPathLength;
        int n = visited.size();
        int copyOfN = n;
        Long startVertex = (long) -1;
        for (Long vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                DFS(vertex);
                currentPathLength = n - copyOfN + howManyVisited();
                n = n - howManyVisited();
                if (maxPathLength < currentPathLength) {
                    maxPathLength = currentPathLength;
                    startVertex = vertex;
                }
            }
        }
        Collection<Long> connectedComponent = new ArrayList<>();
        resetVisited();
        makeSubgraph(startVertex, connectedComponent);
        return connectedComponent;
    }

    public Iterable<Long> getConnectedComponentWithLongestRoad() {
        resetVisited();
        int maxPathLength = 0;
        Long startVertex = (long) -1;
        for (Long vertex : visited.keySet()) {
            if (!visited.get(vertex)) {
                int currentPathLength = DFS_path(vertex);
                if (maxPathLength < currentPathLength) {
                    maxPathLength = currentPathLength;
                    startVertex = vertex;
                }
            }
        }
        Collection<Long> connectedComponent = new ArrayList<>();
        resetVisited();
        makeSubgraph(startVertex, connectedComponent);
        return connectedComponent;
    }

}