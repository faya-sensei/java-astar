import org.faya.sensei.mathematics.Vector3;
import org.faya.sensei.visualization.EngineEntity;
import org.faya.sensei.visualization.EngineScene;
import org.faya.sensei.visualization.components.Camera;
import org.faya.sensei.visualization.components.Transform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VisualizationTest {

    @Nested
    class ComponentsTest {

        @Nested
        class TransformTest {

            private Transform parentTransform;
            private Transform childTransform;

            @BeforeEach
            void setUp() {
                parentTransform = new Transform(null);
                childTransform = new Transform(parentTransform);
            }

            @Test
            void testRelationship() {
                assertEquals(parentTransform, childTransform.getParent());
                assertTrue(parentTransform.getChildren().contains(childTransform));
            }

            @Test
            void testSetParent() {
                Transform newParent = new Transform(null);
                childTransform.setParent(newParent);

                assertEquals(newParent, childTransform.getParent());
                assertTrue(newParent.getChildren().contains(childTransform));
                assertFalse(parentTransform.getChildren().contains(childTransform));
            }

            @Test
            void testGetWorldPosition() {
                parentTransform.setLocalPosition(new Vector3(1.0f, 2.0f, 3.0f));
                childTransform.setLocalPosition(new Vector3(4.0f, 5.0f, 6.0f));

                Vector3 expectedWorldPosition = new Vector3(5.0f, 7.0f, 9.0f);
                assertEquals(expectedWorldPosition, childTransform.getWorldPosition());
            }
        }
    }

    @Nested
    class EngineEntityTest {

        private EngineEntity parentEntity;
        private EngineEntity childEntity;

        @BeforeEach
        void setUp() {
            parentEntity = new EngineEntity();
            childEntity = new EngineEntity(parentEntity.getTransform());
        }

        @Test
        void testRelationship() {
            assertEquals(parentEntity, childEntity.getParent());
            assertTrue(parentEntity.getChildren().contains(childEntity));
        }

        @Test
        void testAddComponent() {
            Camera camera = new Camera();
            parentEntity.addComponent(camera);

            assertEquals(camera, parentEntity.getComponent(Camera.class));
        }
    }

    @Nested
    class EngineSceneTest {

        private EngineScene scene;
        private EngineEntity entity;

        @BeforeEach
        void setUp() {
            scene = new EngineScene();
            entity = new EngineEntity();
        }

        @Test
        void testAddEntity() {
            scene.addEntity(entity);
            assertTrue(scene.getComponents().containsAll(entity.getComponents()));
        }

        @Test
        void testAddEntity_WithChildren() {
            EngineEntity childEntity = new EngineEntity(entity.getTransform());
            scene.addEntity(entity);

            assertTrue(scene.getComponents().containsAll(entity.getComponents()));
            assertTrue(scene.getComponents().containsAll(childEntity.getComponents()));
        }
    }
}
