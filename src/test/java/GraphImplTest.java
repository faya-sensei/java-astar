import org.faya.sensei.IGraph;
import org.faya.sensei.INode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GraphImplTest {

    @Nested
    public class TwoDimensionalGridGraphTest {

        private static Class<? extends IGraph> graphClass;

        private IGraph graph;

        @BeforeAll
        public static void prepare() {
            final String packageName = "org.faya.sensei";

            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes));

            final Set<Class<? extends IGraph>> graphClasses = reflections.getSubTypesOf(IGraph.class);

            assertFalse(graphClasses.isEmpty(), "No implementations found for IGraph interface");

            final Optional<Class<? extends IGraph>> graph = graphClasses.stream()
                    .filter(clazz -> {
                        for (final Field field : clazz.getDeclaredFields()) {
                            if (field.getType().isArray() &&
                                    field.getType().getComponentType().isArray() &&
                                    INode.class.isAssignableFrom(field.getType()
                                            .getComponentType()
                                            .getComponentType())) {
                                return true;
                            }
                        }

                        return false;
                    })
                    .findFirst();

            assertTrue(graph.isPresent(), "No suitable IGraph 2D implementation found");

            graphClass = graph.get();
        }

        @BeforeEach
        public void setUp() throws Exception {
            Constructor<? extends IGraph> constructor = graphClass.getConstructor(int.class, int.class);

            assertNotNull(constructor);

            graph = constructor.newInstance(5, 5);

            assertNotNull(graph);
        }

        @Test
        public void testGetNode() {
            final INode node = graph.getNode(new double[]{2, 2});

            assertNotNull(node);
            assertArrayEquals(new double[]{2, 2}, node.getPosition());
        }

        @Test
        public void testGetNode_OutOfBounds() {
            assertAll(
                    () -> {
                        INode node = graph.getNode(new double[]{-1, 2});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{2, -1});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{5, 2});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{2, 5});

                        assertNull(node);
                    }
            );
        }

        @Test
        public void testGraphNeighbors() {
            final INode node = graph.getNode(new double[]{2, 2});
            List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(4, neighbors.size());
            assertTrue(neighbors.contains(graph.getNode(new double[]{1, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{3, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 1})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 3})));
        }

        @Test
        public void testGetNeighbors_EdgeCase() {
            INode node = graph.getNode(new double[]{0, 0});
            List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(2, neighbors.size());
            assertTrue(neighbors.contains(graph.getNode(new double[]{1, 0})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{0, 1})));
        }
    }

    @Nested
    public class ThreeDimensionalGridGraphTest {

        private static Class<? extends IGraph> graphClass;

        private IGraph graph;

        @BeforeAll
        public static void prepare() {
            final String packageName = "org.faya.sensei";

            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes));

            final Set<Class<? extends IGraph>> graphClasses = reflections.getSubTypesOf(IGraph.class);

            assertFalse(graphClasses.isEmpty(), "No implementations found for IGraph interface");

            final Optional<Class<? extends IGraph>> graph = graphClasses.stream()
                    .filter(clazz -> {
                        for (final Field field : clazz.getDeclaredFields()) {
                            if (field.getType().isArray() &&
                                    field.getType().getComponentType().isArray() &&
                                    field.getType().getComponentType().getComponentType().isArray() &&
                                    INode.class.isAssignableFrom(field.getType()
                                            .getComponentType()
                                            .getComponentType()
                                            .getComponentType())) {
                                return true;
                            }
                        }

                        return false;
                    })
                    .findFirst();

            assertTrue(graph.isPresent(), "No suitable IGraph 3D implementation found");

            graphClass = graph.get();
        }

        @BeforeEach
        public void setUp() throws Exception {
            final Constructor<? extends IGraph> constructor = graphClass.getConstructor(int.class, int.class, int.class);

            assertNotNull(constructor);

            graph = constructor.newInstance(5, 5, 5);

            assertNotNull(graph);
        }

        @Test
        public void testGetNode() {
            final INode node = graph.getNode(new double[]{2, 2, 2});

            assertArrayEquals(new double[]{2, 2, 2}, node.getPosition());
        }

        @Test
        public void testGetNode_OutOfBounds() {
            assertAll(
                    () -> {
                        INode node = graph.getNode(new double[]{-1, 2, 2});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{2, -1, 2});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{2, 2, -1});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{5, 2, 2});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{2, 5, 2});

                        assertNull(node);
                    },
                    () -> {
                        INode node = graph.getNode(new double[]{2, 2, 5});

                        assertNull(node);
                    }
            );
        }

        @Test
        public void testGraphNeighbors() {
            final INode node = graph.getNode(new double[]{2, 2, 2});
            List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(6, neighbors.size());
            assertTrue(neighbors.contains(graph.getNode(new double[]{1, 2, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{3, 2, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 1, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 3, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 2, 1})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 2, 3})));
        }

        @Test
        public void testGetNeighbors_EdgeCase() {
            INode node = graph.getNode(new double[]{0, 0, 0});
            List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(3, neighbors.size());
            assertTrue(neighbors.contains(graph.getNode(new double[]{1, 0, 0})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{0, 1, 0})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{0, 0, 1})));
        }
    }
}