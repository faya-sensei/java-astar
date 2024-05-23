import org.faya.sensei.INode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NodeImplTest {

    private Class<? extends INode> nodeClass;

    @BeforeEach
    public void setUp() {
        String packageName = "org.faya.sensei";

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(false)));

        Set<Class<? extends INode>> nodeClasses = reflections.getSubTypesOf(INode.class);
        assertFalse(nodeClasses.isEmpty(), "No implementations found for Node interface");

        nodeClass = nodeClasses.iterator().next();
    }

    @Test
    public void testNodeImplementation() throws Exception {
        Constructor<? extends INode> constructor = nodeClass.getConstructor(int.class, int.class);
        INode node = constructor.newInstance(1, 2);

        assertArrayEquals(new float[]{1.0f, 2.0f}, node.getPosition(), "GridNode getPosition should return correct coordinates");

        node.setCost(10.0);
        assertEquals(10.0, node.getCost(), "GridNode getCost should return correct cost");
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        Constructor<? extends INode> constructor = nodeClass.getConstructor(int.class, int.class);
        INode node1 = constructor.newInstance(1, 2);
        INode node2 = constructor.newInstance(1, 2);

        assertEquals(node1, node2, "GridNode equals should return true for nodes with the same coordinates");
        assertEquals(node1.hashCode(), node2.hashCode(), "GridNode hashCode should return the same value for nodes with the same coordinates");
    }
}
