import org.faya.sensei.IGraph;
import org.faya.sensei.IGraphBuilder;
import org.faya.sensei.INode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GraphImplTest {

    @Nested
    public class GraphBuilderTest {

        private static Class<? extends IGraphBuilder> graphBuilderClass;

        private IGraphBuilder graphBuilder;

        @BeforeAll
        public static void prepare() {
            final String packageName = "org.faya.sensei";

            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes));

            final Set<Class<? extends IGraphBuilder>> graphBuilderClasses = reflections.getSubTypesOf(IGraphBuilder.class);

            graphBuilderClass = graphBuilderClasses.iterator().next();
        }

        @BeforeEach
        public void setUp() throws Exception {
            final Constructor<? extends IGraphBuilder> constructor = graphBuilderClass.getConstructor();

            assertNotNull(constructor);

            graphBuilder = constructor.newInstance();

            assertNotNull(graphBuilder);
        }

        @Test
        public void testBuildGrid2DGraph() {
            final IGraph graph = graphBuilder.build(5, 5);

            assertNotNull(graph);
        }

        @Test
        public void testBuildGrid3DGraph() {
            final IGraph graph = graphBuilder.build(5, 5, 5);

            assertNotNull(graph);
        }

        @Test
        public void testBuildMeshGraph() throws IOException {
            try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("plane.glb")) {
                assertNotNull(inputStream);

                final Path tempFile = Files.createTempFile("tempPlane", ".gltf");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

                try (final AIScene scene = Assimp.aiImportFile(tempFile.toString(),
                        Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate)) {
                    assertNotNull(scene);

                    try (final AIMesh mesh = AIMesh.create(Objects.requireNonNull(scene.mMeshes()).get(0))) {
                        final IGraph graph = graphBuilder.build(mesh);

                        assertNotNull(graph);
                    }
                } finally {
                    Files.deleteIfExists(tempFile);
                }
            }
        }
    }

    @Nested
    public class TwoDimensionalGridGraphTest {

        private static Class<? extends IGraphBuilder> graphBuilderClass;

        private IGraph graph;

        @BeforeAll
        public static void prepare() {
            final String packageName = "org.faya.sensei";

            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes));

            final Set<Class<? extends IGraphBuilder>> graphBuilderClasses = reflections.getSubTypesOf(IGraphBuilder.class);

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

            graphBuilderClass = graphBuilderClasses.iterator().next();
        }

        @BeforeEach
        public void setUp() throws Exception {
            final Constructor<? extends IGraphBuilder> constructor = graphBuilderClass.getConstructor();

            assertNotNull(constructor);

            final IGraphBuilder graphBuilder = constructor.newInstance();

            assertNotNull(graphBuilder);

            graph = graphBuilder.build(5, 5);
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
                        final INode node = graph.getNode(new double[]{-1, 2});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{2, -1});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{5, 2});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{2, 5});

                        assertNull(node);
                    }
            );
        }

        @Test
        public void testGetNeighbors() {
            final INode node = graph.getNode(new double[]{2, 2});
            final List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(4, neighbors.size());
            assertTrue(neighbors.contains(graph.getNode(new double[]{1, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{3, 2})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 1})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{2, 3})));
        }

        @Test
        public void testGetNeighbors_EdgeCase() {
            final INode node = graph.getNode(new double[]{0, 0});
            final List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(2, neighbors.size());
            assertTrue(neighbors.contains(graph.getNode(new double[]{1, 0})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{0, 1})));
        }
    }

    @Nested
    public class ThreeDimensionalGridGraphTest {

        private static Class<? extends IGraphBuilder> graphBuilderClass;

        private IGraph graph;

        @BeforeAll
        public static void prepare() {
            final String packageName = "org.faya.sensei";

            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes));

            final Set<Class<? extends IGraphBuilder>> graphBuilderClasses = reflections.getSubTypesOf(IGraphBuilder.class);

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

            graphBuilderClass = graphBuilderClasses.iterator().next();
        }

        @BeforeEach
        public void setUp() throws Exception {
            final Constructor<? extends IGraphBuilder> constructor = graphBuilderClass.getConstructor();

            assertNotNull(constructor);

            final IGraphBuilder graphBuilder = constructor.newInstance();

            assertNotNull(graphBuilder);

            graph = graphBuilder.build(5, 5, 5);
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
                        final INode node = graph.getNode(new double[]{-1, 2, 2});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{2, -1, 2});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{2, 2, -1});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{5, 2, 2});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{2, 5, 2});

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(new double[]{2, 2, 5});

                        assertNull(node);
                    }
            );
        }

        @Test
        public void testGetNeighbors() {
            final INode node = graph.getNode(new double[]{2, 2, 2});
            final List<INode> neighbors = graph.getNeighbors(node);

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
            final INode node = graph.getNode(new double[]{0, 0, 0});
            final List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(3, neighbors.size());
            assertTrue(neighbors.contains(graph.getNode(new double[]{1, 0, 0})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{0, 1, 0})));
            assertTrue(neighbors.contains(graph.getNode(new double[]{0, 0, 1})));
        }
    }

    @Nested
    public class MeshGraphTest {

        private static Class<? extends IGraphBuilder> graphBuilderClass;

        private IGraph graph;

        @BeforeAll
        public static void prepare() {
            final String packageName = "org.faya.sensei";

            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes));

            final Set<Class<? extends IGraphBuilder>> graphBuilderClasses = reflections.getSubTypesOf(IGraphBuilder.class);

            final Set<Class<? extends IGraph>> graphClasses = reflections.getSubTypesOf(IGraph.class);

            assertFalse(graphClasses.isEmpty(), "No implementations found for IGraph interface");

            final Optional<Class<? extends IGraph>> graph = graphClasses.stream()
                    .filter(clazz -> {
                        for (final Field field : clazz.getDeclaredFields()) {
                            if (Map.class.isAssignableFrom(field.getType())) {
                                return true;
                            }
                        }

                        return false;
                    })
                    .findFirst();

            assertTrue(graph.isPresent(), "No suitable IGraph mesh implementation found");

            graphBuilderClass = graphBuilderClasses.iterator().next();
        }

        @BeforeEach
        public void setUp() throws Exception {
            final Constructor<? extends IGraphBuilder> constructor = graphBuilderClass.getConstructor();

            assertNotNull(constructor);

            final IGraphBuilder graphBuilder = constructor.newInstance();

            assertNotNull(graphBuilder);

            //  6----5----7
            //  |  / |  / |
            //  | /  | /  |
            //  4----2----0
            //  |  / |  / |
            //  | /  | /  |
            //  3----1----8

            try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("plane.glb")) {
                assertNotNull(inputStream);

                final Path tempFile = Files.createTempFile("tempPlane", ".gltf");
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

                try (final AIScene scene = Assimp.aiImportFile(tempFile.toString(),
                        Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate)) {
                    assertNotNull(scene);

                    try (final AIMesh mesh = AIMesh.create(Objects.requireNonNull(scene.mMeshes()).get(0))) {
                        graph = graphBuilder.build(mesh);

                        assertNotNull(graph);
                    }
                } finally {
                    Files.deleteIfExists(tempFile);
                }
            }
        }

        @Test
        public void testGetNode() {
            final INode node = graph.getNode(2);

            assertArrayEquals(new double[]{0.0, 0.0, 0.0}, node.getPosition(), 1e-6);
        }

        @Test
        public void testGetNode_OutOfBounds() {
            assertAll(
                    () -> {
                        final INode node = graph.getNode(26);

                        assertNull(node);
                    },
                    () -> {
                        final INode node = graph.getNode(-1);

                        assertNull(node);
                    }
            );
        }

        @Test
        public void testGetNeighbors() {
            final INode node = graph.getNode(2);
            final List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(6, neighbors.size());
        }

        @Test
        public void testGetNeighbors_EdgeCase() {
            final INode node = graph.getNode(8);
            final List<INode> neighbors = graph.getNeighbors(node);

            assertEquals(2, neighbors.size());
        }
    }
}