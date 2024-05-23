import org.faya.sensei.IGraph;
import org.faya.sensei.INode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GraphImplTest {

    private Class<? extends IGraph> graphClass;
    private IGraph graph;
    private INode nodeClassInstance;

    @BeforeEach
    public void setUp() throws Exception {
        String packageName = "org.faya.sensei";

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(false)));

        Set<Class<? extends IGraph>> graphClasses = reflections.getSubTypesOf(IGraph.class);
        assertFalse(graphClasses.isEmpty(), "No implementations found for Graph interface");

        graphClass = graphClasses.iterator().next();

        Constructor<? extends IGraph> constructor = graphClass.getConstructor(int.class, int.class);
        graph = constructor.newInstance(5, 5);

        Set<Class<? extends INode>> nodeClasses = reflections.getSubTypesOf(INode.class);
        assertFalse(nodeClasses.isEmpty(), "No implementations found for Node interface");

        Class<? extends INode> nodeClass = nodeClasses.iterator().next();
        Constructor<? extends INode> nodeConstructor = nodeClass.getConstructor(int.class, int.class);
        nodeClassInstance = nodeConstructor.newInstance(2, 2);
    }

    @Test
    public void testGraphNeighbors() {
        List<INode> neighbors = graph.getNeighbors(nodeClassInstance);
        assertEquals(4, neighbors.size());
    }

    @Test
    public void testGetCost() {
        INode nodeA = nodeClassInstance;
        INode nodeB = graph.getNeighbors(nodeClassInstance).get(0);
        assertEquals(1.0, graph.getCost(nodeA, nodeB));
    }
}