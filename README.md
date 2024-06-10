<div align="center">

# A-Star Practice

Another old school java exercise

</div>

# Overview

A* algorithm, as a fundamental programming algorithm, not only helps in
practicing iteration and fostering programming thinking but is also a practical
algorithm widely used in real-world applications.

# Task Checklist

### 1. Node and decorator

#### Instruction:

Programming with object-oriented languages can sometimes be challenging.
Developers might initially rely on procedural programming techniques, such as
wrapping an instance inside another. However, these practices can increase
coupling and are not ideal in an object-oriented context. The Decorator Pattern
emerged as a solution, allowing new functionality to be added to an existing
object without altering its structure.

#### Procedural:

1. **Implement INode interface**: The interface defines the getter setter
structure, attempt to find the best match of the private field.

2. **Extend accessible decorator**: We need an accessible decorator to describe
the accessibility of the node in a grid. 

#### Requirement:

A class inherited the `INode` interface and a decorator class extend
`INode.Decorator`.

```java
INode node = new NodeDecoratorImpl(new NodeImpl());
```

### 2. grid graph and builder

#### Instruction:

When designing a program, starting with a simple model is reasonable. For
complex nodes-edges-graph relationships, simplify them into nodes-graph by
assuming equal edges which place in a grid for a minimal prototype. After that
create a builder for graph, allowing quicker initialization of the graph
instance.

#### Procedural:

1. **Implement grid graph by multidimensional array**: Due to the
standardization of the grid, arrays can be directly used for operations. Design
a grid 2d graph and a grid 3d graph with two or three-dimensional array and
implement the `IGraph` interface.

2. **Implement grid graph builder**: Implement `IGraphBuilder` interface and
finalize the related build function.

   ```java
   IGraph build(final int width, final int height);
   IGraph build(final int width, final int height, final int depth);
   ```

#### Requirement:

A class inherited the `IGraph` interface and a builder inherited the
`IGraphBuilder` interface.

### 3. heuristic and strategy

#### Instruction:

For pluggable code, the strategy pattern is the best solution. For example, the
Manhattan distance has better performance in grid graphs, while the Euclidean
distance can better calculate Euclidean based graphs.

#### Procedural:

1. **Implement Manhattan heuristic**: The Manhattan distance between two points
$x$ and $y$ is $$d(x, y) = |x_1 - x_2| + |y_1 - y_2|$$.

2. **Implement Euclidean heuristic**: The Euclidean distance between two points
$$d(x, y) = \sqrt {(x_1 - y_2)^2 + (y_1 - y_2)^2}$$

### 4. Pathfinder and observer

#### Instruction:

The A* algorithm description refer to [wiki](https://en.wikipedia.org/wiki/A*_search_algorithm#Description)
which can be summarized to the following pseudocode:

```
function reconstruct_path(path, current)
  total_path = [current]
  while current in path do
    current = path[current]
    total_path.prepend(current)
  return total_path

function a_star(start_node, goal_node, heuristic)
  open = [start_node]
  closed = []

  g_score[start_node] = 0
  f_score[start_node] = heuristic(start_node, goal_node)

  while open is not empty do
    current_node = node in open with lowest f_score[current_node]

    if current_node == goal_node then
      return reconstruct_path(closed, current_node)

    open.remove(current_node)

    for each neighbor in neighbor_nodes(current_node) do
      if neighbor is not traversable or neighbor in closed then
        continue

      tentative_g_score = g_score[current_node] + heuristic(current_node, neighbor)
      if tentative_g_score < g_score[neighbor] or neighbor is not in open
        closed[neighbor] = current_node
        g_score[neighbor] = tentative_g_score
        f_score[neighbor] = tentative_g_score + heuristic(neighbor, goal_node)
        if neighbor not in open
          open.add(neighbor)

  return failure
```

The algorithm serves as the core and is reusable. This allows for abstraction
and application design patterns, resulting in more structured code. We consider
using the Bridge Pattern. Additionally, we can apply the Observer Pattern,
enabling observers to record and monitor the process by extending the algorithm.

#### Procedural:

1. **Implement pathfinder**: Implement `IPathfinder` interface and consider
PriorityQueue $O(n^2)$ or TreeSet $O(log_n)$to achieve filtering and sorting,
mark it as abstract and build interface for the subclass.

2. **Implement pathfinder subject**: Implement the subclass of pathfinder
serve as concrete subject for observer.

3. **Check observer can register into pathfinder subject**: Implement
`IPathfinderObserver` interface and try call register observer in pathfinder.