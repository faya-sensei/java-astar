import org.faya.sensei.mathematics.Matrix4x4;
import org.faya.sensei.mathematics.Quaternion;
import org.faya.sensei.mathematics.Vector3;
import org.faya.sensei.mathematics.Vector4;
import org.faya.sensei.visualization.*;
import org.faya.sensei.visualization.components.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIf;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;
import org.mockito.MockedStatic;

import java.nio.ByteBuffer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

public class VisualizationTest {

    @Nested
    class ComponentTest {

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

        @Nested
        class CameraTest {

            private Camera camera;
            private Transform transform;

            @BeforeEach
            public void setUp() {
                var entity = new EngineEntity();
                camera = entity.addComponent(new Camera());
                transform = entity.getTransform();

                camera.start();
            }

            @Test
            public void testMove() {
                camera.move(new Vector3(0.0f, 0.0f, 3.0f));
                assertEquals(new Vector3(0.0f, 0.0f, 0.0f), transform.getLocalPosition());
            }

            @Test
            public void testRotation() {
                camera.rotate(45.0f, 0.0f);
                Vector3 eulerAngles = Quaternion.toEuler(transform.getLocalRotation(), Quaternion.RotationOrder.ZYX);
                assertEquals(Math.toRadians(45.0f), eulerAngles.y(), 0.0001f);
            }

            @Test
            public void testGetViewMatrix() {
                transform.setLocalPosition(new Vector3(1.0f, 2.0f, 3.0f));
                transform.setLocalRotation(Quaternion.fromEuler(new Vector3(0.0f, (float) Math.toRadians(90.0f), 0.0f), Quaternion.RotationOrder.ZYX));

                final Matrix4x4 expectedViewMatrix = new Matrix4x4(
                         0.0f, 0.0f, 1.0f, -3.0f,
                         0.0f, 1.0f, 0.0f, -2.0f,
                        -1.0f, 0.0f, 0.0f,  1.0f,
                         0.0f, 0.0f, 0.0f,  1.0f
                );

                final Matrix4x4 actualViewMatrix = camera.getViewMatrix();

                assertArrayEquals(
                        Matrix4x4.toFloatArray(expectedViewMatrix),
                        Matrix4x4.toFloatArray(actualViewMatrix),
                        0.0001f
                );
            }
        }

        @Nested
        class MeshFilterTest {

            private MeshFilter meshFilter;

            @BeforeEach
            public void setUp() {
                final float[] vertices = {0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f};
                final float[] uvs = {0.0f, 1.0f, 2.0f, 3.0f};
                final int[] indices = {0, 1, 2, 3};

                meshFilter = new MeshFilter(vertices, uvs, indices);
            }

            @Test
            public void testVertices() {
                float[] vertices = meshFilter.getVertices();
                assertArrayEquals(new float[]{0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f}, vertices);
            }

            @Test
            public void testUvs() {
                float[] uvs = meshFilter.getUvs();
                assertArrayEquals(new float[]{0.0f, 1.0f, 2.0f, 3.0f}, uvs);
            }

            @Test
            public void testIndices() {
                int[] indices = meshFilter.getIndices();
                assertArrayEquals(new int[]{0, 1, 2, 3}, indices);
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
    class EngineRendererTest {

        private static final int WINDOW_WIDTH = 9;
        private static final int WINDOW_HEIGHT = 9;

        private static final int[] BACKGROUND_COLOR = { 0, 0, 0, 0 }; // None
        private static final int[] TRIANGLE_COLOR = { 255, 0, 0, 255 }; // Red
        private static final int[][] PATTERN = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        };

        private static boolean isGpuAvailable;
        private static Window window;

        public boolean isGpuAvailable() {
            return isGpuAvailable;
        }

        @BeforeAll
        public static void prepare() {
            try {
                window = new Window("Test", WINDOW_WIDTH, WINDOW_HEIGHT);
                isGpuAvailable = true;
            } catch (Exception e) {
                isGpuAvailable = false;
            }
        }

        @Test
        @EnabledIf("isGpuAvailable")
        public void testRenderGPU() {
            final Shader shader = new Shader(
                    List.of(
                            new Shader.ShaderModuleData("shaders/engine-vertex.glsl", GL_VERTEX_SHADER),
                            new Shader.ShaderModuleData("shaders/engine-fragment.glsl", GL_FRAGMENT_SHADER)
                    )
            );

            glfwMakeContextCurrent(window.getHandle());

            final EngineRenderer renderer = new EngineRenderer(window);
            renderer.init();

            final float[] vertices = {
                    -0.5f, -0.5f, 0.0f, // Bottom left
                     0.5f, -0.5f, 0.0f, // Bottom right
                     0.0f,  0.5f, 0.0f, // Top center
            };

            final float[] uvs = {
                    0.0f, 0.0f, // Bottom left
                    1.0f, 0.0f, // Bottom right
                    0.5f, 1.0f, // Top center
            };

            final int[] indices = {
                    0, 1, 2
            };

            final EngineEntity cameraEntity = new EngineEntity();
            cameraEntity.addComponent(new Camera());

            final EngineEntity modelEntity = new EngineEntity();
            modelEntity.addComponent(new MeshFilter(vertices, uvs, indices));
            modelEntity.addComponent(new MeshRenderer(shader));

            cameraEntity.getComponents().forEach(Component::start);
            modelEntity.getComponents().forEach(Component::start);

            renderer.setCamera((Camera) cameraEntity.getComponent(Camera.class));
            renderer.setRenderer(List.of((MeshRenderer) modelEntity.getComponent(MeshRenderer.class)));
            renderer.render();

            final ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(WINDOW_WIDTH * WINDOW_HEIGHT * 4);
            glReadPixels(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);

            for (int y = 0; y < WINDOW_HEIGHT; ++y) {
                for (int x = 0; x < WINDOW_WIDTH; ++x) {
                    final int index = (y * WINDOW_WIDTH + x) * 4;

                    final int[] actualPixel = new int[]{
                            Byte.toUnsignedInt(pixelBuffer.get(index)),
                            Byte.toUnsignedInt(pixelBuffer.get(index + 1)),
                            Byte.toUnsignedInt(pixelBuffer.get(index + 2)),
                            Byte.toUnsignedInt(pixelBuffer.get(index + 3))
                    };

                    System.out.printf("[%d, %d] rgb(%d, %d, %d, %d)%n", x, y, actualPixel[0], actualPixel[1], actualPixel[2], actualPixel[3]);
                    assertArrayEquals(PATTERN[y][x] == 1 ? TRIANGLE_COLOR : BACKGROUND_COLOR, actualPixel);
                }
            }

            cameraEntity.getComponents().forEach(Component::dispose);
            modelEntity.getComponents().forEach(Component::dispose);
            renderer.dispose();
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

    @Nested
    class ShaderTest {

        private static final int WINDOW_WIDTH = 9;
        private static final int WINDOW_HEIGHT = 9;

        private static final int[] BACKGROUND_COLOR = { 0, 0, 0, 0 }; // None
        private static final int[] TRIANGLE_COLOR = { 255, 192, 203, 255 }; // Pink
        private static final int[][] PATTERN = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
        };

        private static boolean isGpuAvailable;
        private static Window window;

        public boolean isGpuAvailable() {
            return isGpuAvailable;
        }

        @BeforeAll
        public static void prepare() {
            try {
                window = new Window("Test Shader", WINDOW_WIDTH, WINDOW_HEIGHT);
                isGpuAvailable = true;
            } catch (Exception e) {
                isGpuAvailable = false;
            }
        }

        @Test
        public void testShader() {
            final ShaderSystem shaderSystem = ShaderSystem.getInstance();
            final Shader shader = new Shader(
                    List.of(
                            new Shader.ShaderModuleData("shaders/shader-vertex.glsl", GL_VERTEX_SHADER),
                            new Shader.ShaderModuleData("shaders/shader-fragment.glsl", GL_FRAGMENT_SHADER)
                    )
            );

            try (MockedStatic<GL20> mockedGL = mockStatic(GL20.class)) {
                final int mockProgramId = 1;
                final int mockUniformLocation = 10;

                mockedGL.when(GL20::glCreateProgram).thenReturn(mockProgramId);

                mockedGL.when(() -> GL20.glCreateShader(GL_VERTEX_SHADER)).thenReturn(2);
                mockedGL.when(() -> GL20.glCreateShader(GL_FRAGMENT_SHADER)).thenReturn(3);

                mockedGL.when(() -> GL20.glGetShaderi(anyInt(), eq(GL_COMPILE_STATUS))).thenReturn(1);
                mockedGL.when(() -> GL20.glGetProgrami(anyInt(), eq(GL_LINK_STATUS))).thenReturn(1);

                shader.init();

                mockedGL.when(() -> GL20.glGetUniformLocation(1, "uColor")).thenReturn(mockUniformLocation);

                shaderSystem.registerGlobalUniform("uColor");
                shader.registerUniform("uColor");
                shaderSystem.setUniform("uColor", new Vector4(1.0f, 0.0f, 0.0f, 1.0f));

                mockedGL.verify(() -> GL20.glCreateShader(GL_VERTEX_SHADER));
                mockedGL.verify(() -> GL20.glCreateShader(GL_FRAGMENT_SHADER));
                mockedGL.verify(() -> GL20.glAttachShader(1, 2));
                mockedGL.verify(() -> GL20.glAttachShader(1, 3));
                mockedGL.verify(() -> GL20.glLinkProgram(1));
            }
        }

        @Test
        @EnabledIf("isGpuAvailable")
        public void testShaderGPU() {
            glfwMakeContextCurrent(window.getHandle());
            GL.createCapabilities();
            glViewport(0, 0, window.getWidth(), window.getHeight());

            final ShaderSystem shaderSystem = ShaderSystem.getInstance();
            final Shader shader = new Shader(
                    List.of(
                            new Shader.ShaderModuleData("shaders/shader-vertex.glsl", GL_VERTEX_SHADER),
                            new Shader.ShaderModuleData("shaders/shader-fragment.glsl", GL_FRAGMENT_SHADER)
                    )
            );

            final int vaoId = glGenVertexArrays();
            final int vboId = glGenBuffers();

            try {
                shader.init();

                shaderSystem.registerGlobalUniform("uColor");
                shader.registerUniform("uColor");

                final float[] vertices = {
                         0.0f,  0.5f, 0.0f,   // Top vertex
                        -0.5f, -0.5f, 0.0f,   // Bottom-left vertex
                         0.5f, -0.5f, 0.0f    // Bottom-right vertex
                };

                glBindVertexArray(vaoId);

                glBindBuffer(GL_ARRAY_BUFFER, vboId);
                glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

                glEnableVertexAttribArray(0);
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

                glBindBuffer(GL_ARRAY_BUFFER, 0);
                glBindVertexArray(0);

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                shaderSystem.setUniform("uColor", new Vector4(TRIANGLE_COLOR[0] / 255.0f, TRIANGLE_COLOR[1] / 255.0f, TRIANGLE_COLOR[2] / 255.0f, TRIANGLE_COLOR[3] / 255.0f));

                shader.useProgram();

                glBindVertexArray(vaoId);
                glDrawArrays(GL_TRIANGLES, 0, 3);
                glBindVertexArray(0);

                shader.stopProgram();

                final ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(WINDOW_WIDTH * WINDOW_HEIGHT * 4);
                glReadPixels(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer);

                for (int y = 0; y < WINDOW_HEIGHT; ++y) {
                    for (int x = 0; x < WINDOW_WIDTH; ++x) {
                        final int index = (y * WINDOW_WIDTH + x) * 4;

                        final int[] actualPixel = new int[]{
                                Byte.toUnsignedInt(pixelBuffer.get(index)),
                                Byte.toUnsignedInt(pixelBuffer.get(index + 1)),
                                Byte.toUnsignedInt(pixelBuffer.get(index + 2)),
                                Byte.toUnsignedInt(pixelBuffer.get(index + 3))
                        };

                        System.out.printf("[%d, %d] rgb(%d, %d, %d, %d)%n", x, y, actualPixel[0], actualPixel[1], actualPixel[2], actualPixel[3]);
                        assertArrayEquals(PATTERN[y][x] == 1 ? TRIANGLE_COLOR : BACKGROUND_COLOR, actualPixel);
                    }
                }
            } finally {
                shaderSystem.dispose();
                glDeleteBuffers(vboId);
                glDeleteVertexArrays(vaoId);
                glfwMakeContextCurrent(NULL);
            }
        }
    }
}
