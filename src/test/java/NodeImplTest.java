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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NodeImplTest {

    @Nested
    public class GridNodeTest {

        private static Class<? extends INode> nodeClass;

        private INode node;

        @BeforeAll
        public static void prepare() {
            final String packageName = "org.faya.sensei";

            final Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(packageName))
                    .setScanners(Scanners.SubTypes));

            final Set<Class<? extends INode>> nodeClasses = reflections.getSubTypesOf(INode.class);

            assertFalse(nodeClasses.isEmpty(), "No implementations found for INode interface");

            final Optional<Class<? extends INode>> node = nodeClasses.stream()
                    .filter(clazz -> {
                        for (final Field field : clazz.getDeclaredFields()) {
                            if (field.getType().isArray() &&
                                    double.class.isAssignableFrom(field.getType()
                                            .getComponentType())) {
                                return true;
                            }
                        }

                        return false;
                    })
                    .findFirst();

            assertTrue(node.isPresent(), "No suitable INode 2D implementation found");

            nodeClass = node.get();
        }

        @BeforeEach
        public void setUp() throws Exception {
            final Constructor<? extends INode> constructor = nodeClass.getConstructor(double[].class);

            assertNotNull(constructor);

            node = constructor.newInstance(new double[] {0, 0});

            assertNotNull(node);
        }

        @Test
        public void testConstructor() throws Exception {
            final Constructor<? extends INode> constructor = nodeClass.getConstructor(double[].class, double.class);

            assertNotNull(constructor);

            node = constructor.newInstance(new double[] {0, 0}, 10);

            assertNotNull(node);
            assertEquals(10, node.getGCost());
        }

        @Test
        public void testGetPosition() {
            assertArrayEquals(new double[] {0, 0}, node.getPosition());
        }

        @Test
        public void testGetSetGCost() {
            node.setGCost(1);

            assertEquals(1, node.getGCost());
        }

        @Test
        public void testGetSetHCost() {
            node.setHCost(1);

            assertEquals(1, node.getHCost());
        }

        @Test
        public void testGetSetParent() throws Exception {
            final Constructor<? extends INode> constructor = nodeClass
                    .getConstructor(double[].class);

            final INode parent = constructor.newInstance(new double[] {0, 1});

            node.setParent(parent);

            assertEquals(parent, node.getParent());
        }
    }
}
