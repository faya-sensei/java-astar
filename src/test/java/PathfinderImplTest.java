import org.faya.sensei.IGraph;
import org.faya.sensei.IHeuristic;
import org.faya.sensei.INode;
import org.faya.sensei.IPathfinder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PathfinderImplTest {

    private static Class<? extends IPathfinder> pathFinderClass;

    private IGraph graph;

    private IHeuristic heuristic;

    private IPathfinder pathfinder;

    private static class AccessibleDecoratorImpl extends INode.Decorator {

        private boolean accessible = true;

        public AccessibleDecoratorImpl(INode decoratedNode) {
            super(decoratedNode);
        }

        public boolean isTraversable() {
            return accessible;
        }

        public void setTraversable(boolean traversable) {
            this.accessible = traversable;
        }
    }

    private static class NodeImpl implements INode {

        private final double[] position;

        private double gCost, hCost;

        private INode parent;

        public NodeImpl(double[] position) {
            this.position = position;
        }

        @Override
        public double[] getPosition() {
            return position;
        }

        @Override
        public double getGCost() {
            return gCost;
        }

        @Override
        public void setGCost(double gCost) {
            this.gCost = gCost;
        }

        @Override
        public double getHCost() {
            return hCost;
        }

        @Override
        public void setHCost(double hCost) {
            this.hCost = hCost;
        }

        @Override
        public double getFCost() {
            return gCost + hCost;
        }

        @Override
        public INode getParent() {
            return parent;
        }

        @Override
        public void setParent(INode parent) {
            this.parent = parent;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            final INode other = (INode) obj;
            return position[0] == other.getPosition()[0] && position[1] == other.getPosition()[1];
        }

        @Override
        public int hashCode() {
            return Objects.hash(position[0], position[1]);
        }
    }

    @BeforeAll
    public static void prepare() {
        final String packageName = "org.faya.sensei";

        final Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.SubTypes));

        final Set<Class<? extends IPathfinder>> pathfinderClasses = reflections.getSubTypesOf(IPathfinder.class);

        assertFalse(pathfinderClasses.isEmpty(), "No implementations found for IPathfinder interface");

        pathFinderClass = pathfinderClasses.iterator().next();
    }

    @BeforeEach
    public void setUp() throws Exception {
        final Constructor<? extends IPathfinder> constructor = pathFinderClass.getConstructor(IGraph.class, IHeuristic.class);

        assertNotNull(constructor);

        graph = new IGraph() {

            private final int width = 5, height = 5;
            private final INode[][] nodes;

            {
                nodes = new INode[width][height];

                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        nodes[x][y] = new AccessibleDecoratorImpl(new NodeImpl(new double[] {x, y}));
                    }
                }
            }

            @Override
            public INode getNode(double[] position) {
                final int x = (int) position[0];
                final int y = (int) position[1];

                return x >= 0 && x < width && y >= 0 && y < height
                        ? nodes[x][y]
                        : null;
            }

            @Override
            public List<INode> getNeighbors(INode node) {
                List<INode> neighbors = new ArrayList<>();
                final int x = (int) node.getPosition()[0];
                final int y = (int) node.getPosition()[1];

                if (x > 0 && ((AccessibleDecoratorImpl) nodes[x - 1][y]).isTraversable())
                    neighbors.add(nodes[x - 1][y]);
                if (x < width - 1 && ((AccessibleDecoratorImpl) nodes[x + 1][y]).isTraversable())
                    neighbors.add(nodes[x + 1][y]);
                if (y > 0 && ((AccessibleDecoratorImpl) nodes[x][y - 1]).isTraversable())
                    neighbors.add(nodes[x][y - 1]);
                if (y < height - 1 && ((AccessibleDecoratorImpl) nodes[x][y + 1]).isTraversable())
                    neighbors.add(nodes[x][y + 1]);

                return neighbors;
            }
        };

        heuristic = (start, goal) -> {
            double dx = goal.getPosition()[0] - start.getPosition()[0];
            double dy = goal.getPosition()[1] - start.getPosition()[1];

            return Math.sqrt(dx * dx + dy * dy);
        };

        pathfinder = constructor.newInstance(graph, heuristic);

        assertNotNull(pathfinder);
    }

    @Test
    public void testFindPath() {
        INode start = graph.getNode(new double[]{0, 0});
        INode goal = graph.getNode(new double[]{4, 4});

        // • • • P G
        // • • • P •
        // • • • P •
        // • P P P •
        // S P • • •

        List<INode> expectedPath = List.of(
                graph.getNode(new double[] {0, 0}),
                graph.getNode(new double[] {1, 0}),
                graph.getNode(new double[] {1, 1}),
                graph.getNode(new double[] {1, 2}),
                graph.getNode(new double[] {2, 2}),
                graph.getNode(new double[] {3, 2}),
                graph.getNode(new double[] {3, 3}),
                graph.getNode(new double[] {3, 4}),
                graph.getNode(new double[] {4, 4})
        );

        List<INode> actualPath = pathfinder.findPath(start, goal);

        assertArrayEquals(expectedPath.toArray(), actualPath.toArray());
    }

    @Test
    public void testFindPath_Obstacle() {
        INode start = graph.getNode(new double[]{0, 0});
        INode goal = graph.getNode(new double[]{4, 4});

        // • • • • G
        // • X X X P
        // • • • X P
        // • • • X P
        // S P P P P

        double[][] obstacles = {
                {1, 3}, {2, 3}, {3, 1}, {3, 2}, {3, 3}
        };

        for (double[] obstacle : obstacles) {
            ((AccessibleDecoratorImpl) graph.getNode(obstacle)).setTraversable(false);
        }

        List<INode> expectedPath = List.of(
                graph.getNode(new double[] {0, 0}),
                graph.getNode(new double[] {1, 0}),
                graph.getNode(new double[] {2, 0}),
                graph.getNode(new double[] {3, 0}),
                graph.getNode(new double[] {4, 0}),
                graph.getNode(new double[] {4, 1}),
                graph.getNode(new double[] {4, 2}),
                graph.getNode(new double[] {4, 3}),
                graph.getNode(new double[] {4, 4})
        );

        List<INode> actualPath = pathfinder.findPath(start, goal);

        assertArrayEquals(expectedPath.toArray(), actualPath.toArray());
    }

    @Test
    public void testFindPath_Decision() {
        INode start = graph.getNode(new double[]{0, 0});
        INode goal = graph.getNode(new double[]{4, 4});

        // • • X P G
        // X • • P X
        // • • X P •
        // X P P P X
        // S P X • •

        double[][] obstacles = {
                {0, 1}, {0, 3}, {2, 0}, {2, 2}, {2, 4}, {4, 2}, {4, 1}
        };

        for (double[] obstacle : obstacles) {
            ((AccessibleDecoratorImpl) graph.getNode(obstacle)).setTraversable(false);
        }

        List<INode> expectedPath = List.of(
                graph.getNode(new double[] {0, 0}),
                graph.getNode(new double[] {1, 0}),
                graph.getNode(new double[] {1, 1}),
                graph.getNode(new double[] {2, 1}),
                graph.getNode(new double[] {3, 1}),
                graph.getNode(new double[] {3, 2}),
                graph.getNode(new double[] {3, 3}),
                graph.getNode(new double[] {3, 4}),
                graph.getNode(new double[] {4, 4})
        );

        List<INode> actualPath = pathfinder.findPath(start, goal);

        assertArrayEquals(expectedPath.toArray(), actualPath.toArray());
    }

    @Test
    public void testFindPath_EmptyPath() {
        INode start = graph.getNode(new double[]{0, 0});
        INode goal = graph.getNode(new double[]{4, 4});

        // • • X P G
        // X • • P X
        // • • X P •
        // X P P P X
        // S P X • •

        double[][] obstacles = {
                {0, 1}, {1, 0}, {1, 1}
        };

        for (double[] obstacle : obstacles) {
            ((AccessibleDecoratorImpl) graph.getNode(obstacle)).setTraversable(false);
        }

        List<INode> expectedPath = List.of();

        List<INode> actualPath = pathfinder.findPath(start, goal);

        assertArrayEquals(expectedPath.toArray(), actualPath.toArray());
    }
}
