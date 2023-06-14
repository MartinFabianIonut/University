# Optimal Subgroup Identification Algorithm

## Introduction

The Optimal Subgroup Identification Algorithm is designed to identify optimal subgroups within a larger community. It takes input in the form of `.gml` files and produces output in the form of `.txt` files and a graphical representation using the matplotlib.pyplot library.

### Solution Overview

The algorithm consists of the following components:

1. Input Data: The algorithm requires input data in the form of .gml files. These files contain graph data representing the community structure.

2. Community Detection: The algorithm applies a community detection method to identify subgroups within the community. This process helps partition the nodes of the graph into distinct communities based on their connectivity patterns.

3. Output Generation: Once the subgroups are identified, the algorithm generates output files in the .txt format. These files contain information about each node and its assigned community, allowing for further analysis and interpretation.

4. Visualization: The algorithm uses the matplotlib.pyplot library to create visualizations of the identified subgroups. The generated graphs provide a visual representation of the community structure, with nodes color-coded and labeled according to their community membership.

## Input

The algorithm expects .gml files as input, which represent different community or network structures. The following .gml files are required:

- `actors.gml`: Contains an undirected graph with labeled information on actors.
- `dogs.gml`: Contains an undirected graph with labeled information on dog names.
- `france.gml`: Contains an undirected graph with labeled information on French names.
- `musicians.gml`: Contains an undirected graph with labeled information on musicians.
- `painters.gml`: Contains an undirected graph with labeled information on painters.
- `writers.gml`: Contains an undirected graph with labeled information on writers.

## Output

The algorithm generates the following output:

- `.txt` files: Each file contains the node-community mappings in the format "node: community". These files provide information about the optimal subgroups identified within the community.
- Graphical visualization: The algorithm uses the matplotlib.pyplot library to create a graphical representation of the community structure. The graph distinguishes between different communities by color-coding and labeling each node with its ID.
